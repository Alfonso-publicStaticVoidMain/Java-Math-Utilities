package mainpackage;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class CalcUtil {
    /*
    * This is my CalcUtil class, designed as a repository of methods
    * to be applied to many Java classes like Strings, arrays, Lists, as well
    * as classes created by me like RealMatrix and RealPolynomial.
    *
    * Each method includes a description of what it does and if needed, a brief
    * explanation of the mathematical functioning of that method. For more complex
    * methods, the arguments are displayed vertically to allow for better
    * explanations of what they are and their function within the method.
    */
    
    
    /*
    * A tridimensional array of doubles to be used in the derivatives section to
    * to estimate the derivative of a function using the finite difference mehtod.
    * The first coordinate of the array stores the order of the derivative, the
    * second stores the accuracy of the approximation, and the third stores the
    * specific coefficient to be used by the method. Keep in mind that the middle
    * value is the one that multiplies f(x), while the next one multiplies f(x+h)
    * and the previous one f(x-h), so some care has to be taken when accesing
    * those indexes and doing the calculations.
    */
    private static final double[][][] finiteDifferenceCoef = new double[][][] {
        { // Derivative of oder 0 (the same function)
            {1}
        },
        { // Derivate of order 1
            {-1d/2, 0, 1d/2},                                                   // accuracy 2
            {1d/12, -2d/3, 0, 2d/3, -1d/12},                                    // accuracy 4
            {-1d/60, 3d/20, -3d/4, 0, 3d/4, -3d/20, 1d/60},                     // accuracy 6
            {1d/280, -4d/105, 1d/5, -4d/5, 0, 4d/5, -1d/5, 4d/105, -1d/280}     // accuracy 8
        },
        { // Derivative of order 2
            {1, -2, 1},
            {-1d/12, 4d/3, -5d/2, 4d/3, -1d/12},
            {1d/90, -3d/20, 3d/2, -49d/18, 3d/2, -3d/20, 1d/90},
            {-1d/560, 8d/315, -1d/5, 8d/5, -205d/72, 8d/5, -1d/5, 8d/315, -1d/560}
        },
        { // Derivative of order 3
            {-1d/2, 1, 0, -1, 1d/2},
            {1d/8, -1, 13d/8, 0, -13d/8, 1, -1d/8},
            {-7d/240, 3d/10, -169d/120, 61d/30, 0, -61d/30, 169d/120, -3d/10, 7d/240}
        },
        { // Derivative of order 4
            {1, -4, 6, -4, 1},
            {-1d/6, 2, -13d/2, 28d/3, -13d/2, 2, -1d/6},
            {7d/240, -2d/5, 169d/60, -122d/15, 91d/8, -122d/15, 169d/60, -2d/5, 7d/240}
        },
        { // Derivative of order 5
            {-1d/2, 2, -5d/2, 0, 5d/2, -2, 1d/2},
            {1d/6, -3d/2, 13d/3, -29d/6, 0, 29d/6, -13d/3, 3d/2, -1d/6},
            {-13d/288, 19d/36, -87d/32, 13d/2, -323d/48, 0, 323d/48, -13d/2, 87d/32, -19d/36, 13d/288}
        },
        { // Derivative of order 6
            {1, -6, 15, -20, 15, -6, 1},
            {-1d/4, 3, -13, 29, -75d/2, 29, -13, 3, -1d/4},
            {13d/240, -19d/24, 87d/16, -39d/2, 323d/8, -1023d/20, 323d/8, -39d/2, 87d/16, -19d/24, 13d/240}
        }
    };
    
    // <editor-fold defaultstate="collapsed" desc="String methods">
    /*
    * Contains a small number of methods dealing with Strings and some basic
    * functionalities that you may need when dealing with them.
    *
    * Contains the following methods:
    *   countChar: Counts the number of appearences of a character within a String.
    *   generatePassword: Randomly generated a password drawing from a String of avaliable characters, with or without repetition.
    *   shuffle: Randomly reorders the characters within a String.
    *   isInteger: Determines if a String represents an Integer in a given base.
    *   stringToIntegerList: Returns a List of Integers that contains all the digits of the String passed.
    */
    
    // Counts the number of appearences of a character within a String.
    public static int countChar(String str, char ch) {return str.length() - str.replace(Character.toString(ch), "").length();}
    
    /*
    * Generates a password String randomly selecting from a String of avaliable 
    * characters, with or without repetition, as desired.
    *
    * Will throw an error if you enter a length greater than the length
    * of the avaliable chararacters String without allowing repetition
    */
    public static String generatePassword(String avaliableChars, int len, boolean allowRepetition) {
        StringBuilder copyAvaliableChars = new StringBuilder(avaliableChars);
        String password = "";
        Random r = new Random();
        for (int i = 0; i < len; i++) {
            int randomNumber = r.nextInt(avaliableChars.length());
            password = password + copyAvaliableChars.charAt(randomNumber);
            if (!allowRepetition) copyAvaliableChars.deleteCharAt(randomNumber);
        }
        return password;
    }
    
    // Returns a random reordering of the given String.
    public static String shuffle(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        String result = "";
        Random r = new Random();
        while (strBuilder.length() != 0) {
            int randomNumber = r.nextInt(strBuilder.length());
            result = result + strBuilder.charAt(randomNumber);
            strBuilder.deleteCharAt(randomNumber);
        }
        return result;
    }
    
    /*
    * Return true if the String is an integer in the given base, false otherwise.
    * If the base is unspecified, it's assumed 10.
    */
    public static boolean isInteger(String str, int base) {
        try {
            Integer.parseInt(str, base);
            return true;
        } catch (Exception e) {return false;}
    }
    public static boolean isInteger(String str) {return isInteger(str, 10);}

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Array Index methods">
    /*
    * Contains a number of methods that deal with index positions within an array,
    * finding the indexes of elements, the number of appearences, and deleting duplicates.
    *
    * Contains the following methods, implemented both for arrays/Lists of
    * generic T classes, int or double:
    *   onlyIndexCondition: Returns -2 if no element of the array/List satisfies a given condition, -1 if more than one does, or n >= 0 if n is the index of the only element that does.
    *   firstIndexOf: Returns the index of the first appearence of an element in an array, -1 if it doesn't appear.
    *   lastIndexOf: Returns the index of the last appearence of an element in an array, -1 if it doesn't appear.
    *   allIndexesOf: Returns an integer array with the indexes of all apearences of an element in an array.
    *   firstIndexThatSatisfiesCondition: Returns the index of the first appearence of an element that satisfies a condition.
    *   lastIndexThatSatisfiesCondition: Returns the index of the last appearence of an element that satisfies a condition.
    *   appearsExactlyOnce: Returns true if an element appears exactly once in an array, false otherwise.
    *   appearsMoreThanOnce: Returns true if an element appears more than once in an array, false otherwise.
    *   appearsZeroTimes: Returns true if an element appears exactly zero times in an array, false otherwise.
    *   contains: Returns true if an element appears one or more times in an array, false otherwise.
    *   deleteLastDuplicates: Deletes all but the first appearence of each element of an array.
    *   deleteFirstDuplicates: Deletes all but the last appearence of each element of an array.
    */
    
    
    /*
    * This following method will return:
    *    -2 if no element satisfies the condition.
    *    -1 if more than one element satisfies the condition.
    *    n >= 0 if exactly one element satisfies the condition, with index n.
    */
    public static <T> int onlyIndexCondition(T[] targetArray, Function<T, Boolean> condition) {
        int index = -2;
        boolean found = false;
        for (int i = 0; i < targetArray.length; i++) {
            if (!found && condition.apply(targetArray[i])) {
                index = i;
                found = true;
            } else if (found && condition.apply(targetArray[i])) index = -1;
            if (found && index == -1) break;
        }
        return index;
    }
    public static <T> int onlyIndexCondition(List<T> targetArray, Function<T, Boolean> condition) {
        int index = -2;
        boolean found = false;
        for (int i = 0; i < targetArray.size(); i++) {
            if (!found && condition.apply(targetArray.get(i))) {
                index = i;
                found = true;
            } else if (found && condition.apply(targetArray.get(i))) index = -1;
            if (found && index == -1) break;
        }
        return index;
    }  
    public static int onlyIndexCondition(double[] doubleArray, Function<Double, Boolean> condition) {
        int index = -2;
        boolean found = false;
        for (int i = 0; i < doubleArray.length; i++) {
            if (!found && condition.apply(doubleArray[i])) {
                index = i;
                found = true;
            } else if (found && condition.apply(doubleArray[i])) index = -1;
            if (found && index == -1) break;
        }
        return index;
    }
    public static int onlyIndexCondition(int[] intArray, Function<Integer, Boolean> condition) {
        int index = -2;
        boolean found = false;
        for (int i = 0; i < intArray.length; i++) {
            if (!found && condition.apply(intArray[i])) {
                index = i;
                found = true;
            } else if (found && index != -1 && condition.apply(intArray[i])) index = -1;
            if (found && index == -1) break;
        }
        return index;
    }
    
    /*
    * Returns the index of the first appearence of an element in an array,
    * or -1 if it isn't present.
    */
    public static <T> int firstIndexOf(T[] targetArray, T t) {
        for (int i = 0; i < targetArray.length; i++) if (targetArray[i].equals(t)) return i;
        return -1;
    }
    public static int firstIndexOf(double[] doubleArray, double d) {
        for (int i = 0; i < doubleArray.length; i++) if (doubleArray[i] == d) return i;
        return -1;
    }
    public static int firstIndexOf(int[] intArray, int n) {
        for (int i = 0; i < intArray.length; i++) if (intArray[i] == n) return i;
        return -1;
    }
    
    // Returns the index of the last appearence of an element in an array, or -1 if it isn't present.
    public static <T> int lastIndexOf(T[] targetArray, T tElement) {
        for (int i = targetArray.length - 1; i >= 0; i--) if (targetArray[i].equals(tElement)) return i;
        return -1;
    }
    public static int lastIndexOf(double[] doubleArray, double d) {
        for (int i = doubleArray.length - 1; i >= 0; i--) if (doubleArray[i] == d) return i;
        return -1;
    }
    public static int lastIndexOf(int[] intArray, int n) {
        for (int i = intArray.length - 1; i >= 0; i--) if (intArray[i] == n) return i;
        return -1;
    }
    
    /*
    * Returns an int[] array of all the indexes where a possible element of an
    * array appears, or an empty array if it isn't in the array.
    */
    public static <T> int[] allIndexesOf(T[] targetArray, T t) {
        List<Integer> listOfIndexes = new LinkedList<>();
        for (int i = 0; i < targetArray.length; i++) if (targetArray[i].equals(t)) listOfIndexes.add(i);
        return intListToIntArray(listOfIndexes);
    }
    public static int[] allIndexesOf(double[] doubleArray, double d) {
        List<Integer> listOfIndexes = new LinkedList<>();
        for (int i = 0; i < doubleArray.length; i++) if (doubleArray[i] == d) listOfIndexes.add(i);
        return intListToIntArray(listOfIndexes);
    }
    public static int[] allIndexesOf(int[] intArray, int n) {
        List<Integer> listOfIndexes = new LinkedList<>();
        for (int i = 0; i < intArray.length; i++) if (intArray[i] == n) listOfIndexes.add(i);
        return intListToIntArray(listOfIndexes);
    }
    
    /*
    * Returns the index of the first element of an array that satisfies the
    * given condition, or -1 if no element does.
    */
    public static <T> int firstIndexThatSatisfiesCondition(T[] targetArray, Function<T, Boolean> condition) {
        for (int i = 0; i < targetArray.length; i++) if (condition.apply(targetArray[i])) return i;
        return -1;
    }
    public static int firstIndexThatSatisfiesCondition(List<Integer> targetArray, Function<Integer, Boolean> condition) {
        for (int i = 0; i < targetArray.size(); i++) if (condition.apply(targetArray.get(i))) return i;
        return -1;
    }
    public static int firstIndexThatSatisfiesCondition(double[] doubleArray, Function<Double, Boolean> condition) {
        for (int i = 0; i < doubleArray.length; i++) if (condition.apply(doubleArray[i])) return i;
        return -1;
    }
    public static int firstIndexThatSatisfiesCondition(int[] intArray, Function<Integer, Boolean> condition) {
        for (int i = 0; i < intArray.length; i++) if (condition.apply(intArray[i])) return i;
        return -1;
    }
    
    /*
    * Returns the index of the last element of an array that satisfies the
    * given condition, or -1 if no element does.
    */
    public static <T> int lastIndexThatSatisfiesCondition(T[] targetArray, Function<T, Boolean> condition) {
        for (int i = targetArray.length - 1; i >= 0; i--) if (condition.apply(targetArray[i])) return i;
        return -1;
    }
    public static <T> int lastIndexThatSatisfiesCondition(List<T> targetArray, Function<T, Boolean> condition) {
        for (int i = targetArray.size() - 1; i >= 0; i--) if (condition.apply(targetArray.get(i))) return i;
        return -1;
    }
    public static int lastIndexThatSatisfiesCondition(double[] doubleArray, Function<Double, Boolean> condition) {
        for (int i = doubleArray.length - 1; i >= 0; i--) if (condition.apply(doubleArray[i])) return i;
        return -1;
    }
    public static int lastIndexThatSatisfiesCondition(int[] intArray, Function<Integer, Boolean> condition) {
        for (int i = intArray.length - 1; i >= 0; i--) if (condition.apply(intArray[i])) return i;
        return -1;
    }
    
    // Returns true if the element appears exactly once in the array, false otherwise.
    public static <T> boolean appearsExactlyOnce(T[] targetArray, T t) {return firstIndexOf(targetArray, t) != -1 && firstIndexOf(targetArray, t) == lastIndexOf(targetArray, t);}
    public static boolean appearsExactlyOnce(double[] doubleArray, double d) {return firstIndexOf(doubleArray, d) != -1 && firstIndexOf(doubleArray, d) == lastIndexOf(doubleArray, d);}
    public static boolean appearsExactlyOnce(int[] intArray, int n) {return firstIndexOf(intArray, n) != -1 && firstIndexOf(intArray, n) == lastIndexOf(intArray, n);}
    
    // Returns true if the element appears more than once in the array, false otherwise.
    public static <T> boolean appearsMoreThanOnce(T[] targetArray, T t) {return firstIndexOf(targetArray, t) != -1 && firstIndexOf(targetArray, t) != lastIndexOf(targetArray, t);}
    public static boolean appearsMoreThanOnce(double[] doubleArray, double d) {return firstIndexOf(doubleArray, d) != -1 && firstIndexOf(doubleArray, d) != lastIndexOf(doubleArray, d);}
    public static boolean appearsMoreThanOnce(int[] intArray, int n) {return firstIndexOf(intArray, n) != -1 && firstIndexOf(intArray, n) != lastIndexOf(intArray, n);}
    
    // Return true if the element doesn't appear in the array, false otherwise.
    public static <T> boolean appearsZeroTimes(T[] targetArray, T t) {return firstIndexOf(targetArray, t) == -1;}
    public static boolean appearsZeroTimes(double[] doubleArray, double d) {return firstIndexOf(doubleArray, d) == -1;}
    public static boolean appearsZeroTimes(int[] intArray, int n) {return firstIndexOf(intArray, n) == -1;}
    
    // Returns true if the array contains the passed element, false otherwise.
    public static <T> boolean contains(T[] targetArray, T tElement) {
        for (T t : targetArray) if (t.equals(tElement)) return true;
        return false;
    }
    public static boolean contains(double[] doubleArray, double d) {
        for (double t : doubleArray) if (t == d) return true;
        return false;
    }
    public static boolean contains(int[] intArray, int n) {
        for (int t : intArray) if (t == n) return true;
        return false;
    }
    
    // Deletes all but the first appearence of duplicate elements in an array.
    public static <T> List<T> deleteLastDuplicates(T[] targetArray) {
        List<T> result = new ArrayList(Arrays.asList(targetArray[0]));
        for (int i = 1; i < targetArray.length; i++) {
            boolean foundDuplicate = false;
            for (int j = 0; j < result.size(); j++) {
                if (targetArray[i].equals(result.get(j))) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (!foundDuplicate) result.add(targetArray[i]);
        }
        return result;
    }
    public static List<Double> deleteLastDuplicates(double[] doubleArray) {
        List<Double> result = new ArrayList(Arrays.asList(doubleArray[0]));
        for (int i = 1; i < doubleArray.length; i++) {
            boolean foundDuplicate = false;
            for (int j = 0; j < result.size(); j++) {
                if (doubleArray[i] == result.get(j)) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (!foundDuplicate) result.add(doubleArray[i]);
        }
        return result;
    }
    public static List<Integer> deleteLastDuplicates(int[] intArray) {
        List<Integer> result = new ArrayList(Arrays.asList(intArray[0]));
        for (int i = 1; i < intArray.length; i++) {
            boolean foundDuplicate = false;
            for (int j = 0; j < result.size(); j++) {
                if (intArray[i] == result.get(j)) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (!foundDuplicate) result.add(intArray[i]);
        }
        return result;
    }
    
    // Deletes all but the last appearence of duplicate elements in an array.
    public static <T> List<T> deleteFirstDuplicates(T[] targetArray) {
        List<T> result = new ArrayList(Arrays.asList(targetArray[targetArray.length - 1]));
        for (int i = targetArray.length - 2; i >= 0 ; i--) {
            boolean foundDuplicate = false;
            for (int j = 0; j < result.size(); j++) {
                if (targetArray[i].equals(result.get(j))) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (!foundDuplicate) result.add(targetArray[i]);
        }
        return result.reversed();
    }
    public static List<Double> deleteFirstDuplicates(double[] doubleArray) {
        List<Double> result = new ArrayList(Arrays.asList(doubleArray[doubleArray.length - 1]));
        for (int i = doubleArray.length - 2; i >= 0 ; i--) {
            boolean foundDuplicate = false;
            for (int j = 0; j < result.size(); j++) {
                if (doubleArray[i] == result.get(j)) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (!foundDuplicate) result.add(doubleArray[i]);
        }
        return result.reversed();
    }
    public static List<Integer> deleteFirstDuplicates(int[] intArray) {
        List<Integer> result = new ArrayList(Arrays.asList(intArray[intArray.length - 1]));
        for (int i = intArray.length - 2; i >= 0 ; i--) {
            boolean foundDuplicate = false;
            for (int j = 0; j < result.size(); j++) {
                if (intArray[i] == result.get(j)) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (!foundDuplicate) result.add(intArray[i]);
        }
        return result.reversed();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Array Condition methods">
    /*
    * Contains a number of methods that aim to describe or modify arrays and/or
    * Lists based on a condition that their elements and their indexes can
    * satisfy or not.
    *
    * Methods about applying a given function to the elements that satisfy a
    * condition are missing, since they are included in the Array Function Methods
    * section.
    *
    * When I say "condition" I mean a function from a certain class T to the
    * Boolean class, or from the cartesian product of T x Integer to Boolean
    * if it also takes into account the position of the element within the
    * array/List.
    *
    * This way, an element t of class T satisfies the condition if
    * condition.apply(t) is true, and doesn't satisfy it if it's false.
    *
    * Contains the following methods, implemented both for arrays/Lists of
    * generic T classes, int or double:
    *   countCondition: Counts the number of elements that satisfy a condition.
    *   deleteExteriorIfCondition: Deletes elements from the beginning and/or end until it finds an element that doesn't satisfy a given condition.
    *   moveElementsIfCondition: Moves all elements that satisfy a condition to the beginning or end, preserving their order.
    */
    
    /*
    * Counts the number of elements that satisfy a given condition on their
    * value and position.
    */
    public static <T> int countCondition(
        T[] targetArray,
        BiFunction<T, Integer, Boolean> condition
    ) {
        int counter = 0;
        for (int i = 0; i < targetArray.length; i++) if (condition.apply(targetArray[i], i)) counter++;
        return counter;
    }
    public static <T> int countCondition(
        List<T> targetArray,
        BiFunction<T, Integer, Boolean> condition
    ) {
        int counter = 0;
        for (int i = 0; i < targetArray.size(); i++) if (condition.apply(targetArray.get(i), i)) counter++;
        return counter;
    }
    public static int countCondition(
        double[] doubleArray,
        BiFunction<Double, Integer, Boolean> condition
    ) {
        int counter = 0;
        for (int i = 0; i < doubleArray.length; i++) if (condition.apply(doubleArray[i], i)) counter++;
        return counter;
    }
    public static int countCondition(
        int[] intArray,
        BiFunction<Integer, Integer, Boolean> condition
    ) {
        int counter = 0;
        for (int i = 0; i < intArray.length; i++) if (condition.apply(intArray[i], i)) counter++;
        return counter;
    }
    public static int countCondition(boolean[] booleanArray, BiFunction<Boolean, Integer, Boolean> condition) {
        int counter = 0;
        for (int i = 0; i < booleanArray.length; i++) if (condition.apply(booleanArray[i], i)) counter++;
        return counter;
    }
    
    /*
    * Counts the number of elements that satisfy a given condition on
    * just their value.
    */
    public static <T> int countCondition(T[] targetArray, Predicate<T> condition) {return countCondition(targetArray, (n, i) -> condition.test(n));}
    public static <T> int countCondition(List<T> targetArray, Predicate<T> condition) {return countCondition(targetArray, (n, i) -> condition.test(n));}
    public static int countCondition(double[] doubleArray, Predicate<Double> condition) {return countCondition(doubleArray, (n, i) -> condition.test(n));}
    public static int countCondition(int[] intArray, Predicate<Integer> condition) {return countCondition(intArray, (n, i) -> condition.test(n));}
    public static int countCondition(boolean[] booleanArray, Predicate<Boolean> condition) {return countCondition(booleanArray, (n, i) -> condition.test(n));}
    
    // Deletes elements from the beginning and/or end, if they satisfy the condition.
    public static <T> T[] deleteExteriorIfCondition(
        T[] targetArray,                // Array to delete elements from.
        Predicate<T> condition,         // Condition that needs to be satisfied by the elements deleted.
        boolean deleteFromBeginning,    // Set to true if you want to delete from the beginning.
        boolean deleteFromEnd           // Set to true if you want to delete form the end.
    ) {
        int len = targetArray.length;
        int initIndex = 0, finalIndex = len;
        boolean initFound = false, finalFound = false;
        for (int i = 0; i < len; i++) {
            if (!initFound && deleteFromBeginning && !condition.test(targetArray[i])) {
                initIndex = i;
                initFound = true;
            }
            if (!finalFound && deleteFromEnd && !condition.test(targetArray[len - 1 - i])) {
                finalIndex = len - i;
                finalFound = true;
            }
            if (initFound && finalFound ||
            initFound && deleteFromBeginning && !deleteFromEnd ||
            finalFound && deleteFromEnd && !deleteFromBeginning) break;
        }
        if (initIndex == 0 && finalIndex == len) return (T[]) Arrays.asList().toArray();
        return Arrays.copyOfRange(targetArray, initIndex, finalIndex);
    }
    public static <T> List<T> deleteExteriorIfCondition(
        List<T> targetArray,            // List to delete elements from.
        Predicate<T> condition,         // Condition that needs to be satisfied by the elements deleted.
        boolean deleteFromBeginning,    // Set to true if you want to delete from the beginning.
        boolean deleteFromEnd           // Set to true if you want to delete form the end.
    ) {
        int len = targetArray.size();
        int initIndex = 0, finalIndex = len;
        boolean initFound = false, finalFound = false;
        for (int i = 0; i < len; i++) {
            if (!initFound && deleteFromBeginning && !condition.test(targetArray.get(i))) {
                initIndex = i;
                initFound = true;
            }
            if (!finalFound && deleteFromEnd && !condition.test(targetArray.get(len - 1 - i))) {
                finalIndex = len - i;
                finalFound = true;
            }
            if (initFound && finalFound ||
            initFound && deleteFromBeginning && !deleteFromEnd ||
            finalFound && deleteFromEnd && !deleteFromBeginning) break;
        }
        if (initIndex == 0 && finalIndex == len) return Arrays.asList();
        return targetArray.subList(initIndex, finalIndex);
    }
    public static double[] deleteExteriorIfCondition(
        double[] doubleArray,
        Predicate<Double> condition,         
        boolean deleteFromBeginning,
        boolean deleteFromEnd
    ) {
        int len = doubleArray.length;
        int initIndex = 0, finalIndex = len;
        boolean initFound = false, finalFound = false;
        for (int i = 0; i < len; i++) {
            if (!initFound && deleteFromBeginning && !condition.test(doubleArray[i])) {
                initIndex = i;
                initFound = true;
            }
            if (!finalFound && deleteFromEnd && !condition.test(doubleArray[len - 1 - i])) {
                finalIndex = len - i;
                finalFound = true;
            }
            if (initFound && finalFound) break;
        }
        if (initIndex == 0 && finalIndex == len) return new double[0];
        return Arrays.copyOfRange(doubleArray, initIndex, finalIndex);
    }
    public static int[] deleteExteriorIfCondition(
        int[] intArray,
        Predicate<Integer> condition,         
        boolean deleteFromBeginning,
        boolean deleteFromEnd
    ) {
        int len = intArray.length;
        int initIndex = 0, finalIndex = len;
        boolean initFound = false, finalFound = false;
        for (int i = 0; i < len; i++) {
            if (!initFound && deleteFromBeginning && !condition.test(intArray[i])) {
                initIndex = i;
                initFound = true;
            }
            if (!finalFound && deleteFromEnd && !condition.test(intArray[len - 1 - i])) {
                finalIndex = len - i;
                finalFound = true;
            }
            if (initFound && finalFound) break;
        }
        if (initIndex == 0 && finalIndex == len) return new int[0];
        return Arrays.copyOfRange(intArray, initIndex, finalIndex);
    }
    
    // Moves all elements of the array that satisfy the condition to the beginning or to the end of it.
    public static <T> List<T> moveElementsIfCondition(
        T[] targetArray,
        Predicate<T> condition,         
        boolean moveToBeginning
    ) {
        List<T> result = new LinkedList<>();
        int index = 0;
        for (T t : targetArray) {
            if (moveToBeginning) {
                if (condition.test(t)) {
                    result.add(index, t);
                    index++;
                } else result.add(t);
            } else {
                if (condition.test(t)) result.add(t);
                else {
                    result.add(index, t);
                    index++;
                }
            }
        }
        return result;
    }
    public static <T> List<T> moveElementsIfCondition(
        List<T> targetArray,
        Predicate<T> condition,         
        boolean moveToBeginning
    ) {
        List<T> result = new LinkedList<>();
        int index = 0;
        for (T t : targetArray) {
            if (moveToBeginning) {
                if (condition.test(t)) {
                    result.add(index, t);
                    index++;
                } else result.add(t);
            } else {
                if (condition.test(t)) result.add(t);
                else {
                    result.add(index, t);
                    index++;
                }
            }
        }
        return result;
    }
    public static List<Double> moveElementsIfCondition(
        double[] targetArray,
        Predicate<Double> condition,         
        boolean moveToBeginning
    ) {
        List<Double> result = new LinkedList<>();
        int index = 0;
        for (double t : targetArray) {
            if (moveToBeginning) {
                if (condition.test(t)) {
                    result.add(index, t);
                    index++;
                } else result.add(t);
            } else {
                if (condition.test(t)) result.add(t);
                else {
                    result.add(index, t);
                    index++;
                }
            }
        }
        return result;
    }
    public static List<Integer> moveElementsIfCondition(
        int[] targetArray,
        Predicate<Integer> condition,         
        boolean moveToBeginning
    ) {
        List<Integer> result = new LinkedList<>();
        int index = 0;
        for (int t : targetArray) {
            if (moveToBeginning) {
                if (condition.test(t)) {
                    result.add(index, t);
                    index++;
                } else result.add(t);
            } else {
                if (condition.test(t)) result.add(t);
                else {
                    result.add(index, t);
                    index++;
                }
            }
        }
        return result;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Array Function methods">
    /*
    * Contains a number of methods dealing with applying functions to arrays/Lists,
    * a function being a map f: T -> U from one class T to another U, or a map
    * f: T x Integer -> U if it takes into account the position of an element
    * within the array/List.
    *
    * Contains the following methods, including their implementation for
    * arrays of generic T classes, int or double.
    *   applyFunction: Applies a function to all elements of an array/List
    *   applyFunctionIfCondition: Applies a function to all elements of an array/List that satisfy a condition
    *   concatenatedOperator: Applies an operator consecutively to all elements of an array/List
    */

    
    /*
    * Applies a function f: T -> U to a List or array of class T,
    * returning a List of class U. Includes variants of both functions and
    * bifunctions, that are meant to be applied to the pair (t, i), where
    * i is the index of the element t.
    */
    
    /* TO DO: Reimplement the applyFunction methods using streams. Examples:
    
    public static <T, U> List<U> applyFunction(List<T> t, Function<T, U> f) {
        return t.stream()
            .map(f)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public static <T, U> List<U> applyFunction(List<T> t, BiFunction<T, Integer, U> f) {
        return IntStream.range(0, t.size())
           .mapToObj(i -> f.apply(t.get(i), i))
           .collect(Collectors.toCollection(ArrayList::new));
    }
    
    */
    public static <T, U> List<U> applyFunction(
        List<T> targetArray,            // List to apply the function to.
        BiFunction<T, Integer, U> f     // BiFunction to apply to the list.
    ) {
        return IntStream.range(0, targetArray.size())
           .mapToObj(i -> f.apply(targetArray.get(i), i))
           .collect(Collectors.toCollection(ArrayList::new));
    }
    public static <T, U> List<U> applyFunction(
        T[] targetArray,                // Array to apply the function to.
        BiFunction<T, Integer, U> f     // BiFunction to apply to the array.
    ) {
        return IntStream.range(0, targetArray.length)
           .mapToObj(i -> f.apply(targetArray[i], i))
           .collect(Collectors.toCollection(ArrayList::new));
    }
    public static <T, U> List<U> applyFunction(
        List<T> targetArray,    // List to apply the function to.
        Function<T, U> f        // Function to apply to the list.
    ) {
        return targetArray.stream()
            .map(f)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    public static <T, U> List<U> applyFunction(
        T[] targetArray,    // Array to apply the function to.
        Function<T, U> f    // Function to apply to the array.
    ) {
        return Stream.of(targetArray)
            .map(f)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    public static double[] applyFunction(
        double[] doubleArray,
        BiFunction<Double, Integer, Double> f
    ) {
        return IntStream.range(0, doubleArray.length)
           .mapToDouble(i -> f.apply(doubleArray[i], i))
           .toArray();
//        double[] result = new double[doubleArray.length];
//        for (int i = 0; i < doubleArray.length; i++) result[i] = f.apply(doubleArray[i], i);
//        return result;
    }
    public static double[] applyFunction(
        double[] doubleArray,
        DoubleUnaryOperator f
    ) {
        return DoubleStream.of(doubleArray)
           .map(f)
           .toArray();      
//        double[] result = new double[doubleArray.length];
//        for (int i = 0; i < doubleArray.length; i++) result[i] = f.apply(doubleArray[i]);
//        return result;
    }
    public static int[] applyFunction(
        int[] intArray,
        BiFunction<Integer, Integer, Integer> f
    ) {
        return IntStream.range(0, intArray.length)
           .map(i -> f.apply(intArray[i], i))
           .toArray();
//        int[] result = new int[intArray.length];
//        for (int i = 0; i < intArray.length; i++) result[i] = f.apply(intArray[i], i);
//        return result;
    }
    public static int[] applyFunction(
        int[] intArray,
        IntUnaryOperator f
    ) {
        return IntStream.of(intArray)
           .map(f)
           .toArray();
//        int[] result = new int[intArray.length];
//        for (int i = 0; i < intArray.length; i++) result[i] = f.apply(intArray[i]);
//        return result;
    }
    
    /*
    * Applies a function f: T -> U  to the elements of the array/List that
    * satisfy the condition on their value and index, or a condition on just
    * their value.
    */
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        T[] targetArray,                            // Array to apply the function to.
        BiFunction<T, Integer, Boolean> condition,  // BiFunction to apply to the array.
        BiFunction<T, Integer, U> f                 // Condition on element and index to satisfy for the function to be applied.
    ) {
        return applyFunction(targetArray, (x, i) -> condition.apply(x, i) ? f.apply(x, i) : x);
//        List<T> result = new LinkedList<>();
//        for (int i = 0; i < targetArray.length; i++) {
//            if (condition.apply(targetArray[i], i)) result.add(f.apply(targetArray[i], i));
//            else result.add(targetArray[i]);
//        }
//        return result;
    }
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        T[] targetArray,
        Predicate<T> condition,
        BiFunction<T, Integer, U> f
    ) {return applyFunction(targetArray, (x, i) -> condition.test(x) ? f.apply(x, i) : x);}
        //{return applyFunctionIfCondition(targetArray, (x, i) -> condition.apply(x), f);}
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        T[] targetArray,
        BiFunction<T, Integer, Boolean> condition,
        Function<T, U> f
    ) {return applyFunction(targetArray, (x, i) -> condition.apply(x, i) ? f.apply(x) : x);}
        //{return applyFunctionIfCondition(targetArray, condition, (x, i) -> f.apply(x));}
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        T[] targetArray,
        Predicate<T> condition,
        Function<T, U> f
    ) {return applyFunction(targetArray, (x, i) -> condition.test(x) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(targetArray, (x, i) -> condition.apply(x), (x, i) -> f.apply(x));}
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        List<T> targetArray,                        // List to apply the function to.
        BiFunction<T, Integer, Boolean> condition,  // BiFunction to apply to the List.
        BiFunction<T, Integer, U> f                 // Condition on element and index to satisfy for the function to be applied.
    ) {
        return applyFunction(targetArray, (x, i) -> condition.apply(x, i) ? f.apply(x, i) : x);
//        List<T> result = new LinkedList<>();
//        for (int i = 0; i < targetArray.size(); i++) if (condition.apply(targetArray.get(i), i)) result.add(f.apply(targetArray.get(i), i));
//        return result;
    }
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        List<T> targetArray,
        Predicate<T> condition,
        BiFunction<T, Integer, U> f
    ) {return applyFunction(targetArray, (x, i) -> condition.test(x) ? f.apply(x, i) : x);}
    //{return applyFunctionIfCondition(targetArray, (x, i) -> condition.apply(x), f);}
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        List<T> targetArray,
        BiFunction<T, Integer, Boolean> condition,
        Function<T, U> f
    ) {return applyFunction(targetArray, (x, i) -> condition.apply(x, i) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(targetArray, condition, (x, i) -> f.apply(x));}
    public static <T, U extends T> List<T> applyFunctionIfCondition(
        List<T> targetArray,
        Predicate<T> condition,
        Function<T, U> f
    ) {return applyFunction(targetArray, (x, i) -> condition.test(x) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(targetArray, (x, i) -> condition.apply(x), (x, i) -> f.apply(x));}
    public static <U extends Double> double[] applyFunctionIfCondition(
        double[] doubleArray,
        BiFunction<Double, Integer, Boolean> condition,
        BiFunction<Double, Integer, U> f
    ) {
        return applyFunction(doubleArray, (x, i) -> condition.apply(x, i) ? f.apply(x, i) : x);
//        List<Double> result = new LinkedList<>();
//        for (int i = 0; i < doubleArray.length; i++) {
//            if (condition.apply(doubleArray[i], i)) result.add(f.apply(doubleArray[i], i));
//            else result.add(doubleArray[i]);
//        }
//        return result;
    }
    public static <U extends Double> double[] applyFunctionIfCondition(
        double[] doubleArray,
        Predicate<Double> condition,
        BiFunction<Double, Integer, U> f
    ) {return applyFunction(doubleArray, (x, i) -> condition.test(x) ? f.apply(x, i) : x);}
    //{return applyFunctionIfCondition(doubleArray, (x, i) -> condition.apply(x), f);}
    public static <U extends Double> double[] applyFunctionIfCondition(
        double[] doubleArray,
        BiFunction<Double, Integer, Boolean> condition,
        Function<Double, U> f
    ) {return applyFunction(doubleArray, (x, i) -> condition.apply(x, i) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(doubleArray, condition, (x, i) -> f.apply(x));}
    public static <U extends Double> double[] applyFunctionIfCondition(
        double[] doubleArray,
        Predicate<Double> condition,
        Function<Double, U> f
    ) {return applyFunction(doubleArray, (x, i) -> condition.test(x) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(doubleArray, (x, i) -> condition.apply(x), (x, i) -> f.apply(x));}
    public static <U extends Integer> int[] applyFunctionIfCondition(
        int[] intArray,
        BiFunction<Integer, Integer, Boolean> condition,
        BiFunction<Integer, Integer, U> f
    ) {
        return applyFunction(intArray, (x, i) -> condition.apply(x, i) ? f.apply(x, i) : x);
//        List<Integer> result = new LinkedList<>();
//        for (int i = 0; i < intArray.length; i++) {
//            if (condition.apply(intArray[i], i)) result.add(f.apply(intArray[i], i));
//            else result.add(intArray[i]);
//        }
//        return result;
    }
    public static <U extends Integer> int[] applyFunctionIfCondition(
        int[] intArray,
        Predicate<Integer> condition,
        BiFunction<Integer, Integer, U> f
    ) {return applyFunction(intArray, (x, i) -> condition.test(x) ? f.apply(x, i) : x);}
    //{return applyFunctionIfCondition(intArray, (x, i) -> condition.apply(x), f);}
    public static <U extends Integer> int[] applyFunctionIfCondition(
        int[] intArray,
        BiFunction<Integer, Integer, Boolean> condition,
        Function<Integer, U> f
    ) {return applyFunction(intArray, (x, i) -> condition.apply(x, i) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(intArray, condition, (x, i) -> f.apply(x));}
    public static <U extends Integer> int[] applyFunctionIfCondition(
        int[] intArray,
        Predicate<Integer> condition,
        Function<Integer, U> f
    ) {return applyFunction(intArray, (x, i) -> condition.test(x) ? f.apply(x) : x);}
    //{return applyFunctionIfCondition(intArray, (x, i) -> condition.apply(x), (x, i) -> f.apply(x));}
    
    /*
    * Operates the first two elements of an array/List, then operates the result
    * with the third, and so on... then returns the last result.
    * If startAtBeginning is false, it starts operating the last two elements.
    * Will throw an error if the array is empty.
    * If the array has exactly one element, it will return that element.
    */
    
    /*
    * TO DO: Find how to implement the concatenatedOperator method with streams.
    * Current problems:
    *   .reduce(BinaryOperator) returns an Optional, which can't be cast.
    *   Need to find how to implement the startAtBeginning state
    */
    public static <T> T concatenatedOperator(
        T[] targetArray,                // Array to operate on.
        BiFunction<T, T, T> operator,   // Operator to apply to the array.
        boolean startAtBeginning        // State parameter. Set to true to start at the beginning of the array.
    ) {
        T t = startAtBeginning ? targetArray[0] : targetArray[targetArray.length - 1];
        if (targetArray.length > 1 && startAtBeginning) for (int i = 1; i < targetArray.length; i++) t = operator.apply(t, targetArray[i]);
        else if (targetArray.length > 1 && !startAtBeginning) for (int i = targetArray.length - 2; i >= 0; i--) t = operator.apply(t, targetArray[i]);
        return t;
    }
    public static <T> T concatenatedOperator(
        List<T> targetArray,            // List to operate on.
        BiFunction<T, T, T> operator,   // Operator to apply to the List.
        boolean startAtBeginning        // State parameter. Set to true to start at the beginning of the List.
    ) {
        T t = startAtBeginning ? targetArray.get(0) : targetArray.get(targetArray.size() - 1);
        if (targetArray.size() > 1 && startAtBeginning) for (int i = 1; i < targetArray.size(); i++) t = operator.apply(t, targetArray.get(i));
        else if (targetArray.size() > 1 && !startAtBeginning) for (int i = targetArray.size() - 2; i >= 0; i--) t = operator.apply(t, targetArray.get(i));
        return t;
    }
    public static double concatenatedOperator(
        double[] doubleArray,
        BiFunction<Double, Double, Double> operator,
        boolean startAtBeginning
    ) {
        double t = startAtBeginning ? doubleArray[0] : doubleArray[doubleArray.length - 1];
        if (doubleArray.length > 1 && startAtBeginning) for (int i = 1; i < doubleArray.length; i++) t = operator.apply(t, doubleArray[i]);
        else if (doubleArray.length > 1 && !startAtBeginning) for (int i = doubleArray.length - 2; i >= 0; i--) t = operator.apply(t, doubleArray[i]);
        return t;
    }
    public static int concatenatedOperator(
        int[] intArray,
        BiFunction<Integer, Integer, Integer> operator,
        boolean startAtBeginning
    ) {
        int t = startAtBeginning ? intArray[0] : intArray[intArray.length - 1];
        if (intArray.length > 1 && startAtBeginning) for (int i = 1; i < intArray.length; i++) t = operator.apply(t, intArray[i]);
        else if (intArray.length > 1 && !startAtBeginning) for (int i = intArray.length - 2; i >= 0; i--) t = operator.apply(t, intArray[i]);
        return t;
    }
    
    public static <T> T concatenatedOperator(T[] targetArray, BiFunction<T, T, T> operator) {return concatenatedOperator(targetArray, operator, true);}
    public static <T> T concatenatedOperator(List<T> targetArray, BiFunction<T, T, T> operator) {return concatenatedOperator(targetArray, operator, true);}
    public static double concatenatedOperator(double[] targetArray, BiFunction<Double, Double, Double> operator) {return concatenatedOperator(targetArray, operator, true);}
    public static int concatenatedOperator(int[] targetArray, BiFunction<Integer, Integer, Integer> operator) {return concatenatedOperator(targetArray, operator, true);}

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Number array methods">
    /*
    * Contains a number of methods dealing with arrays/Lists of numbers, ie,
    * int or double primitive types. All the methods can be expanded to other
    * numeric primitive types, but I preferred to only implement those two.
    *
    * Contains the following methods:
    *   doubleListToDoubleArray: Returns a List<Integer> with all the elements of an int[] array.
    *   intListToIntArray: Returns a List<Double> with all the elements of a double[] array.
    *   booleanToIntArray: Return a List<Integer> with 0 where the original array had false, and 1 where it had true.
    *   vectorToString: Returns a String with the standard representation of a vector, of the form "(a, b, c, ...)"
    *   sum: Sums all the elements of a number array.
    *   mult: Multiplies all the elements of a number array.
    *   average: Returns the average, or mean, of the elements of a number array: 1/n times the sum of all those numbers, where n is the amount of numbers.
    *   geometricAverage: Returns the geometric average of the elements of a number array, the n-th root of the multiplication of all those numbers, where n is the amount of numbers.
    *   maximum: Returns the maximum value of a number array.
    *   maxIndexes: Returns an int[] array with all the indexes where the maximum value appears.
    *   minimum: Returns the minimum value of a number array.
    *   minIndexes: Returns an int[] array with all the indexes where the minimum value appears.
    *   sum: Sums two number arrays element-wise.
    *   mult: Multiplies two number arrays element-wise.
    *   scalarProduct: Returns the value of the scalar product of two number arrays when viewed as two vectors.
    *   module: Returns the module of a number array when viewed as a vector.
    *   vectorOf: Returns a number array with all of its elements equal to a certain double value.
    *   multByScalar: Multiplies each element of a number array by a certain double value.
    */
    
    // Copies a List<Double> to a double[] array.
    public static double[] doubleListToDoubleArray(List<Double> doubleList) {
        return doubleList.stream()
            .mapToDouble(Double::doubleValue)
            .toArray();      
//        double[] result = new double[doubleList.size()];
//        for (int i = 0; i < result.length; i++) result[i] = doubleList.get(i);
//        return result;
    }
    // Copies a List<Integer> to an int[] array.
    public static int[] intListToIntArray(List<Integer> intList) {
        return intList.stream()
            .mapToInt(Integer::intValue)
            .toArray();
//        int[] result = new int[intList.size()];
//        for (int i = 0; i < result.length; i++) result[i] = intList.get(i);
//        return result;
    }
    
    // Converts a boolean[] array to an int[] array, where true -> 1 and false -> 0.
    // TO DO implement this method with streams.
    public static int[] booleanToIntArray(boolean[] boolArray) {
        int[] result = new int[boolArray.length];
        for (int i = 0; i < result.length; i++) result[i] = boolArray[i] ? 1 : 0;
        return result;
    }
    
    // TO DO implement this method with streams.
    public static String vectorToString(double[] x) {
//        return DoubleStream.of(x)
//            .collect(Collectors.joining(",", "(", ")"));
        StringBuilder result = new StringBuilder("(");
        for (int i = 0; i < x.length; i++) {
            result.append(x[i]);
            if (i != x.length-1) result.append(", ");
        }
        result.append(")");
        return result.toString();
    }
    
    // Sums all elements of a numeric array.
    public static double sum(double... numberArray) //{return concatenatedOperator(numberArray, (x, y) -> x + y);}
    {
        return DoubleStream.of(numberArray)
            .reduce(0, (x, y) -> x+y);
    }
    public static int sum(int... numberArray) //{return concatenatedOperator(numberArray, (x, y) -> x + y);}
    {
        return IntStream.of(numberArray)
            .reduce(0, (x, y) -> x+y);
    }
    // Multiplies all elements of a numeric array.
    public static double mult(double... numberArray) //{return concatenatedOperator(numberArray, (x, y) -> x * y);}
    {
        return DoubleStream.of(numberArray)
            .reduce(1, (x, y) -> x*y);
    }
    public static int mult(int... numberArray) //{return concatenatedOperator(numberArray, (x, y) -> x * y);}
    {
        return IntStream.of(numberArray)
            .reduce(1, (x, y) -> x*y);
    }
            
    // Calculates the average of all elements of a numeric array.
    public static double average(double... numberArray) {return sum(numberArray)/numberArray.length;}
    
    // Calculates the geometric average of all elements of a numeric array
    public static double geometricAverage(double... numberArray) {return Math.pow(CalcUtil.mult(numberArray), 1/numberArray.length);}
    
    // Finds the maximum value of a numeric array.
    public static double maximum(double... numberArray) {
        double max = Double.MIN_VALUE;
        for (double d : numberArray) if (d > max) max = d;
        return max;
    }
    public static int maximum(int... numberArray) {
        int max = Integer.MIN_VALUE;
        for (int n : numberArray) if (n > max) max = n;
        return max;
    }
    
    // Returns an int[] array of all indexes where the maximum value is found.
    public static int[] maxIndexes(double[] numberArray) {
        List<Integer> result = new LinkedList<>();
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberArray.length; i++) {
            double d = numberArray[i];
            if (d == max) result.add(i);
            else if (d > max) {
                max = d;
                result.clear();
                result.add(i);
            }
        }
        return intListToIntArray(result);
    }
    public static int[] maxIndexes(int[] numberArray) {
        List<Integer> result = new LinkedList<>();
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < numberArray.length; i++) {
            int n = numberArray[i];
            if (n == max) result.add(i);
            else if (n > max) {
                max = n;
                result.clear();
                result.add(i);
            }
        }
        return intListToIntArray(result);
    }
    
    // Finds the minimum value of a numeric array.
    public static double minimum(double... numberArray) {
        double min = Double.MAX_VALUE;
        for (double d : numberArray) if (d < min) min = d;
        return min;
    }
    public static int minimum(int... numberArray) {
        int min = Integer.MAX_VALUE;
        for (int n : numberArray) if (n < min) min = n;
        return min;
    }
    // Returns an int[] array of all indexes where the maximum value is found.
    public static int[] minIndexes(double[] numberArray) {
        List<Integer> result = new LinkedList<>();
        double min = Double.MAX_VALUE;
        for (int i = 0; i < numberArray.length; i++) {
            double d = numberArray[i];
            if (d == min) result.add(i);
            else if (d < min) {
                min = d;
                result.clear();
                result.add(i);
            }
        }
        return intListToIntArray(result);
    }
    public static int[] minIndexes(int[] numberArray) {
        List<Integer> result = new LinkedList<>();
        int min = Integer.MIN_VALUE;
        for (int i = 0; i < numberArray.length; i++) {
            int n = numberArray[i];
            if (n == min) result.add(i);
            else if (n < min) {
                min = n;
                result.clear();
                result.add(i);
            }
        }
        return intListToIntArray(result);
    }
    
    /*
    * Sums two numeric arrays element-wise.
    * If one of the summands has lower length than the other,
    * it will be extended to be of the same length, padded with zeroes.
    */
    public static double[] sum(double[] a1, double[] a2) {
        int len1 = a1.length, len2 = a2.length;
        if (len1 < len2) return sum(a2, a1);
        if (len2 < len1) {
            a2 = Arrays.copyOf(a2, len1);
            a2 = applyFunction(a2, (n, i) -> i < len2 ? n : 0);
        }
        double[] result = new double[len1];
        for (int i = 0; i < len1; i++) result[i] = a1[i] + a2[i];
        return result;
    }
    public static int[] sum(int[] a1, int[] a2) {
        int len1 = a1.length, len2 = a2.length;
        if (len1 < len2) return sum(a2, a1);
        if (len2 < len1) {
            a2 = Arrays.copyOf(a2, len1);
            a2 = applyFunction(a2, (n, i) -> i < len2 ? n : 0);
        }
        int[] result = new int[len1];
        for (int i = 0; i < len1; i++) result[i] = a1[i] + a2[i];
        return result;
    }
    
    /*
    * Multiplies two numeric arrays element-wise.
    * If one of the factors has lower length than the other,
    * it will be extended to be of the same length, padded with ones.
    */
    public static double[] mult(double[] a1, double[] a2) {
        int len1 = a1.length, len2 = a2.length;
        if (len1 < len2) return mult(a2, a1);
        if (len2 < len1) {
            a2 = Arrays.copyOf(a2, len1);
            a2 = applyFunction(a2, (n, i) -> i < len2 ? n : 1);
        }
        double[] result = new double[len1];
        for (int i = 0; i < len1; i++) result[i] = a1[i] * a2[i];
        return result;
    }
    public static int[] mult(int[] a1, int[] a2) {
        int len1 = a1.length, len2 = a2.length;
        if (len1 < len2) return mult(a2, a1);
        if (len2 < len1) {
            a2 = Arrays.copyOf(a2, len1);
            a2 = applyFunction(a2, (n, i) -> i < len2 ? n : 1);
        }
        int[] result = new int[len1];
        for (int i = 0; i < len1; i++) result[i] = a1[i] * a2[i];
        return result;
    }
    
    // Calculates the scalar product of two numeric arrays, as if they were vectors of real numbers.
    public static double scalarProduct(double[] a1, double[] a2) {
        if (a1.length != a2.length) return Double.NaN;
        return sum(mult(a1, a2));
    }
    public static int scalarProduct(int[] a1, int[] a2) {return sum(mult(a1, a2));}
    
    // Calculates the module of a number array, as if it were a vector of real numbers.
    public static double module(double... numberArray) {return Math.sqrt(sum(applyFunction(numberArray, x -> Math.pow(x, 2))));}
    
    // Creates a double[] array of the specified length whose elements are all the same double input.
    public static double[] vectorOf(double d, int length) {
        double[] result = new double[length];
        for (int i = 0; i < length; i++) result[i] = d;
        return result;
    }
    
    // Multiplies all elements of a double[] array by a given double value.
    public static double[] multByScalar(double[] numberArray, double d) {return mult(numberArray, vectorOf(d, numberArray.length));}
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Function Methods">
    /*
    * Contains a small number of methods dealing with functions and operations
    * with them. Contains the followind methods:
    *   functionSum: Returns the function sum of two number functions.
    *   functionInvertSign: Returns the function with inverted sign of a number function.
    *   functionSubstr: Returns the function substraction of two number functions.
    *   functionMult: Returns the function multiplication of two number functions.
    */
    
    public static Function<Double, Double> functionSum(Function<Double, Double> f, Function<Double, Double> g) {return x -> f.apply(x) + g.apply(x);}
    public static Function<Double, Double> functionInvertSign(Function<Double, Double> f) {return x -> -f.apply(x);}
    public static Function<Double, Double> functionSubstr(Function<Double, Double> f, Function<Double, Double> g) {return functionSum(f, functionInvertSign(g));}
    public static Function<Double, Double> functionMult(Function<Double, Double> f, Function<Double, Double> g) {return x -> f.apply(x) * g.apply(x);}
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Root Finder Methods">
    /*
    * Contains two numerical methods for finding the root of a given number function:
    *
    *   The bisection method, which requires the lower and upper bound of an interval
    *   where the function has opposites sign at each side.
    *
    *   A generic Root Finder Method, which requires a step function to be passed
    *   as an argument in order to compute each iteration. The next section will
    *   provide many step functions for some popular methods.
    * function
    */
    
    
    /*
    * The Bisection Method behaves this way:
    *
    * Two bounds a and b of a real interval are given, such that the function f
    * has opposite signs at each side, ie, f(a) * f(b) < 0. If this condition is
    * false with the given values of a and b, NaN will be returned.
    *
    * Then we calculate c = (a+b)/2 as the midpoint between a and b. It follows 
    * that either f(c) * f(a) < 0 or f(c) * f(b) < 0. If the first case is true,
    * then we take b = c and repeat this iteration, otherwise, we take a = c.
    *
    * We repeat this process until f(c) is close enough to 0 (within eps
    * distance). When this is the case, we return c.
    *
    * maxit is the maximum number of iterations the method will perform before ending.
    * prinIterations represent whether or not you want the method to print the values
    * of each iteration as it calculates them.
    */
    public static double bisectionMethod(
        Function<Double, Double> f, // Function we want to find a root of.
        double a,                   // Lower bound of the starting interval.
        double b,                   // Upper bound of the starting interval. It must be true that f(a) * f(b) < 0, or NaN will be returned.
        double eps,                 // Distance from 0 that we deem acceptable to stop the method.
        int maxit,                  // Maximum number of iterations.
        boolean printIterations     // State parameter to print the results of each iteration or not.
    ) {
        if (printIterations) {
            System.out.println("| it\t| a\t\t| b\t\t| f(a)\t\t| f(b)\t\t|");
            System.out.printf("| %d\t| %.8f\t| %.8f\t| %.8f\t| %.8f\t|\n", 0, a, b, f.apply(a), f.apply(b));
        }
        if (f.apply(a)*f.apply(b) > 0) return Double.NaN;
        for (int n = 0; n <= maxit; n++) {
            double c = (a+b)/2;
            if (f.apply(c) * f.apply(a) < 0) b = c;
            else if (f.apply(c) * f.apply(b) < 0) a = c;
            if (printIterations) System.out.printf("| %d\t| %.8f\t| %.8f\t| %.8f\t| %.8f\t|\n", n, a, b, f.apply(a), f.apply(b));
            if (Math.abs(f.apply(c)) < eps) return c;
        }
        if (printIterations) System.out.println("Maximum number of iterations reached.");
        return a;
    }
    
    /*
    * The Root Finder Method behaves simply:
    * We try to apply the nextStep function passed as argument to calculate the
    * value of x for the next iteration. If doing that throws any exception,
    * the method returns NaN. Otherwise, the value of x is updated this way.
    * When the function f applied to x is within distance eps of 0, the method
    * stops and returns x.
    *
    * maxit is the maximum number of iterations the method will perform before ending.
    * prinIterations represent whether or not you want the method to print the values
    * of each iteration as it calculates them.
    */
    public static double rootFinderMethod(
        Function<Double, Double> f,         // Function we want to find a root of.
        Function<Double, Double> nextStep,  // Function used to update the value of x each iteration.
        double x,                           // Starting value. For some step functions, this needs to be reasonably near a root of f.
        double eps,                         // Distance from 0 that we deem acceptable to stop the method.
        int maxit,                          // Maximum number of iterations.
        boolean printIterations             // State parameter to print the results of each iteration or not.
    ) {
        int counter = 1;
        if (printIterations) {
            System.out.printf("Finding roots of the function: " + f + "\nWith initial value %.2f\nAnd precision, %.0E\n", x, eps);
            System.out.println("| it\t| x\t\t| f(x)\t\t|");
            System.out.printf("| %d\t| %.8f\t| %.8f\t|\n", 0, x, f.apply(x));
        }
        while (Math.abs(f.apply(x)) > eps && counter < maxit) {
            try {x = nextStep.apply(x);}
            catch (Exception e) {
                if (printIterations) System.out.println("Error while calculating iteration " + counter);
                return Double.NaN;
            }
            if (printIterations) System.out.printf("| %d\t| %.8f\t| %.8f\t|\n", counter, x, f.apply(x));
            counter++;
            if (Double.isNaN(x)) return Double.NaN;
        }
        if (printIterations && counter >= maxit) System.out.println("Maximum number of iterations reached.");
        return x;
    }
    
    /*
    * The following two methods are versions adapted to expect an object of the
    * RealPolynomial and RationalFunction classes, also made by me, using their
    * method .toFunction() to interpret them as Double -> Double functions.
    *
    * The step function is unmodified here, but versions of some popular step
    * functions will be provided on next section that are specifically adapted
    * for polynomials and rational functions, since those have derivatives that
    * can be analytically calculated instead of approximated numerically.
    */
    public static double rootFinderMethod(
        RealPolynomial p,                   // Polynomial we want to find a root of.
        Function<Double, Double> nextStep,  // Function used to update the value of x each iteration.
        double x,                           // Starting value. For some step functions, this needs to be reasonably near a root of f.
        double eps,                         // Distance from 0 that we deem acceptable to stop the method.
        int maxit,                          // Maximum number of iterations.
        boolean printIterations             // State parameter to print the results of each iteration or not.
    ) {return rootFinderMethod(p.toFunction(), nextStep, x, eps, maxit, printIterations);}
    
    public static double rootFinderMethod(
        RationalFunction r,                 // Rational function we want to find a root of.
        Function<Double, Double> nextStep,  // Function used to update the value of x each iteration.
        double x,                           // Starting value. For some step functions, this needs to be reasonably near a root of f.
        double eps,                         // Distance from 0 that we deem acceptable to stop the method.
        int maxit,                          // Maximum number of iterations.
        boolean printIterations             // State parameter to print the results of each iteration or not.     
    ) {return rootFinderMethod(r.toFunction(), nextStep, x, eps, maxit, printIterations);}
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Various step functions">
    /*
    * Contains some well-known step functions to be used with the generic
    * Root Finder Method of the previous section, namely:
    *   Newton's Method
    *   Halley's Method
    *   Householder's Method
    *   Compound Householder's Method
    *
    * Each of those methods is adapted to also work nicely with objects of the
    * RealPolynomial and RationalFunction classes, also made by me, since those
    * types of functions have derivatives that can be analytically calculated,
    * and that is implemented by the .diff() method of their respective classes.
    *
    * For regular functions, numerical approximations of derivatives are used
    * when needed, which are provided in the next section.
    */
    
    // Newton's Method equation: N(x) = x - f(x)/f'(x)
    public static Function<Double, Double> Newton(RealPolynomial p) {return x -> x - p.evaluate(x)/p.diff().evaluate(x);}
    public static Function<Double, Double> Newton(RationalFunction r) {return x -> x - r.evaluate(x)/r.diff().evaluate(x);}
    public static Function<Double, Double> Newton(Function<Double, Double> f) {return x -> x - f.apply(x)/derivativeAt(f, x);}
    // Halley's Method equation: H(x) = x - (2f(x)f'(x)) / (2f'(x)^2 - f(x)f''(x))
    public static Function<Double, Double> Halley(RealPolynomial p) {return x -> x - 2*p.evaluate(x)*p.diff().evaluate(x)/(2*Math.pow(p.diff().evaluate(x), 2) - p.evaluate(x)*p.diff(2).evaluate(x));}
    public static Function<Double, Double> Halley(RationalFunction r) {return x -> x - 2*r.evaluate(x)*r.diff().evaluate(x)/(2*Math.pow(r.diff().evaluate(x), 2) - r.evaluate(x)*r.diff(2).evaluate(x));}
    public static Function<Double, Double> Halley(Function<Double, Double> f) {return x -> x - 2*f.apply(x)*derivativeAt(f, x)/(2*Math.pow(derivativeAt(f, x), 2) - f.apply(x)*derivativeAt(f, x, 2));}
    // Householder's Method equation: HH(x) = x - (6f(x)f'(x)^2 - 3f(x)^2f''(x)) / (6f'(x)^3 - 6f(x)f'(x)f''(x) + f(x)^2f'''(x)
    public static Function<Double, Double> Householder(RealPolynomial p) {return x -> x - (6*p.evaluate(x)*Math.pow(p.diff().evaluate(x), 2) - 3*Math.pow(p.evaluate(x), 2)*p.diff(2).evaluate(x)) / (6*Math.pow(p.diff().evaluate(x), 3) - 6*p.evaluate(x)*p.diff().evaluate(x)*p.diff(2).evaluate(x) + Math.pow(p.evaluate(x), 2)*p.diff(3).evaluate(x));}
    public static Function<Double, Double> Householder(RationalFunction r) {return x -> x - (6*r.evaluate(x)*Math.pow(r.diff().evaluate(x), 2) - 3*Math.pow(r.evaluate(x), 2)*r.diff(2).evaluate(x)) / (6*Math.pow(r.diff().evaluate(x), 3) - 6*r.evaluate(x)*r.diff().evaluate(x)*r.diff(2).evaluate(x) + Math.pow(r.evaluate(x), 2)*r.diff(3).evaluate(x));}
    public static Function<Double, Double> Householder(Function<Double, Double> f) {return x -> (6*f.apply(x)*Math.pow(derivativeAt(f, x), 2) - 3*Math.pow(f.apply(x), 2)*derivativeAt(f, x, 2)) / (6*Math.pow(derivativeAt(f, x), 3) - 6*f.apply(x)*derivativeAt(f, x)*derivativeAt(f, x, 2) + Math.pow(f.apply(x), 2)*derivativeAt(f, x, 3));}
    /* Compound Householder Method equation:
    *   y = x - f(x)/f'(x) (Newton's Method)
    *   cH(x) = y - f(y)f''(y)/2f'''(y)
    */
    public static Function<Double, Double> compoundHouseholderAux(RealPolynomial p) {return x -> Newton(p).apply(x) - (p.evaluate(x)*p.diff(2).evaluate(x)/(2*p.diff(3).evaluate(x)));}
    public static Function<Double, Double> compoundHouseholderAux(RationalFunction r) {return x -> Newton(r).apply(x) - (r.evaluate(x)*r.diff(2).evaluate(x)/(2*r.diff(3).evaluate(x)));}
    public static Function<Double, Double> compoundHouseholderAux(Function<Double, Double> f) {return x -> Newton(f).apply(x) - (f.apply(x)*derivativeAt(f, x, 2)/(2*derivativeAt(f, x, 3)));}
    public static Function<Double, Double> compoundHouseholder(RealPolynomial p) {return CalcUtil.compoundHouseholderAux(p).compose(Newton(p));}
    public static Function<Double, Double> compoundHouseholder(RationalFunction r) {return CalcUtil.compoundHouseholderAux(r).compose(Newton(r));}
    public static Function<Double, Double> compoundHouseholder(Function<Double, Double> f) {return compoundHouseholderAux(f).compose(Newton(f));}
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Derivatives">
    /*
    * Contains only three distinctly named methods:
    *   derivativeAt: Calculates an approximation of the derivative of a number function at a given point using the finite difference method.
    *   derivative: Returns a Function<Double, Double> approximation of the derivative of a function, using the previous method to calculate the derivative at each point.
    *   analyticVsNumericalDerivative: A method that prints the values of the analytic and numerical calculations of the derivative of a Polynomial, to grasp the accuracy of the approximation.
    */
    
    
    /*
    * Approximates the value of a real function f: R -> R (passed as a Function
    * from Double to Double), at a point x, with a step h, and a given accuracy
    * and order.
    *
    * Accuracy accepts the values 2, 4, 6 or 8, otherwise it will return NaN.
    * Order can be from 0 to 6. Higher values will return NaN.
    * Order 1 and 2 accept accuracies of 2, 4, 6 and 8. Order 3 to 6 only 2, 4 and 6.
    * Inserting a wrong combination of order and accuracy will return NaN.
    * The higher the accuracy the more precise the approximation of the derivative,
    * at the cost of more computations and possible loss of precision in combination
    * with a small value of h and a high order.
    */
    public static double derivativeAt(
        Function<Double, Double> f, // Function to derivate.
        double x,                   // Point to evaluate the derivative at.
        double h,                   // Size of the step used in the approximation. Too small a step can cause bigger rounding errors on higher order, higher accuracy calls of the method.
        int accuracy,               // Accepts values 2, 4, 6 and 8 for order 1 and 2 derivatives, and 2, 4 and 6 for orders 3 to 6. It increases the accuracy of the approximation at the cost of more computations. Rarely worth going above 4.
        int order                   // Order of the derivative. A value of 0 would simply evaluate the function at x.
    ) {
        if (h == 0 && order != 0) return Double.NaN;
        if (order > 6
            || (accuracy != 2 && accuracy != 4 && accuracy != 6 && accuracy != 8)
            || (order >= 3 && order <= 6 && accuracy == 8)) return Double.NaN;
        if (order == 0) return f.apply(x);
        accuracy = accuracy/2 - 1;
        double result = 0;
        double[] finiteDiffCoef = CalcUtil.finiteDifferenceCoef[order][accuracy];
        int len = finiteDiffCoef.length;
        for (int i = 0, j = -len/2; i < len; i++, j++) result += finiteDiffCoef[i] * f.apply(x + j*h);
        result = result / Math.pow(h, order);
        return result;
    }
    public static double derivativeAt(
        Function<Double, Double> f, // Function to derivate.
        double x                    // Point to evaluate the derivative at.
                                    // h takes the default value of 1e-3.
                                    // accuracy takes the default value of 2.
                                    // order takes the default value of 1.
    ) {return derivativeAt(f, x, 1e-3, 2, 1);}
    public static double derivativeAt(
        Function<Double, Double> f, // Function to derivate.
        double x,                   // Point to evaluate the derivative at.
        int order                   // Order of the derivative. A value of 0 would simply evaluate the function at x.
    ) {return derivativeAt(f, x, 1e-3, 2, order);}
    public static double derivativeAt(
        Function<Double, Double> f, // Function to derivate.
        double x,                   // Point to evaluate the derivative at.
        double h,                   // Size of the step used in the approximation. Too small a step can cause bigger rounding errors on higher order, higher accuracy calls of the method.
        int order                   // Order of the derivative. A value of 0 would simply evaluate the function at x.
    ) {return derivativeAt(f, x, h, 2, order);}
    
    public static Function<Double, Double> derivative(
        Function<Double, Double> f, // Function to derivate.
        double h,                   // Size of the step used in the approximation. Too small a step can cause bigger rounding errors on higher order, higher accuracy calls of the method.
        int accuracy,               // Accepts values 2, 4, 6 and 8 for order 1 and 2 derivatives, and 2, 4 and 6 for orders 3 to 6. It increases the accuracy of the approximation at the cost of more computations. Rarely worth going above 4.
        int order                   // Order of the derivative. A value of 0 would simply return the function f.
    ) {return x -> derivativeAt(f, x, h, accuracy, order);}
    public static Function<Double, Double> derivative(
        Function<Double, Double> f  // Function to derivate.
    ) {return x -> derivativeAt(f, x);}
    public static Function<Double, Double> derivative(
        Function<Double, Double> f, // Function to derivate.
        int order                   // Order of the derivative. A value of 0 would simply return the function f.
    ) {return x -> derivativeAt(f, x, order);}
    public static Function<Double, Double> derivative(
        Function<Double, Double> f, // Function to derivate.
        double h,                   // Size of the step used in the approximation. Too small a step can cause bigger rounding errors on higher order, higher accuracy calls of the method.
        int order                   // Order of the derivative. A value of 0 would simply return the function f.
    ) {return x -> derivativeAt(f, x, h, order);}
    
    /*
    * The following method prints the value calculated with the analytic
    * calculation of the derivative of a polynomial, of the RealPolynomial class,
    * and the numerical derivative approximated by the finite difference method,
    * as implemented in this section.
    *
    * It shows the value of both derivative calculations, the absolute difference 
    * between those values, and the relative difference.
    *
    * The main purpose of this method is to illustrate the accuracy of the numerical
    * approximations of the derivative, and how they lose accuracy with very small
    * values of h in combination with higher accuracy values or in higher order
    * derivatives.
    */
    public static void analyticVsNumericalDerivative(
        RealPolynomial p,   // Polynomial to derivate.
        double initValue,   // Initial value to start printing evaluations.
        double finalValue,  // Final value to end printing evaluations.
        double step,        // Step between succesive evaluations.
        double h,           // h value for the derivative evaluations.
        int accuracy,       // accuracy for the derivative evaluations.
        int order           // Order of the derivatives taken.
    ) {
        System.out.println("| x\t\t| p'(x) analytic\t| p'(x) numeric\t| Abs. Differ. \t| Rel. Differ.\t|");
        RealPolynomial derivativePolynomial = p.diff(order);
        Function<Double, Double> derivativePolynomialFunction = derivative(p.toFunction(), h, accuracy, order);
        for (double x = initValue; x <= finalValue + step; x+=step) {
            double analyticValue = derivativePolynomial.evaluate(x);
            double numericValue = derivativePolynomialFunction.apply(x);
            double difference = Math.abs(analyticValue - numericValue);
            double mean = Math.abs(analyticValue + numericValue)/2;
            double relativeDifference = difference/mean;
            System.out.printf("| %.8f\t| %.8f\t| %.8f\t| %.2E\t| %.2E\t|\n", x, analyticValue, numericValue, difference, relativeDifference);
        }
        System.out.println("| x\t\t| p'(x) analytic\t| p'(x) numeric\t| Abs. Differ. \t| Rel. Differ.\t|");
    }
    public static void analyticVsNumericDerivative(
        RealPolynomial p,   // Polynomial to derivate.
        double initValue,   // Initial value to start printing evaluations.
        double finalValue,  // Final value to end printing evaluations.
        double step         // Step between succesive evaluations.
                            // h takes a default value of 1e-3.
                            // accuracy takes a default value of 2.
                            // order takes a default value of 1.
    ) {analyticVsNumericalDerivative(p, initValue, finalValue, step, 1e-3, 2, 1);}
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Integrals">
    /*
    * Contains a few numerical integration methods to approximate the value of
    * the integral of a number function in a given interval. Those methods are:
    *   Midpoint Rule
    *   Trapezoidal Rule
    *   Simpson's Rule
    *
    * TO DO: testing
    */
    
    public static double midpointRule(
        Function<Double, Double> f, // Function to approximate the integral of.
        double lowerBound,          // Lower bound of the integration interval.
        double upperBound,          // Upper bound of the integration interval.
        int n                       // Number of subintervals to divide the interval into.
    ) {
        double[] interval = partitionInterval(lowerBound, upperBound, n);
        double[] coefficients = new double[interval.length];
        for (int i = 0; i < coefficients.length; i++) coefficients[i] = 1;
        double step = (upperBound - lowerBound)/n;
        return step * scalarProduct(applyFunction(interval, x -> f.apply(x)), coefficients);
    }
    
    public static double trapezoidalRule(
        Function<Double, Double> f, // Function to approximate the integral of.
        double lowerBound,          // Lower bound of the integration interval.
        double upperBound,          // Upper bound of the integration interval.
        int n                       // Number of subintervals to divide the interval into.
    ) {
        double[] interval = partitionInterval(lowerBound, upperBound, n);
        double[] coefficients = new double[interval.length];
        for (int i = 0; i < coefficients.length; i++) {
            if (i == 0 || i == coefficients.length - 1) coefficients[i] = 1;
            else coefficients[i] = 2;
        }
        double step = (upperBound - lowerBound)/n;
        return step/2 * scalarProduct(applyFunction(interval, x -> f.apply(x)), coefficients);
    }
    
    public static double SimpsonRule(
        Function<Double, Double> f, // Function to approximate the integral of.
        double lowerBound,          // Lower bound of the integration interval.
        double upperBound,          // Upper bound of the integration interval.
        int n                       // Number of subintervals to divide the interval into.
    ) {
        double[] interval = partitionInterval(lowerBound, upperBound, n);
        double[] coefficients = new double[interval.length];
        for (int i = 0; i < coefficients.length; i++) {
            if (i == 0 || i == coefficients.length - 1) coefficients[i] = 1;
            else if (i % 2 == 1) coefficients[i] = 4;
            else coefficients[i] = 2;
        }
        double step = (upperBound - lowerBound)/n;
        return step/3 * scalarProduct(applyFunction(interval, x -> f.apply(x)), coefficients);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Gradient Descent">
    /*
    * Implements the gradient descent method to calculate the minimum of a
    * multivalued real function, represented by a Function from double[] to
    * Double.
    *
    * It only implements the easiest version of that method, when it's applied
    * to a quadratic function of the form J(x) = 1/2 x'Ax - b'x, where A is a
    * matrix, b a vector and ' represent transposition. This case doesn't even
    * deserve to use gradient descent on it, since it can also be solved with
    * simple linear algebra, since the jacobian of J is Ax - b, and the minimum
    * can be located where the jacobian vanishes.
    *
    * To work with matrices, this section uses the RealMatrix class, also
    * created by me.
    *
    * This section remains a WIP while I research the method more in depth and
    * learn how to properly implement it in the cases where it really is a valid
    * alternative to calculate the minimum of the given function.
    */
    
    
    /*
    * Constructs a multivalued quadratic function from the given matrix A
    * and vector b. The formula of such a function is:
    * J(x) = 1/2 x'Ax - b'x
    * Where ' represents matrix transposition.
    */
    public static Function<double[], Double> quadFunction(RealMatrix A, double[] b) {
        return x -> 0.5 * A.conjugateProduct(x) - scalarProduct(b, x);
    }
    
    public static double[] quadraticGradientDescent(
        RealMatrix A,
        double[] b,
        double[] x,
        double eps,
        int maxit,
        boolean printIterations
    ) {
        double[] d = sum(A.invertSign().applyToVector(x), b);
        Function<double[], Double> J = quadFunction(A, b);
        for (int k = 1; k < maxit; k++) {
            if (module(d) < eps) return x;
            double alpha = Math.pow(module(d),2) / A.conjugateProduct(d); // alpha = (d'd)/(d'Ad); where ' denotes transpose matrix, like it does in Matlab
            x = sum(x, multByScalar(d, alpha)); // x = x + alpha*d
            d = sum(d, multByScalar(A.applyToVector(d), -alpha)); // d = d - alpha*A*d
            if (printIterations) {
                System.out.println("Iteration number " + k);
                System.out.println("x = " + vectorToString(x));
                System.out.println("d = " + vectorToString(d));
                System.out.println("||d|| = " + module(d));
                System.out.println("J(x) = " + J.apply(x));
                System.out.println("------------------------------------------------------------------------");
            }
        }
        return x;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ODE resolution">
    // TO DO
    
    /*
    * Returns an array of doubles of size n+1, with initial value initValue,
    * final value finValue, and each inner value being equally spaced by a step
    * of (initValue + finValue)/n.
    */
    public static double[] partitionInterval(double initValue, double finValue, int n) {
        double step = (initValue + finValue) / n;
        double[] result = new double[n+1];
        result[0] = initValue;
        result[n] = finValue;
        for (int i = 1; i < n; i++) {
            initValue += step;
            result[i] = initValue;
        }
        return result;
    }
    
    public static void printGraphData(double[] x, double[] y) {
        if (x.length != y.length) return;
        System.out.println("| x \t\t| y \t\t|");
        for (int i = 0; i < x.length; i++) System.out.printf("| %8f \t| %8f \t|\n", x[i], y[i]);
    }
    
    /*
    * Solves the fixed point problem of finding the value x such that f(x) = x,
    * with a precision of eps, and starting to iterate on a certain initValue.
    * maxit is the highest number of iterations the method will perform before
    * ending.
    *
    * Depending on the choice of the function and initial value, convergence of
    * this method isn't guaranteed.
    */
    public static double fixedPoint(Function<Double, Double> f, double initValue, double eps, int maxit, boolean verbose) {
        if (verbose) System.out.println("| it\t| x\t\t| f(x)\t\t|");
        for (int i = 0; i <= maxit; i++) {
            double fx = f.apply(initValue);
            if (verbose) System.out.printf("| %d\t| %8f\t| %8f\t|\n", i, initValue, fx);
            if (Math.abs(fx - initValue) < eps) return initValue;
            if (Double.isNaN(fx)) return Double.NaN;
            initValue = fx;
        }
        return initValue;
    }
    public static double fixedPoint(Function<Double, Double> f, double initValue, double eps, int maxit) {return fixedPoint(f, initValue, eps, maxit, false);}
    
    /*
    * Solves an ODE of the form y' = f(x, y) on an interval [initValue, finValue]
    * with initial condition y(initValue) = initCondition. Returns an array of doubles
    * that is the approximation of the solution when evaluated on each of the
    * elements of the double array partitionInterval(initValue, finValue, n).
    * It follows the formula:
    * y[i] = y[i-1] + h * f(x[i-1], y[i-1])
    */
    public static double[] forwardEulerMethod(
        BiFunction<Double, Double, Double> f,   // Function of our ODE y' = f(x, y)
        double initValue,                       // Initial value to begin approximating our solution y
        double finValue,                        // Final value to end approximating out solution y
        double initCondition,                   // Condition, such that f(initValue) = initCondition
        int n                                   // Number of sections to divide out interval into
    ) {
        double[] x = partitionInterval(initValue, finValue, n);
        double step = (initValue + finValue) / n;
        double[] y = new double[n+1];
        y[0] = initCondition;
        for (int i = 1; i <= n; i++) {
            y[i] = y[i-1] + step * f.apply(x[i-1], y[i-1]);
        }
        return y;
    }
    
    /* WIP, not working properly
    * Solves an ODE of the form y' = f(x, y) on an interval [initValue, finValue]
    * with initial condition y(initValue) = initCondition. Returns an array of doubles
    * that is the approximation of the solution when evaluated on each of the
    * elements of the double array partitionInterval(initValue, finValue, n).
    * It follows the formula:
    * y[i] = y[i-1] + h * f(x[i], y[i])
    * Since y[i] appears in the calculation of itself, on each iteration a fixed
    * point problem will be solved, applied to the function:
    * z -> y[i-1] + h * f(x[i], z)
    */
    public static double[] backwardEulerMethod(
        BiFunction<Double, Double, Double> f,   // Function of our ODE y' = f(x, y)
        double initValue,                       // Initial value to begin approximating our solution y
        double finValue,                        // Final value to end approximating out solution y
        double initCondition,                   // Condition, such that f(initValue) = initCondition
        int n                                   // Number of sections to divide out interval into
    ) {
        double[] x = partitionInterval(initValue, finValue, n);
        double step = (initValue + finValue) / n;
        double[] y = new double[n+1];
        y[0] = initCondition;
        for (int i = 1; i <= n; i++) {
            int j = i;
            Function<Double, Double> fixedPointFunction = z -> y[j-1] + step * f.apply(x[j], z);
            y[i] = rootFinderMethod(fixedPointFunction, Newton(fixedPointFunction), 1, 1e-6, 100, false);
        }
        return y;
        
        /* Testing block for thebackward Euler method:
        BiFunction<Double, Double, Double> f = (x, t) -> x;
        double initValue = 0;
        double finValue = 1;
        double initCondition = 0;
        int n = 100;
        double[] x = CalcUtil.partitionInterval(initValue, finValue, n);
        double[] y = CalcUtil.backwardEulerMethod(f, initValue, finValue, initCondition, n);
        CalcUtil.printGraphData(x, y);
        */
        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Lineal Algebra">
    /*
    * WIP section. For now, only the solveLinearSystem is succesfuly implemented.
    */
    
    /*
    * Solves a Linear System of equations with matricial form Ax = b, where
    * A is a n x n matrix of real numbers, b is a n-dimensional real vector
    * and x is a n-dimensional array of unknowns, which will be returned
    * as solution if able to be calculated following the formula:
    * x = A^(-1) * b
    *
    * If det(A) = 0 (or close enough to 0), if A isn't a square matrix
    * or if the number of rows of A is different from b's length,
    * an array of NaN will be returned.
    */
    public static double[] solveLinearSystem(RealMatrix A, double[] b) { 
        if (Math.abs(A.det()) < 1e-10
        || !A.isSquareMatrix()
        || A.getRows() != b.length) return vectorOf(Double.NaN, b.length);
        return A.inverse().mult(RealMatrix.colVector(b)).getCol(0);
    }
    
    // </editor-fold>
    
    
}
