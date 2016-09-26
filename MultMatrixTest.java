package lesson7.hw;

public class MultMatrixTest {
    public static void main(String[] args) {
        int[][] matrix1 = {{19, 24, 35},
                {40, 51, 65},
                {71, 82, 99},
                {10, 11, 12}};

        int[][] matrix2 = {{11, 92, 55},
                {45, 35, 12},
                {67, 78, 23}};

        //*******     test MultiThread multiply matrix


        // creation Thread list:
        MultPartMatrix[] list = createThreads(matrix1, matrix2);

        //start Threads:
        long startMultiThread = System.currentTimeMillis();
        startThreads(list);

        //join Threads:
        joinThreads(list);

        //get mult:
        int[][] result1 = resultMatrix(list, matrix1, matrix2);
        long endMultiThread = System.currentTimeMillis();


        //*******     test One Thread multiply matrix
        long startOneThread = System.currentTimeMillis();
        int[][] result2 = matrixMult(matrix1, matrix2);
        long endOneThread = System.currentTimeMillis();

        System.out.println("Result1: ");
        for (int[] i: result1) {
            for (int j: i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }

        System.out.println("*****************");

        System.out.println("Result2: ");
        for (int[] i: result2) {
            for (int j: i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println("*****************");


        System.out.println("MultiThread result: " + (endMultiThread - startMultiThread));
        System.out.println("One Thread result: " + (endOneThread - startOneThread));
        if ((endMultiThread - startMultiThread) < (endOneThread - startOneThread)) {
            System.out.println("MultiThread is better");
        } else if ((endMultiThread - startMultiThread) > (endOneThread - startOneThread)) {
            System.out.println("One Thread is better");
        } else {
            System.out.println("Results are equals.");
        }
    }

    public static int[][] matrixMult(int[][] matrix1, int[][] matrix2) throws MultMatrixException {
        if(matrix1[0].length!=matrix2.length) {
            throw new MultMatrixException("length of first matrix must be equal high of second!");
        }
        int[][] res = new int[matrix1.length][matrix2[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                int q = 0;
                int r = 0;
                while (q < matrix1[i].length) {
                    r += matrix1[i][q] * matrix2[q][j];
                    q++;
                }
                res[i][j] = r;
            }
        }
        return res;
    }


    public static MultPartMatrix[] createThreads(int[][] matrix1, int[][] matrix2) {
        int cntTread = matrix1.length;
        MultPartMatrix[] list = new MultPartMatrix[cntTread];
        for (int i=0; i<cntTread; i++) {

            list[i] = new MultPartMatrix (matrix1, matrix2, i);
        }
        return list;
    }


    public static void startThreads(MultPartMatrix[] list) {
        for(MultPartMatrix mpm: list) {
            try{
                mpm.start();
            } catch (MultMatrixException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public static void joinThreads(MultPartMatrix[] list) {
        for(MultPartMatrix mpm: list) {
            try {
                mpm.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int[][] resultMatrix (MultPartMatrix[] list, int[][] matrix1, int[][] matrix2) {
        int[][] res = new int[matrix1.length][matrix2[0].length];
        int i = 0;
        for(MultPartMatrix mpm: list) {
            res[i++] = mpm.getMult();
        }
        return res;
    }


}

class MultPartMatrix extends Thread {
    int[][] matrix1;
    int[][] matrix2;
    int numbLine;
    private int[] mult;

    public MultPartMatrix(int[][] matrix1, int[][] matrix2, int numbLine) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.numbLine = numbLine;
        mult = new int[matrix2[0].length];
    }

    @Override
    public void run() {
        if (matrix1[0].length != matrix2.length) {
            throw new MultMatrixException("length of first matrix must be equal high of second!");
        }
        for (int j = 0; j < matrix2[0].length; j++) {
            int q = 0;
            int r = 0;
            while (q < matrix1[numbLine].length) {
                r += matrix1[numbLine][q] * matrix2[q][j];
                q++;
            }
            mult[j] = r;
        }
    }

    public int[] getMult() {
        return mult;
    }
}

    class MultMatrixException extends RuntimeException {
        public MultMatrixException(String message) {
            super(message);
        }

        @Override
        public String getMessage() {
            return "MultMatrixException: " + super.getMessage();
        }
    }