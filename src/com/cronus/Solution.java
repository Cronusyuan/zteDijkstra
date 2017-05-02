package com.cronus;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by cronusyuan on 17-5-2.
 * Graph-传入的图，包含邻接矩阵信息与约束信息
 * prev-前驱节点，prev[i][j]表示i为起点时j的前驱节点
 * dist-距离矩阵，dist[i][j]表示i为起点时j到i的最短距离
 */
public final class Solution {
    private Integer[][] prev;
    private Integer[][] dist;
    private Integer[][] graph;
    private int limit;
    private int[] targets;
    private String[] edges;
    private int[] bonuses;
    private int defaultVS;
    private int defaultVE;

    public Solution(Graph input){
        graph = input.getGraph();
        limit = input.getLimit();
        targets = input.getTargetVertexes();
        edges = input.getEdges();
        bonuses = input.getBonusEdges();
        defaultVS = input.getVertexStart();
        defaultVE = input.getVertexEnd();

        prev = new Integer[graph.length][graph.length];
        dist = new Integer[graph.length][graph.length];

        for(int i = 0;i < graph.length; i++)
            dijkstraAll(i);
    }

    private void dijkstraAll(int vS){
        boolean[] flag = new boolean[graph.length];
        for(int i = 0; i < graph.length; i++){
            flag[i] = false;
            dist[vS][i] = graph[vS][i];
            prev[vS][i] = vS;
        }
        flag[vS] = true;
        dist[vS][vS] = 0;
        prev[vS][vS] = null;

        for(int i = 0; i < graph.length; i++){
            Integer min = null;
            Integer pos = null;
            for(int j = 0; j < graph.length; j++){
                if(!flag[j] && dist[vS][j] != null){
                    if(min == null || dist[vS][j] < min){
                        min = dist[vS][j];
                        pos = j;
                    }
                }
            }
            if(min == null){
                for(boolean f : flag)
                    f = true;
            }
            else{
                flag[pos] = true;
                for(int j = 0; j < graph.length; j++){
                    Integer temp = graph[pos][j] == null ? null : (min + graph[pos][j]);
                    if(!flag[j] && temp != null){
                        if(dist[vS][j] == null || dist[vS][j] > temp){
                            dist[vS][j] = temp;
                            prev[vS][j] = pos;
                        }
                    }
                }
            }
        }
    }

    private LinkedList<Integer> vertexConstaints(int vS, int vE){
        LinkedList<Integer> result = new LinkedList<>();

        return result;
    }
    private LinkedList<Integer> noConstrains(int vS, int vE){
        LinkedList<Integer> result = new LinkedList<>();
        Integer node = vE;
        while(node != null){
            result.addFirst(node);
            node = prev[vS][node];
        }
        return result;
    }

    public void result(){this.result(defaultVS, defaultVE);}
    public void result(int vS, int vE){
        if(dist[vS][vE] == null){
            System.out.println(vS + "节点到" + vE + "节点不连通！");
        }
        else {
            print(noConstrains(vS, vE));
        }
    }

    private void print(LinkedList<Integer> path){
        if(path.size() > limit)
            System.out.println("【超出了节点数限制：" + limit + "】参考路径为：");
        System.out.println("节点" + path.get(0) + "到节点" + path.get(path.size() - 1) + "的路径寻找结果：");
        System.out.println(path + "共" + path.size() + "个节点");
        int distance = 0;
        for(int i = 0; i < path.size() - 1; i++)
            distance += dist[path.get(i)][path.get(i + 1)];
        System.out.println("总路程花费：" + distance);
    }
}
