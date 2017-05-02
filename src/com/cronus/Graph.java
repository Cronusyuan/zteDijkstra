package com.cronus;

/**
 * Created by cronusyuan on 17-4-24.
 * 输入文件格式：
 * 有n个节点，m条边，节点默认编号0~n-1，起始节点编号vS，结束节点编号vE，x个必须经过节点，y个必须经过边，z个不能经过边，限制总节点L个
 * 形式如下：
 * n m vS vE x y z L
 * 0 0 1 3
 * 1 0 2 5
 * ......      共m行边信息，分别为编号、起始点、终止点、花费
 * m-1 5 6 10
 * 5 9 ...     x个数，代表必须经过的x个节点，第m+1行
 * 1 10 ...    y个数，代表必须经过的y条边，第m+2行
 * 6 ...       z个数，代表不能经过的z条边，第m+3行
 */
public final class Graph {
    private Integer[][] graph;
    private int vertexNum;
    private int edgeNum;
    private int targetNum;
    private int bonusNum;
    private int forbbidenNum;
    private int vertexStart;
    private int vertexEnd;
    private int limit;
    private String[] edges;
    private int[] targetVertexes;
    private int[] bonusEdges;
    private int[] forbiddenEdges;

    public Graph(String[] input){
        String[] basicInfo = input[0].split(" ");
        vertexNum = Integer.parseInt(basicInfo[0]);
        edgeNum = Integer.parseInt(basicInfo[1]);
        vertexStart = Integer.parseInt(basicInfo[2]);
        vertexEnd = Integer.parseInt(basicInfo[3]);
        targetNum = Integer.parseInt((basicInfo[4]));
        bonusNum = Integer.parseInt(basicInfo[5]);
        forbbidenNum = Integer.parseInt(basicInfo[6]);
        limit = Integer.parseInt(basicInfo[7]);
        edges = new String[edgeNum];
        for(int i = 1; i <= edgeNum; i++){
            String info = input[i];
            int id = Integer.parseInt(info.split(" ")[0]);
            String thisInfo = info.split(" ")[1] + " " + info.split(" ")[2] + " " + info.split(" ")[3];
            edges[id] = thisInfo;
        }
        targetVertexes = new int[targetNum];
        for(int i = 0; i < targetNum; i++){
            targetVertexes[i] = Integer.parseInt(input[edgeNum + 1].split(" ")[i]);
        }
        bonusEdges = new int[bonusNum];
        for(int i = 0; i < bonusNum; i++){
            bonusEdges[i] = Integer.parseInt(input[edgeNum + 2].split(" ")[i]);
        }
        forbiddenEdges = new int[forbbidenNum];
        for(int i = 0; i < forbbidenNum; i++){
            forbiddenEdges[i] = Integer.parseInt(input[edgeNum + 3].split(" ")[i]);
        }

        graph = new Integer[vertexNum][vertexNum];
        for(int i = 0; i < vertexNum; i++){
            for(int j = 0; j < vertexNum; j++){
                if(i == j)
                    graph[i][j] = 0;
                else
                    graph[i][j] = null;
            }
        }
        for(int i = 0; i < edgeNum; i++){
            String[] info = edges[i].split(" ");
            int v1 = Integer.parseInt(info[0]), v2 = Integer.parseInt(info[1]), cost = Integer.parseInt(info[2]);
            graph[v1][v2] = cost;
            graph[v2][v1] = cost;
        }
        for(int i = 0; i < forbbidenNum; i++){
            String[] info = edges[forbiddenEdges[i]].split(" ");
            int v1 = Integer.parseInt(info[0]), v2 = Integer.parseInt(info[1]);
            graph[v1][v2] = null;
            graph[v2][v1] = null;
        }
    }

    public Integer[][] getGraph(){return graph;}
    public int getVertexStart(){return vertexStart;}
    public int getVertexEnd(){return  vertexEnd;}
    public int getLimit(){return limit;}
    public int[] getTargetVertexes(){return targetVertexes;}
    public int[] getBonusEdges(){return bonusEdges;}
    public String[] getEdges(){return edges;}
}
