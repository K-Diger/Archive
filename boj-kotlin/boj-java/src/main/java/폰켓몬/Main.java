package 폰켓몬;

import java.util.HashSet;

public class Main {

    private static int[] nums = {3, 1, 2, 3};

    public static void main(String[] args) {
        int max = nums.length / 2;

        // 중복 제거하기
        HashSet<Integer> numsSet = new HashSet<>();

        for (int num : nums) {
            numsSet.add(num);
        }

        // 중복을 제거한 셋의 크기가 max보다 크면 max를, 작으면 numsSet의 size를 리턴
        System.out.println(Math.min(numsSet.size(), max));
    }
}
