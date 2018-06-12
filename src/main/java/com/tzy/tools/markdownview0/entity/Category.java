package com.tzy.tools.markdownview0.entity;

import lombok.Data;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author lipu@csii.com.cn
 */
@Data
public class Category implements Comparable<Category>{
    /**
     * 节点名
     */
    private String name;
    /**
     * 节点id
     */
    private int id;
    /**
     * 节点表示的路径
     */
    private String path;
    /**
     * 是否是文件夹
     */
    private Boolean isDir = false;
    /**
     * 节点
     */
    private Set<Category> children;
    /**
     * 是否展开
     */
    private boolean spread;
    public Category() {
        this.children = new TreeSet<>();
    }
    /**
     * 使得文件夹排序在前面,treeSet为递增排序
     */
    @Override
    public int compareTo(Category o) {
        //目录优先
        if (!this.isDir && o.isDir){
            return 1;
        }
        if (this.isDir && !o.isDir){
            return -1;
        }
        //并列的相同类型元素自然序
        return 1;
    }
}
