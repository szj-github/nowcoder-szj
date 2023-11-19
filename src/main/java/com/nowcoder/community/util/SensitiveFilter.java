package com.nowcoder.community.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.compiler.KeywordTable;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SensitiveFilter {

    /*检测出敏感词后替换*/
    private static final String REPLACEMENT="***";

    /*初始化根节点*/
    private TreeNode rootNode = new TreeNode();

    /*服务启动的时候自动调用*/
    /*初始化敏感词前缀树*/
    @PostConstruct
    public void init(){
        try {
            InputStream inputStream =this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String Keyword;
            while ((Keyword=bufferedReader.readLine())!=null){
                /*将敏感词添加到前缀树*/
                this.addKeyword(Keyword);
            }
        }
        catch (Exception e) {
            log.info("加载敏感词失败");
            throw new RuntimeException(e);
        }
    }

    private void addKeyword(String keyword){
        TreeNode tmpNode = rootNode;

        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TreeNode subNode=tmpNode.getSubNode(c);
            if(subNode==null){
                /*初始化子节点*/
                subNode = new TreeNode();
                tmpNode.addSubNode(c,subNode);
            }
            /*指向子节点，进入下一轮循环*/
            tmpNode = subNode;
            if(i==keyword.length()-1){
                tmpNode.setKeywordEnd(true);
            }
        }

    }

    /*
    * 过滤敏感词
    * @param：text 待过虑文本*/
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        /*指针1*/
        TreeNode tmpNode = rootNode;
        /*指针2*/
        int begin = 0;
        int position = 0;
        StringBuilder stringBuilder = new StringBuilder();

        while (position<text.length()){
            char c = text.charAt(position);

            /*跳过符号*/
            if(isSymbol(c)){
                if(tmpNode==rootNode){
                    stringBuilder.append(c);
                    begin++;
                }
                /*无论符号开头或者中间，指针3都向下走一步*/
                position++;
                continue;
            }
            tmpNode = tmpNode.getSubNode(c);
            if(tmpNode==null){
                /*以begin开头的不是敏感词*/
                stringBuilder.append(text.charAt(begin));
                position = ++begin;

                /*指针3重新指向根节点*/
                tmpNode = rootNode;
            } else if (tmpNode.isKeywordEnd()) {
                stringBuilder.append(REPLACEMENT);
                begin = ++position;
                /*重新指向根节点*/
                tmpNode = rootNode;

            }else {
                position++;
            }
        }
        stringBuilder.append(text.substring(begin));
        return stringBuilder.toString();
    }
    private boolean isSymbol(Character c){
        return CharUtils.isAsciiAlphanumeric(c) && (c<0x2E80 || c>0x9FFF);
    }

    @Data
    private class TreeNode{
        /*关键词结束标识*/
        private boolean isKeywordEnd = false;

        /*当前节点的子节点(ket是下级字符，value是下级结点*/
        private Map<Character,TreeNode> subNodes = new HashMap<>();

        /*添加子节点*/
        public void addSubNode(Character c,TreeNode node){
            subNodes.put(c,node);
        }
        public TreeNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
