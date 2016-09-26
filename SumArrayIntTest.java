package lesson7.hw;

public class SumArrayIntTest {
    public static void main(String[] args) {
        int cntTread = Runtime.getRuntime().availableProcessors();

        //generate new int array:
        int[] array = intArrRandGenerator(1000000, 10, 150);

        // creation Thread list:
        SumArrayInt[] list = null;
        list = createThreads(array, cntTread);

        //start Threads:
        startThreads(list);

        //join Threads:
        joinThreads(list);

        //get sum:
        long sum = sumArrInt(list);
        long en = System.currentTimeMillis();


    }

    public static int[] intArrRandGenerator(int len, int min, int max) {
        int[] arr = new int[len];

        for (int i = 0; i < len; i++) {
            arr[i] = (min + (int) (Math.random() * max));
        }
        return arr;
    }



    public static SumArrayInt[] createThreads(int[] arr, int cntTread) {
        SumArrayInt[] list = new SumArrayInt[cntTread];
        for (int i=0; i<cntTread; i++) {
            int start = i*(arr.length/cntTread);
            int end = (i+1 == cntTread)
                      ? (((i + 1) * (arr.length/cntTread)) - 1) + (arr.length%cntTread)
                    : (((i + 1) * (arr.length/cntTread)) - 1);
            list[i] = new SumArrayInt (arr, start, end);
        }
        return list;
    }


    public static void startThreads(SumArrayInt[] list) {
        for(SumArrayInt sai: list) {
            sai.start();
        }
    }

    public static void joinThreads(SumArrayInt[] list) {
        for(SumArrayInt sai: list) {
            try {
                sai.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static long sumArrInt(SumArrayInt[] list) {
        long sum = 0;
        for(SumArrayInt sai: list) {
            sum += sai.getSum();
        }
        return sum;
    }


}

class SumArrayInt extends Thread{
    int[] arr;
    int start;
    int end;
    private long sum;
    public SumArrayInt(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run(){
        for(int i = start; i<=end; i++) {
            sum += arr[i];
        }
    }

    public long getSum() {
        return sum;
    }
}