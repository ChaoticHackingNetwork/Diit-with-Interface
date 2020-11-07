// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.util;

import java.util.Stack;
import java.util.Random;

public class QuickSort
{
    private int mUseInsertion;
    private int mPivotType;
    private Random mNumGen;
    public static final int RANDOM_PIVOT = 0;
    public static final int MEDIAN_PIVOT = 1;
    public static final int LAST_ELEM_PIVOT = 2;
    
    public QuickSort(final int pivot, final int useInsertionSort) {
        if (pivot != 0 && pivot != 1 && pivot != 2) {
            this.mPivotType = 0;
        }
        else {
            this.mPivotType = pivot;
        }
        this.mNumGen = new Random(System.currentTimeMillis());
        this.mUseInsertion = useInsertionSort;
    }
    
    public QuickSort() {
        this.mPivotType = 0;
        this.mNumGen = new Random(System.currentTimeMillis());
        this.mUseInsertion = 0;
    }
    
    public void rsort(final int[][] anarray, final int start, final int finish) {
        if (finish - start < 1 || anarray.length < 2) {
            return;
        }
        if (finish - start == 1) {
            if (anarray[0][start] > anarray[0][finish]) {
                for (int t = 0; t < anarray.length; ++t) {
                    final int temp = anarray[t][start];
                    anarray[t][start] = anarray[t][finish];
                    anarray[t][finish] = temp;
                }
            }
            return;
        }
        if (finish - start <= this.mUseInsertion) {
            this.insertionSort(anarray, start, finish);
            return;
        }
        final int pivpos = this.getPivotPosition(anarray[0], start, finish);
        if (pivpos != finish) {
            for (int t2 = 0; t2 < anarray.length; ++t2) {
                final int temp2 = anarray[t2][pivpos];
                anarray[t2][pivpos] = anarray[t2][finish];
                anarray[t2][finish] = temp2;
            }
        }
        final int pivValue = anarray[0][finish];
        int s1 = start;
        int s2 = finish - 1;
        while (s1 - s2 != 0) {
            if (anarray[0][s1] > pivValue) {
                for (int t3 = 0; t3 < anarray.length; ++t3) {
                    final int temp3 = anarray[t3][s1];
                    anarray[t3][s1] = anarray[t3][s2];
                    anarray[t3][s2] = temp3;
                }
                --s2;
            }
            else {
                ++s1;
            }
        }
        if (anarray[0][s2] <= pivValue) {
            ++s2;
        }
        for (int t3 = 0; t3 < anarray.length; ++t3) {
            final int temp3 = anarray[t3][s2];
            anarray[t3][s2] = anarray[t3][finish];
            anarray[t3][finish] = temp3;
        }
        if (s2 == start) {
            this.rsort(anarray, start + 1, finish);
        }
        else if (s2 == finish) {
            this.rsort(anarray, start, finish - 1);
        }
        else {
            this.rsort(anarray, start, s2 - 1);
            this.rsort(anarray, s2 + 1, finish);
        }
    }
    
    public static void printArray(final int[][] sarray) {
        for (int t = 0; t < sarray.length; ++t) {
            System.out.print("Array = { ");
            for (int i = 0; i < sarray[t].length; ++i) {
                System.out.print(String.valueOf(sarray[t][i]) + " ");
            }
            System.out.println("}");
        }
    }
    
    private int getPivotPosition(final int[] anarray, final int start, final int finish) {
        if (this.mPivotType == 0) {
            return this.mNumGen.nextInt(finish - start) + start;
        }
        if (this.mPivotType != 1) {
            return finish;
        }
        final int middle = (finish - start) / 2 + start;
        final int max = getMedian(anarray[start], anarray[middle], anarray[finish]);
        if (max == 0) {
            return start;
        }
        if (max == 1) {
            return middle;
        }
        return finish;
    }
    
    private static int getMedian(final int a, final int b, final int c) {
        if ((a >= b && a <= c) || (a <= b && a >= c)) {
            return 0;
        }
        if ((b >= c && b <= a) || (b <= c && b >= a)) {
            return 1;
        }
        return 2;
    }
    
    private void insertionSort(final int[][] sarray, final int start, final int finish) {
        if (finish - start <= 1) {
            return;
        }
        for (int i = start + 1; i <= finish; ++i) {
            if (sarray[0][i] < sarray[0][i - 1]) {
                int j;
                for (j = start; sarray[0][i] > sarray[0][j]; ++j) {}
                for (int t = 0; t < sarray.length; ++t) {
                    final int insertable = sarray[t][i];
                    for (int k = i; k > j; --k) {
                        sarray[k] = sarray[k - 1];
                    }
                    sarray[t][j] = insertable;
                }
            }
        }
    }
    
    public void sort(final int[][] anarray, final int s, final int f) {
        final Stack<Position> stack = new Stack();
        try {
            stack.push(new Position(s, f));
            do {
                final Position pos = stack.pop();
                final int start = pos.getStart();
                final int finish = pos.getFinish();
                if (finish - start >= 1 && anarray.length >= 2) {
                    if (finish - start == 1) {
                        if (anarray[0][start] <= anarray[0][finish]) {
                            continue;
                        }
                        for (int t = 0; t < anarray.length; ++t) {
                            final int temp = anarray[t][start];
                            anarray[t][start] = anarray[t][finish];
                            anarray[t][finish] = temp;
                        }
                    }
                    else if (finish - start + 1 <= this.mUseInsertion) {
                        this.insertionSort(anarray, start, finish);
                    }
                    else {
                        final int pivpos = this.getPivotPosition(anarray[0], start, finish);
                        if (pivpos != finish) {
                            for (int t2 = 0; t2 < anarray.length; ++t2) {
                                final int temp2 = anarray[t2][pivpos];
                                anarray[t2][pivpos] = anarray[t2][finish];
                                anarray[t2][finish] = temp2;
                            }
                        }
                        final int pivValue = anarray[0][finish];
                        int s2 = start;
                        int s3 = finish - 1;
                        while (s2 - s3 != 0) {
                            if (anarray[0][s2] > pivValue) {
                                for (int t3 = 0; t3 < anarray.length; ++t3) {
                                    final int temp3 = anarray[t3][s2];
                                    anarray[t3][s2] = anarray[t3][s3];
                                    anarray[t3][s3] = temp3;
                                }
                                --s3;
                            }
                            else {
                                ++s2;
                            }
                        }
                        if (anarray[0][s3] <= pivValue) {
                            ++s3;
                        }
                        for (int t3 = 0; t3 < anarray.length; ++t3) {
                            final int temp3 = anarray[t3][s3];
                            anarray[t3][s3] = anarray[t3][finish];
                            anarray[t3][finish] = temp3;
                        }
                        stack.push(new Position(start, s3 - 1));
                        stack.push(new Position(s3 + 1, finish));
                    }
                }
            } while (!stack.empty());
        }
        catch (Exception e) {
            System.out.println("Pop attempted on empty stack!");
            System.exit(-1);
        }
    }
    
    private class Position
    {
        private int mStart;
        private int mFinish;
        
        public Position(final int s, final int f) {
            this.mStart = s;
            this.mFinish = f;
        }
        
        public int getStart() {
            return this.mStart;
        }
        
        public int getFinish() {
            return this.mFinish;
        }
    }
}
