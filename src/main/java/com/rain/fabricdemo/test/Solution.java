package com.rain.fabricdemo.test;

import java.util.*;

public class Solution{
    public static int N;
    public static int M;
    public static int H;
    public static int W;
    public static int X;
    public static int Y;
    public static int[][] directions=new int[][]{{1,0},{-1,0},{0,1},{0,-1}};
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        N=sc.nextInt();
        M=sc.nextInt();
        H=sc.nextInt();
        W=sc.nextInt();
        X=sc.nextInt();
        Y=sc.nextInt();
        int[][] box=new int[N][M];
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                box[i][j]=sc.nextInt();
            }
        }
        for(int i=X;i<X+H;i++){
            for(int j=Y;j<Y+W;j++){
                box[i][j]=0;
            }
        }
        int ans=bfs(X,Y,box);
        System.out.println(ans);
    }
    public static int bfs(int x,int y,int[][] box){
        Queue<int[]> queue=new LinkedList<>();
        queue.offer(new int[]{x,y});
        int step=0;
        while(!queue.isEmpty()){
            int size=queue.size();
            System.out.println(size);
            for(int i=0;i<size;i++){
                int[] cur=queue.poll();
                if(cur[0]==0 || cur[0]+H==N-1 || cur[1]==0 ||cur[1]+W==M-1) return step;
                for(int[] target:directions){
                    if(canMove(cur[0],cur[1],box,target)) queue.offer(new int[]{cur[0]+target[0],cur[1]+target[1]});
                }
            }
            step++;
        }
        return step;
    }
    public static boolean canMove(int x,int y,int[][] box,int[] target){
        int targetX=x+target[0];
        int targetY=y+target[1];
        if(targetX<0 || targetX >=N || targetY<0 || targetY >=M || targetX+H>=N || targetY+W>=M) return false;
        //说明上下移动
        if(target[0]!=0){
            if(target[0]==1){
                for(int i=y;i<W;i++){
                    if(box[targetX+H][i]==1) return false;
                }
                return true;
            }
            else{
                for(int i=y;i<W;i++){
                    if(box[targetX][i]==1) return false;
                }
                return true;
            }
        }
        //说明左右移动
        else{
            if(target[0]==1){
                for(int i=x;i<H;i++){
                    if(box[i][targetY+W]==1) return false;
                }
                return true;
            }
            else{
                for(int i=x;i<H;i++){
                    if(box[i][targetY]==1) return false;
                }
                return true;
            }
        }

    }
}