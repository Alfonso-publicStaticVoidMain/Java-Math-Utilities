package mainpackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class TestClass {

    public static void main(String[] args) {

        int[] intArray = new int[] {0, 1, -1, -5, 0, 5, 1, 5, 27, 81};
        
        
        System.out.println(CalcUtil.firstIndexThatSatisfiesCondition(intArray, n -> Math.abs(n) == 5));
        
        
    }
}
