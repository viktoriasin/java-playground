package ru.sinvic.simplealgorithms;

public class FindSecondLarges {
    public static void main(String[] args) {
        int[] arr = new int[] {1, 2, 3};


        System.out.println(findSecondLargest(null));           // null
        System.out.println(findSecondLargest(new int[]{}));     // null
        System.out.println(findSecondLargest(new int[]{5}));     // null
        System.out.println(findSecondLargest(new int[]{1,2}));  // 1
        System.out.println(findSecondLargest(new int[]{5,5,4})); // 4
        System.out.println(findSecondLargest(new int[]{Integer.MIN_VALUE, 1})); // 1
    }
    public static Integer findSecondLargest(int[] array) {
        if (array == null || array.length < 2) {
            return null;
        }

        int firstMax = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;

        for (int j : array) {
            if (j > firstMax) {
                secondMax = firstMax;
                firstMax = j;
            } else if (j > secondMax && j != firstMax) {
                secondMax = j;
            }
        }

        return secondMax;
    }
}
