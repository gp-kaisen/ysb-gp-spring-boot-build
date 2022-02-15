package com.ysb.gp.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;

public class TreeNode<T> {

    @Getter
    private T data;

    @Getter
    private List<TreeNode<T>> children;

    public TreeNode(T data, List<TreeNode<T>> children) {
        this.data = data;
        this.children = children;
    }

    /**
     * 将数据列表转换为树
     * 
     * @param <TKey> 数据Id类型
     * @param <T> 数据类型
     * @param items 数据列表
     * @param idExtractor 获取数据Id的方法
     * @param parentIdExtractor 获取数据ParentId的方法
     * @param topParentId 数据顶级节点ParentId值
     * @return 数据的顶级节点列表
     */
    public static <TKey, T> List<TreeNode<T>> toTree(
            Collection<T> items,
            Function<T, TKey> idExtractor,
            Function<T, TKey> parentIdExtractor,
            TKey topParentId) {

        if (idExtractor == null) {
            throw new NullPointerException("id extractor must not null");
        }
        if (parentIdExtractor == null) {
            throw new NullPointerException("parent id extractor must not null");
        }

        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        Map<TKey, List<T>> childrenMap = getChildrenMap(items, parentIdExtractor);
        List<T> topItems = childrenMap.get(topParentId);
        if (topItems == null) {
            return Collections.emptyList();
        }
        List<TreeNode<T>> treeNodes = topItems.stream()
                .map(item -> toTreeNode(item, idExtractor, childrenMap))
                .collect(Collectors.toList());
        return treeNodes;
    }

    /**
     * 将数据列表转换为以ParentId为key的字典
     * 
     * @param <T> 数据类型
     * @param <TKey> 数据ParentId类型
     * @param items 数据列表
     * @param parentIdExtractor 获取ParentId的的方法
     * @return 数据字典
     */
    private static <T, TKey> Map<TKey, List<T>> getChildrenMap(
            Collection<T> items,
            Function<T, TKey> parentIdExtractor) {
        Map<TKey, List<T>> treeMap = new HashMap<>();
        items.stream().forEach(item -> {
            TKey parentId = parentIdExtractor.apply(item);
            List<T> treeItems = treeMap.get(parentId);
            if (treeItems == null) {
                treeItems = new ArrayList<>();
                treeMap.put(parentId, treeItems);
            }
            treeItems.add(item);
        });
        return treeMap;
    }

    private static <T, TKey> TreeNode<T> toTreeNode(
            T data,
            Function<T, TKey> idExtractor,
            Map<TKey, List<T>> childrenMap) {

        if (data == null) {
            throw new NullPointerException("id extractor must not null");
        }

        TKey id = idExtractor.apply(data);

        List<T> children = childrenMap.get(id);
        TreeNode<T> node = new TreeNode<>(
                data,
                children == null ? null : children.stream()
                        .map(child -> toTreeNode(child, idExtractor, childrenMap))
                        .collect(Collectors.toList()));
        return node;
    }
}
