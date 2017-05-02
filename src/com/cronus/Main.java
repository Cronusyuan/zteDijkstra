package com.cronus;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String filePath = "/home/cronusyuan/input";
        String[] graphContent = FileUtil.read(filePath, null);
        Graph graph = new Graph(graphContent);
        Solution solution = new Solution(graph);
        solution.result(0, 17);
    }
}
