package com.raidiam.consents.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnumDuplicateChecker {

    /**
     * Checks if Enum list has duplicate items
     *
     * @param enumList  List
     * @return booelan  True/False
     * @param <T>       Generic type
     */
    public static <T> boolean hasDuplicateItems(List<T> enumList) {
        Set<T> seen = new HashSet<T>();
        Set<T> duplicates = new HashSet<T>();

        for (T el : enumList) {
            if (! seen.add(el)) {
                duplicates.add(el);
            }
        }

        return ! duplicates.isEmpty();
    }
}
