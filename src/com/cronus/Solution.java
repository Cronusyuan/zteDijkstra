package com.cronus;

/**
 * Created by cronusyuan on 17-4-24.
 * 输入文件格式：
 * 有n个节点，m条边，节点默认编号0~n-1，起始节点编号vS，结束节点编号vE，x个必须经过节点，y个必须经过边，z个不能经过边
 * 形式如下：
 * n m vS vE x y z
 * 0 0 1 3
 * 1 0 2 5
 * ……       共m行边信息，分别为编号、起始点、终止点、花费
 * m-1 5 6 10
 * 5 9      x个数，代表必须经过的x个节点，第m+1行
 * 1 10     y个数，代表必须经过的y条边，第m+2行
 * 6        z个数，代表不能经过的z条边，第m+3行
 */
public class Solution {
    private class Edge{
        private int vertexId1;
        private int vertexId2;
        private int cost;

        private Edge(){}
        public Edge(int v1, int v2, int c){
            this.vertexId1 = v1;
            this.vertexId2 = v2;
            this.cost = c;
        }

        public int getVertexId1(){return vertexId1;}
        public int getVertexId2(){return vertexId2;}
        public int getCost(){return cost;}
    }

    private int vertexes;
    private Edge[] edges;

    private int startVertex;
    private int endVertex;
    private int[] targetVertexes;
    private Edge[] bonusEdges;
    private Edge[] forbiddenEdges;

    private static final int LIMIT = 9;

    private Solution(){}
    public Solution(String[] input){
        String[] basicInfo = input[0].split(" ");
        vertexes = Integer.parseInt(basicInfo[0]);
        startVertex = Integer.parseInt(basicInfo[2]);
        endVertex = Integer.parseInt(basicInfo[3]);
        int edgeNum = Integer.parseInt(basicInfo[1]), targetNum = Integer.parseInt(basicInfo[4]);
        int bonusNum = Integer.parseInt(basicInfo[5]), forbiddenNum = Integer.parseInt(basicInfo[6]);
        edges = new Edge[edgeNum];
        targetVertexes = new int[targetNum];
        bonusEdges = new Edge[bonusNum];
        forbiddenEdges = new Edge[forbiddenNum];

        for(int i = 0; i < edges.length; i++){
            String[] thisInfo = input[i + 1].split(" ");
            int v1 = Integer.parseInt(thisInfo[1]), v2 = Integer.parseInt(thisInfo[2]), cost = Integer.parseInt(thisInfo[3]);
            Edge edge = new Edge(v1, v2, cost);
            edges[i] = edge;
        }

        String[] targetVertexsInfo = input[edgeNum + 1].split(" ");
        for(int i = 0; i < targetVertexes.length; i++){
            targetVertexes[i] = Integer.parseInt(targetVertexsInfo[i]);
        }

        String[] bonusEdgesInfo = input[edgeNum + 2].split(" ");
        for(int i = 0; i < bonusEdges.length; i++){
            bonusEdges[i] = edges[Integer.parseInt(bonusEdgesInfo[i])];
        }

        String[] forbiddenEdgesInfo = input[edgeNum + 3].split(" ");
        for(int i = 0;i < forbiddenEdges.length; i++){
            forbiddenEdges[i] = edges[Integer.parseInt(forbiddenEdgesInfo[i])];
        }
    }
}
