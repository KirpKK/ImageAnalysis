package ru.kirpkk.image_processing;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Utils {
    public static Integer[] getSortedArray(List<Integer> colorsSet) {
        Integer[] array = new Integer[colorsSet.size()];
        Iterator iterator = colorsSet.iterator();
        for (int i = 0; i < array.length; i++) {
            array[i] = (Integer) iterator.next();
        }
        Arrays.sort(array);
        return array;
    }
}
