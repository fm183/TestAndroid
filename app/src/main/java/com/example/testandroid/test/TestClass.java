package com.example.testandroid.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class TestClass {

    @Test
    public void test1(){
        int[] arrays = getRandomArrays();
        long startTime = System.currentTimeMillis();
        selectOrder(arrays);
        System.out.println(System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        bobbleOrder(arrays);
        System.out.println(System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        insertOrder(arrays);
        System.out.println(System.currentTimeMillis() - startTime);
    }

    private int[] getRandomArrays(){
        Random random = new Random();
        int[] arrays = new int[random.nextInt(30) + 10000];
        for (int i = 0;i < arrays.length;i ++){
            arrays[i] = random.nextInt(100);
        }
        return arrays;
    }


    private void insertOrder(int[] arrays) {
        for (int i = 1;i < arrays.length;i ++){
            for (int j = i - 1;j >= 0 && arrays[j] > arrays[j + 1];j --){
                arrays[j] = arrays[j] ^ arrays[j + 1];
                arrays[j+1] = arrays[j] ^ arrays[j + 1];
                arrays[j] = arrays[j] ^ arrays[j + 1];
            }
        }

        System.out.println(Arrays.toString(arrays));
    }

    private void bobbleOrder(int[] arrays) {
        for(int i = arrays.length - 1;i > 0;i --){
            for (int j = 0;j < i;j ++){
                if (arrays[j] > arrays[j + 1]) {
                    arrays[j] = arrays[j] ^ arrays[j + 1];
                    arrays[j + 1] = arrays[j] ^ arrays[j + 1];
                    arrays[j] = arrays[j] ^ arrays[j + 1];
                }
            }
        }
        System.out.println(Arrays.toString(arrays));
    }


    private void selectOrder(int[] arrays){

        for (int i = 0;i < arrays.length;i ++){
            int minIndex = i;
            for (int j = i + 1;j < arrays.length;j ++){
                minIndex = arrays[j ] > arrays[minIndex] ? minIndex : j;
            }
            if (minIndex != i) {
                int tmp = arrays[i];
                arrays[i] = arrays[minIndex];
                arrays[minIndex] = tmp;
            }
        }
        System.out.println(Arrays.toString(arrays));
    }


}
