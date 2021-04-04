package com.leetcode.medium;

public class GasStation {

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int gasSum = 0, costSum = 0;
        for (int i = 0; i < gas.length; i++) {
            gasSum += gas[i];
            costSum += cost[i];
        }

        if (gasSum < costSum) return -1;

        int tank = 0, startIdx = 0;
        for (int i = 0; i < gas.length; i++) {
            tank += (gas[i] - cost[i]);

            if (tank < 0) {
                startIdx = i + 1;

                tank = 0;
            }
        }

        return startIdx;
    }

    public static void main(String[] args) {
        int[] gas = new int[] {1, 2, 3, 4, 5};
        int[] cost = new int[] {3, 4, 5, 1, 2};

        System.out.println(new GasStation().canCompleteCircuit(gas, cost));
    }

}
