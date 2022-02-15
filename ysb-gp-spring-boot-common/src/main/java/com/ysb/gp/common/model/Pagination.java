package com.ysb.gp.common.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Data;

/**
 * 分页数据类
 */
@Data
public class Pagination<T> implements Serializable {
    private static final long serialVersionUID = 133639905663041623L;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    // @JsonSerialize(using = LongTypeSerializer.class)
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    // @JsonSerialize(using = LongTypeSerializer.class)
    private long size = 20;

    /**
     * 当前页
     */
    // @JsonSerialize(using = LongTypeSerializer.class)
    private long current = 1;

}
