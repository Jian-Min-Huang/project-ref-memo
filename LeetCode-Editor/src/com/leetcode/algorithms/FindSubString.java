package com.leetcode.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindSubString {

    public static void main(String[] args) {
        System.out.println(findSubstring("dddacdddd", "abcd"));
        System.out.println(findSubstring("dddabcdddd", "abcd"));
    }

    public static int findSubstring(String source, String substring) {
        // find substring(0,1) in source or not, return array a for index
        // 0 0 0 0 1 1 1 0 0 0
        // a b c d e f g h i j
        // 0 0 0 0 e f g 0 0 0
        // 0 0 0 0 0 0 0 e f g
        // e f g

        char[] sourceChars = source.toCharArray();
        char[] sourceSub = substring.toCharArray();

        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < source.length(); i++) {
            if (sourceChars[i] == sourceSub[0]) {
                idx.add(i);
            }
        }

        if (idx.size() == 0) {
            return -1;
        }

        for (int i = 0; i < idx.size(); i++) {
            boolean isSub = true;
            for (int j = 0; j < sourceSub.length; j++) {
                if (sourceChars[idx.get(i) + j] != sourceSub[j]) {
                    isSub = false;
                }
            }

            return (isSub) ? idx.get(i) : -1;
        }

        return -1;
    }
}
