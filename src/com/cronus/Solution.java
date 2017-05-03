package com.cronus;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by cronusyuan on 17-5-2.
 * Graph-传入的图，包含邻接矩阵信息与约束信息
 * prev-前驱节点，prev[i][j]表示i为起点时j的前驱节点
 * dist-距离矩阵，dist[i][j]表示i为起点时j到i的最短距离
 */
final class Solution {
    private Integer[][] prev;
    private Integer[][] dist;
    private Integer[][] graph;
    private int limit;
    private int[] targets;
    private String[] edges;
    private int[] bonuses;
    private int[] forbiddens;
    private int defaultVS;
    private int defaultVE;

    private class Constraint{
        private boolean vertex;
        private int val;
        private int val2;
        private int distance;
        Constraint(int input){
            vertex = true;
            val = input;
        }
        Constraint(int input1, int input2, int dis){
            vertex = false;
            val = input1;
            val2 = input2;
            distance = dis;
        }
        boolean isVertex(){return vertex;}
        int getVal(){return val;}
        int getVal2(){return !vertex ? val2 : val;}
        int getDistance(){return  !vertex ? distance : 0;}
        void reverseVal(){
            if(!vertex){
                int tmp = val;
                val = val2;
                val2 = tmp;
            }
        }
    }

    Solution(Graph input){
        graph = input.getGraph();
        limit = input.getLimit();
        targets = input.getTargetVertexes();
        edges = input.getEdges();
        bonuses = input.getBonusEdges();
        forbiddens = input.getForbiddenEdges();
        defaultVS = input.getVertexStart();
        defaultVE = input.getVertexEnd();

        prev = new Integer[graph.length][graph.length];
        dist = new Integer[graph.length][graph.length];

        forbidEdges();
        for(int i = 0;i < graph.length; i++)
            dijkstraAll(i);
    }

    private void forbidEdges(){
        for(int i = 0; i < forbiddens.length; i++){
            String[] info = edges[forbiddens[i]].split(" ");
            int v1 = Integer.parseInt(info[0]), v2 = Integer.parseInt(info[1]);
            graph[v1][v2] = null;
            graph[v2][v1] = null;
        }
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
                for(int j = 0; j < flag.length; j++)
                    flag[j] = true;
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

    void result(){
        Constraint start = new Constraint(defaultVS);
        Constraint end = new Constraint(defaultVE);
        LinkedList<Constraint> constraints = new LinkedList<>();
        for(int target : targets){
            Constraint cons = new Constraint(target);
            constraints.add(cons);
        }
        for(int bonus : bonuses){
            String[] info = edges[bonus].split(" ");
            Constraint cons = new Constraint(Integer.parseInt(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]));
            constraints.add(cons);
        }
        LinkedList<Integer> abandoned = new LinkedList<>();
        LinkedList<Constraint> linkedCons = linkedConstraints(constraints, abandoned);
        if(linkedCons == null){
            System.out.println("起点与终点不连通！");
        }
        else {
            LinkedList<LinkedList<Constraint>> all = new LinkedList<>();
            allPermutations(all, linkedCons.toArray(new Constraint[linkedCons.size()]), 0);
            for (LinkedList<Constraint> one : all) {
                one.addFirst(start);
                one.add(end);
            }
            LinkedList<Constraint> bestPath = pathSelect(all);
            LinkedList<Integer> path = pathDetail(bestPath);
            System.out.println("从" + path.get(0) + "到" + path.get(path.size() - 1) + "的满足全约束最短路径：");
            System.out.println(path);
            if(abandoned.size() != 0)
                System.out.println("因不连通舍去的约束：" + abandoned);
            String flag = path.size() > limit ? "【不" : "【";
            System.out.println("总结点数：" + path.size() + flag + "满足节点数要求" + limit + "】");
            System.out.println("总路径长度：" + distance(bestPath));
        }
    }

    private void allPermutations(LinkedList<LinkedList<Constraint>> res, Constraint[] cons, int index){
        if(index >= cons.length){
            int[] pos = new int[bonuses.length];
            int cur = 0;
            for(int i = 0; i < cons.length; i++){
                if(!cons[i].isVertex()){
                    pos[cur] = i;
                    cur++;
                }
            }
            for(int i = 0; i < Math.pow(2, bonuses.length); i++){
                boolean[] flag = new boolean[bonuses.length];
                int num = i;
                for(int j = 0; j < flag.length; j++){
                    if(num % 2 == 0)
                        flag[j] = false;
                    else
                        flag[j] = true;
                    num /= 2;
                }
                Constraint[] tmp = Arrays.copyOf(cons, cons.length);
                for(int j = 0; j < flag.length; j++){
                    if(flag[j])
                        tmp[pos[j]].reverseVal();
                }
                res.add(new LinkedList<>(Arrays.asList(tmp)));
            }
        }
        else{
            for(int i = index; i < cons.length; i++){
                Constraint tmp = cons[index];
                cons[index] = cons[i];
                cons[i] = tmp;
                allPermutations(res, cons, index + 1);
                Constraint tmp2 = cons[index];
                cons[index] = cons[i];
                cons[i] = tmp2;
            }
        }
    }

    private int distance(LinkedList<Constraint> path){
        int result = 0;
        for(int i = 0; i < path.size() - 1; i++){
            Constraint cons = path.get(i);
            Constraint next = path.get(i + 1);
            if(cons.isVertex()){
                result += dist[cons.getVal()][next.getVal()];
            }
            else{
                result += cons.getDistance();
                result += dist[cons.getVal2()][next.getVal()];
            }
        }
        return result;
    }

    private LinkedList<Constraint> linkedConstraints(LinkedList<Constraint> allConstraints, LinkedList<Integer> abandoned){
        LinkedList<Constraint> result = new LinkedList<>();
        if(dist[defaultVS][defaultVE] == null)
            return null;
        else{
            for(int i = 0; i < allConstraints.size(); i++){
                if(dist[allConstraints.get(i).getVal()][defaultVE] != null) {
                    result.add(allConstraints.get(i));
                }
                else{
                    abandoned.add(allConstraints.get(i).getVal());
                    if(!allConstraints.get(i).isVertex())
                        abandoned.add(allConstraints.get(i).getVal2());
                }
            }
        }
        return result;
    }

    private LinkedList<Constraint> pathSelect(LinkedList<LinkedList<Constraint>> paths){
        int min = distance(paths.get(0)), pos = 0;
        for(int i = 1; i < paths.size(); i++){
            int temp = distance(paths.get(i));
            if(temp < min){
                min = temp;
                pos = i;
            }
        }
        return paths.get(pos);
    }

    private LinkedList<Integer> pathDetail(LinkedList<Constraint> path){
        LinkedList<Integer> result = new LinkedList<>();
        Integer node = path.get(0).getVal();
        for(int i = 0; i < path.size() - 1; i++){
            Constraint now = path.get(i);
            Constraint next = path.get(i + 1);
            if(!now.isVertex()){
                result.add(now.getVal());
                node = now.getVal2();
            }
            while(node != next.getVal()){
                result.add(node);
                node = prev[next.getVal()][node];
            }
        }
        result.add(path.get(path.size() - 1).getVal());
        return result;
    }
}
