package com.ysb.gp.dao.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 实体基类
 */
@Data
public abstract class BaseDO<K extends Serializable> {

    /**
     * 主键
     */
    private K id;
}
