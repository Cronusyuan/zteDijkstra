package com.cronus;

public class Main {

    public static void main(String[] args) {
        long now = System.currentTimeMillis();

        String filePath = "/home/cronusyuan/input";
        String[] graphContent = FileUtil.read(filePath, null);
        Graph graph = new Graph(graphContent);
        Solution solution = new Solution(graph);
        solution.result();

        System.out.println("耗时：" + (System.currentTimeMillis() - now) + "ms");
    }
}
