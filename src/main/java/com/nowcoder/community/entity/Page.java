package com.nowcoder.community.entity;

import lombok.Data;

@Data
public class Page {

    private int current=1;
    private int limit=10;
    //数据总数
    private int rows;
    //分页路径
    private String path;

    /*
    * 获取当前页面的起始行*/
    public int getOffset(){
        return (current-1)*limit;
    }
    /*
    * 获取总页数*/
    public int getTotal(){
        if (rows % limit == 0){
            return rows/limit;
        }else {
            return rows/limit+1;
        }
    }

    /*
    *显示当前起始页码 ,只显示前后两页码*/
    public int getFrom(){
        int from = current-2;
        return Math.max(from, 1);
    }
    /*
    * 获取当前结束页码*/
    public int getTo(){
        int to = current+2;
        int total = getTotal();
        return Math.min(to, total);
    }

}
