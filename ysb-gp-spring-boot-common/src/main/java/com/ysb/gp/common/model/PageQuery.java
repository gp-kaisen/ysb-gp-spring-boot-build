package com.ysb.gp.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 分页查询参数类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageQuery implements Serializable {

    private static final long serialVersionUID = -7804231477136647694L;

    /**
     * 排序字段信息
     */
    private List<OrderItem> orders = new ArrayList<>();

    /**
     * 每页显示条数，默认 10
     */
    private long size = 20;

    /**
     * 当前页
     */
    private long current = 1;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        /**
         * 需要进行排序的字段
         */
        private String column;

        /**
         * 是否正序排列，默认 true
         */
        private boolean isAsc = true;

        public static OrderItem asc(String column) {
            return build(column, true);
        }

        public static OrderItem desc(String column) {
            return build(column, false);
        }

        public static List<OrderItem> ascs(String... columns) {
            return Arrays.stream(columns).map(OrderItem::asc).collect(Collectors.toList());
        }

        public static List<OrderItem> descs(String... columns) {
            return Arrays.stream(columns).map(OrderItem::desc).collect(Collectors.toList());
        }

        private static OrderItem build(String column, boolean asc) {
            return new OrderItem(column, asc);
        }
    }
}
