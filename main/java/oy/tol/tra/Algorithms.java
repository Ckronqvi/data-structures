package oy.tol.tra;

import java.util.function.Predicate;

public class Algorithms {

    /**
     * Method sorts a generic array with bubble-sort.
     */
    public static <T extends Comparable<T>> void sort(T[] array) {

        boolean modifyed = true;
        while (modifyed) {
            modifyed = false;
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i].compareTo(array[i + 1]) > 0) {
                    swap(array, i, i + 1);
                    modifyed = true;
                }
            }
        }

    }

    /**
     * Reverses an array of objects.
     */
    public static <T> void reverse(T[] array) {
        int i = 0;
        while (i <= (array.length / 2) - 1) {
            swap(array, i, array.length - 1 - i);
            i++;
        }
    }

    /**
     * Swap the array element's places with each other.
     */
    public static <T> void swap(T[] array, int first, int second) {
        T temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    /**
     * Mode result. Contains the actual mode and also its count.
     */
    public static class ModeSearchResult<T> {
        public T theMode;
        public int count = 0;
    }

    /**
     * Finds the mode of given array and returns it
     */
    public static <T extends Comparable<T>> ModeSearchResult<T> findMode(T[] array) {
        ModeSearchResult<T> result = new ModeSearchResult<>();
        result.theMode = null;
        result.count = -1;
        if (null == array || array.length <= 1) {
            return result;
        }

        int n = array.length - 1;
        int i;
        int maxCount = 0, maxModeIndex = 0, maxTempCount = 0;
        int count = 1;
        boolean thisHappened = false;

        sort(array);

        // Finding the mode.
        for (i = 0; i < n; i++) {
            if (array[i].compareTo(array[i + 1]) == 0) {
                count++;
            } else {
                thisHappened = true;
                if (count == maxCount) {
                    maxTempCount = count;
                }
                if (count > maxCount) {
                    maxCount = count;
                    maxModeIndex = i;
                }
                count = 1;
            }
        }
        // If the last element happens to have the highest frequency.
        if (count > maxCount) {
            maxCount = count;
            maxModeIndex = n;
        }
        if (!thisHappened) {
            maxCount = count;
        }
        // checks that the maxModeIndex's count is unique.
        // If not, it will return the result that will be null.
        if (i == n && maxTempCount == maxCount) {
            return result;
        }
        result.theMode = array[maxModeIndex];
        result.count = maxCount;
        return result;
    }

    public static <T> int partitionByRule(T[] array, int count, Predicate<T> rule) {
        int i = 0;
        int index = 0;
        for (; i < count; i++) {
            if (rule.test(array[i])) {
                index = i;
                break;
            }
        }
        if (index >= count) {
            return count;
        }
        // start swapping from the index of the first rule breaker..
        i = index;
        while (i < count) {
            if (!rule.test(array[i])) {
                swap(array, i, index);
                index++;
                i++;
            } else {
                i++;
            }
        }
        return index;
    }

    /**
     * This is generic implemetation of the recurive quicksort algorithm.
     * It uses the middle value of the given range as a pivot.
     * 
     * @param <T>  Generic object that extends Comparable
     * @param arr  Array of objects that will be sorted
     * @param low  Lowest index of the given range. (At start, it will be 0).
     * @param high Highest index og the given range (At start, it will be
     *             arr.length-1).
     */
    public static <T extends Comparable<T>> void fastSort(T[] arr, int low, int high) {
        // T pivot = arr[low + (high - low) / 2];
        T pivot = arr[(low + high) >>> 1];
        int l = low, h = high;
        // This finds the first item on the left that is bigger than the pivot.
        while (l <= h) {
            while (arr[l].compareTo(pivot) < 0) {
                l++;
            }
            // This finds the first item on the right that is smaller than the pivot.
            while (arr[h].compareTo(pivot) > 0) {
                h--;
            }
            // swap their places.
            // Also l and h need to pass each other, hence the <= is used.
            if (l <= h) {
                swap(arr, l, h);
                l++;
                h--;
            }
        }
        // Continue on with the lower part of the array, gradually divinding the range.
        if (low < h) {
            fastSort(arr, low, h);
        }
        // Continue on with the upper part of the array, gradually divinding the range.
        if (l < high) {
            fastSort(arr, l, high);
        }
    }

    public static <T extends Comparable<T>> void fastSort(T[] arr) {
        fastSort(arr, 0, arr.length - 1);
    }

    /**
     * binarySearch that searches the index of a given element from an array.
     * 
     * @param <T>       General object that extends comparable
     * @param aValue    The value of which index we are looking for
     * @param fromArray Sorted array of object that contains the element
     * @param fromIndex Lower boundary of the array
     * @param toIndex   Upper boundary of the array
     * @return index of the element. Returns -1 if no such element exists.
     */
    public static <T extends Comparable<? super T>> int binarySearch(T aValue, T[] fromArray, int fromIndex,
            int toIndex) {
        int index = -1;
        if (toIndex < fromIndex) {
            return index;
        }
        int middle = ((toIndex + fromIndex) >>> 1); // unsigned bit shift to avoid
                                                    // overflow issues with big
                                                    // integers.

        if (fromArray[middle].compareTo(aValue) == 0) {
            return middle;
        }
        if (fromArray[middle].compareTo(aValue) > 0) {
            return binarySearch(aValue, fromArray, fromIndex, middle - 1);
        }
        if (fromArray[middle].compareTo(aValue) < 0) {
            return binarySearch(aValue, fromArray, middle + 1, toIndex);
        }
        return index;
    }

    /**
     * Return biggest prime number of given range using the sieve of Eratosthenes
     * 
     * @param n Maximum value of the prime number.
     * @return Prime number,
     */
    public static int findPrime(int n) {
        if (n <= 1) {
            return 1;
        }

        boolean prime[] = new boolean[n + 1];
        for (int i = 0; i < prime.length; i++) {
            prime[i] = true;
        }

        for (int p = 2; p * p <= n; p++) {
            if (prime[p]) {
                for (int i = p * 2; i <= n; i += p) {
                    prime[i] = false;
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (prime[i]) {
                return i;
            }
        }
        // If something were to heppen.
        return -1;
    }
}
