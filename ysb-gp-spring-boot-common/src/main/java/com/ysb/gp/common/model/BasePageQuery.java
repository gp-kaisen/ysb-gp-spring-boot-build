package com.ysb.gp.common.model;

import lombok.Data;

/**
 * 带分页的查询对象基类
 */
@Data
public abstract class BasePageQuery {
    /**
     * 查询对象
     */
    private PageQuery pageQuery;
}
