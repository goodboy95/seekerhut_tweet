package com.seekerhut.service;

import java.util.Arrays;

public class TestService {

    Function<Integer, Integer, Boolean> needSwap = (cur, base, rightAndAscend) -> rightAndAscend ? cur < base : cur > base;

    public void qsort(String nums) {
        Integer[] numArr = Arrays.stream(nums.split(",")).map(Integer::parseInt).toArray(Integer[] :: new);
        qsort(numArr, 0, numArr.length - 1, true);
    }

    private void qsort(Integer[] nums, int head, int tail, boolean isAscend) {
        int h = head, t = tail;
        if (h >= t) {
            return;
        }
        int base = nums[head];
        while (t > head) {
            if ((boolean)needSwap.apply(nums[t], base, isAscend)) {
                while (h < t) {
                    h++;
                    if ((boolean)needSwap.apply(nums[h], base, !isAscend)) {
                        int tmp = nums[h];
                        nums[h] = nums[t];
                        nums[t] = tmp;
                        break;
                    }
                }
                if (h >= t) {
                    break;
                }
            }
            t--;
        }
        int tmp = nums[head];
        nums[head] = nums[t];
        nums[t] = tmp;
        qsort(nums, head, t - 1, isAscend);
        qsort(nums, t + 1, tail, isAscend);
    }

    @FunctionalInterface
    private interface Function<T1, T2, U> {
        public Object apply(T1 t1, T2 t2, U u);
    }
}