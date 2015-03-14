package com.mikhail.pokedex.misc;

import com.mikhail.pokedex.data.PokedexClasses.VarComparable;
import java.util.*;

public class QuickSort<T extends VarComparable<T>>
{
	
    public void sort(T[] inputArr, int sortBy) {

        if (inputArr == null || inputArr.length == 0) {
            return;
        }
		
        int length = inputArr.length;
        quickSort(inputArr, length,sortBy,0, length - 1);
		
    }

    private void quickSort(T[]array, int length, int sortBy, int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        T pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which 
             * is greater then the pivot value, and also we will identify a number 
             * from right side which is less then the pivot value. Once the search 
             * is done, then we exchange both numbers.
             */
            while (array[i].compareTo(pivot, sortBy) < 0) {
                i++;
            }
            while (array[j].compareTo(pivot, sortBy) > 0){
                j--;
            }
            if (i <= j) {
                exchangeObjects(array, i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(array, length, sortBy, lowerIndex, j);
        if (i < higherIndex)
            quickSort(array, length, sortBy, i, higherIndex);
    }

    private void exchangeObjects(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
