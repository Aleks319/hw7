package lesson7.hw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class ManyReadInOneWrite {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input your directory: ");
        String dir = sc.nextLine();

        ArrayList<String> list = getListTXTFiles(dir);

        try {
            ArrayList<ReadWrite> threadList = new ArrayList<>();
            RandomAccessFile newFile = new RandomAccessFile(dir + "\\" + "InfoFromAllTXTFiles.txt", "rw");
            for (String s: list) {
                threadList.add(new ReadWrite(new RandomAccessFile(s, "r"), newFile));
            }

            for(ReadWrite rw: threadList) {
                rw.start();
            }

            for(ReadWrite rw: threadList) {
                rw.join();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done!");


    }

    public static ArrayList<String> getListTXTFiles(String dir) {
        ArrayList<String> list = new ArrayList<>();
        File listFiles[] = new File(dir).listFiles();
        for (int i = 0; i<listFiles.length; i++) {
            if(listFiles[i].isFile() && listFiles[i].getName().endsWith(".txt")) {
                try {
                    list.add(listFiles[i].getCanonicalPath());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static class ReadWrite extends Thread {
        private RandomAccessFile rafFile;
        private RandomAccessFile rafCopy;

        public ReadWrite(RandomAccessFile rafFile, RandomAccessFile rafCopy) {
            this.rafFile = rafFile;
            this.rafCopy = rafCopy;
        }

        @Override
        public void run() {
            if (rafFile == null || rafCopy == null) {
                return;
            }
            try {
                byte[] buf = new byte[(int) rafFile.length()];
                rafFile.read(buf);

                synchronized (rafCopy) {
                    rafCopy.seek(rafCopy.length());
                    rafCopy.write(buf);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    rafFile.close();
                } catch (IOException e) {
                    return;
                }
            }
        }
    }
}
