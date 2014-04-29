public class Solution {

    static final char ZERO_BIT = '0';
    static final char ONE_BIT = '1';

    public static void main(String args[]) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        // HALE those heroes that resolved it in 90 minutes. For me it took longer, and with second attempt
        final Solution solution = new Solution();
        System.out.println(solution.countOnBits(solution.readArraySize(), solution.readLine()));
    }

    String readLine() {
        final String inValue = System.console().readLine();
        if (inValue == null || inValue.trim().length() == 0) {
            throw new RuntimeException("An empty string is entered");
        }
        return inValue;
    }

    int readArraySize() {
        final String inValue = readLine();
        int result;
        try {
            result = Integer.parseInt(inValue);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Array size must be an integer: entered value: %s", inValue), e);
        }
        if (result < 1 || result > 100000) {
            throw new RuntimeException("0/1 arrays size must be between 1 and 100_000");
        }
        return result;
    }

    int countOnBits(int nSize, String inValue) {
        int count0 = 0;
        int count1 = 0;

        BitStatistic current = new BitStatistic();
        BitStatistic max0 = new BitStatistic();

        final char[] chars = inValue.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char digit = chars[i];
            switch (digit) {
                case ZERO_BIT:
                    if (current.isOne(chars)) {
                        current = new BitStatistic(i, 1, current);
                        if (max0.isEmpty()) {
                            max0 = current;
                        }
                    } else {
                        current.increment(i);
                    }
                    count0++;
                    break;
                case ONE_BIT:
                    if (current.isZero(chars)) {
                        if (current.count > max0.count) {
                            max0 = current;
                        }
                        current = new BitStatistic(i, 1, current);
                    } else {
                        current.increment(i);
                    }
                    count1++;
                    break;
            }
        }
        if (current.isZero(chars) && current.count > max0.count) {
            max0 = current;
        }
        final int nDigits = count0 + count1;
        if (nDigits < nSize) {
            throw new RuntimeException(String.format("Entered 0/1 array size: %d doesn't correspond entered array size: %d", nDigits, nSize));
        }

        int result;
        if (nDigits == count1) {
            result = 0;
        } else if (nDigits == count0) {
            result = nDigits;
        } else {
            result = max0.swapAndCount(count1, chars);
        }
        return result;
    }

    private static class BitStatistic {
        int index;
        int count;

        BitStatistic next;
        BitStatistic previous;

        BitStatistic() {
        }

        BitStatistic(int index, int count, BitStatistic previous) {
            this.index = index;
            this.count = count;
            this.previous = previous;
            previous.next = this;
        }

        int count(int count1, char[] chars) {
            return count1 + ((isZero(chars)) ? count : -count);
        }

        int swapAndCount(int count1, char[] chars) {
            int result = count(count1, chars);
            if (next != null) {
                result = Math.max(result, next.swapAndCountNext(result, chars));
            }
            if (previous != null) {
                result = Math.max(result, previous.swapAndCountPrevious(result, chars));
            }
            return result;
        }

        int swapAndCountNext(int count1, char[] chars) {
            if(next == null && isOne(chars)) {
                return count1;
            }
            int result = count(count1, chars);
            if (next != null) {
                result = next.swapAndCountNext(result, chars);
            }
            return result;
        }

        int swapAndCountPrevious(int count1, char[] chars) {
            if(previous == null && isOne(chars)) {
                return count1;
            }
            int result = count(count1, chars);
            if (previous != null) {
                result = previous.swapAndCountPrevious(result, chars);
            }
            return result;
        }

        boolean isZero(char[] chars) {
            return ZERO_BIT == chars[index];
        }

        boolean isOne(char[] chars) {
            return ONE_BIT == chars[index];
        }

        boolean isEmpty() {
            return count == 0;
        }

        void increment(int currentIdx) {
            if (count == 0 && index == 0) {
                index = currentIdx;
            }
            count++;
        }

    }

}
/*
You are given an array of size N elements: d[0], d[1], ... d[N - 1] where each d[i] is either 0 or 1. You can perform at most one move on the array: choose any two integers [L, R], and flip all the elements between (and including) the Lth and Rth bits. L and R represent the left-most and right-most index of the bits marking the boundaries of the segment which you have decided to flip.

What is the maximum number of '1'-bits (indicated by S) which you can obtain in the final bit-string? 'Flipping' a bit means, that a 0 is transformed to a 1 and a 1 is transformed to a 0.

Input Format:
A single integer N
The next line contains the N elements in the array separated by a space: d[0] d[1] ... d[N - 1]

Output format:
Output a single integer that denotes the maximum number of 1-bits which can be obtained in the final bit string

Constraints:
1 <= N <= 100000
d[i] can only be 0 or 1
0 <= L <= R < n

Sample Input:
8
1 0 0 1 0 0 1 0

Sample Output:
6

Explanation:
We can get a maximum of 6 ones in the given binary array by performing either of the following operations:
Flip [1, 5] ==> 1 1 1 0 1 1 1 0
or
Flip [1, 7] ==> 1 1 1 0 1 1 0 1

* */