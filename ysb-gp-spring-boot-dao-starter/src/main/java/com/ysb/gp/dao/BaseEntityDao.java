package com.ysb.gp.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.ysb.gp.dao.model.BaseDO;

/**
 * 业务实体DAO接口定义
 */
public interface BaseEntityDao<K extends Serializable, T extends BaseDO<K>> {

    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     */
    int insert(T entity);

    /**
     * 批量插入实体对象
     * 
     * @param entities 实体对象列表
     * @return 操作结果
     */
    int insertBatch(Collection<T> entities);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    int deleteById(K id);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param ids 主键ID列表(不能为 null 以及 empty)
     */
    int deleteBatchIds(Collection<K> ids);

    /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     */
    int update(T entity);

    /**
     * 批量修改实体对象
     *
     * @param entities 实体对象列表
     */
    int updateBatch(Collection<T> entities);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    T selectById(K id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表(不能为 null 以及 empty)
     */
    List<T> listByIds(Collection<K> ids);

}
