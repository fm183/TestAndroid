package com.example.testandroid.utils;

import java.util.Collection;
import java.util.List;

public class CollectionsUtils {

    public static boolean isEmpty(Collection collection){
        return collection == null || collection.isEmpty();
    }

    public static int getCount(Collection collection){
        return isEmpty(collection) ? 0 : collection.size();
    }

    /**
     * 是否是无效下标
     * @param collection 集合
     * @param position 下标
     * @return 是否是无效下标 true 无效，false 有效
     */
    public static boolean isInValidPosition(Collection collection, int position){
        if (position < 0) {
            return true;
        }
        return position >= getCount(collection);
    }

    public static <T> T getItem(List<T> list, int position){
        try {
            boolean isValidPosition = isInValidPosition(list,position);
            return isValidPosition ? null : list.get(position);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean contains(int[] array,int value){
        for (int i : array){
            if (i == value) {
                return true;
            }
        }
        return false;
    }

}
