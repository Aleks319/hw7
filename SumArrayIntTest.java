package lesson7.hw;

import lesson6.hw.CopyFileBlock;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

public class SumArrayIntTest {
    public static void main(String[] args) {

    }

   // public static int[][] divIntArr (int[] arr)  {
   //     int cntTread = Runtime.getRuntime().availableProcessors();
   //     for (int i=0; i<cntTread; i++) {
   //         int start = i*(lenFile/cntTread);
   //         long end = (i+1 == cntTread) ? lenFile - (lenFile - (((i + 1) * (lenFile/cntTread)) - 1)) + (lenFile%cntTread) : lenFile - (lenFile - (((i + 1) * (lenFile/cntTread)) - 1));
   //         list[i] = new CopyFileBlock.ReadWrite(rafFile, rafCopy, bufSize, start, end);
        }
     //   return 0;
 //   }

//}


class SumArrayInt extends Thread{
    int[] arr;
    private int sum;
    public SumArrayInt(int[] arr) {
        this.arr = arr;
    }

    @Override
    public void run(){
        for(int i: arr) {
            sum += i;
        }
    }

    public int getSum() {
        return sum;
    }
}
