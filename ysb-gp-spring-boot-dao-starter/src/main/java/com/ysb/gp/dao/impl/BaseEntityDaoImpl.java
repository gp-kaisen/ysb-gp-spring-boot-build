package com.ysb.gp.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.ysb.gp.dao.BaseEntityDao;
import com.ysb.gp.dao.mapper.BaseEntityMapper;
import com.ysb.gp.dao.model.BaseDO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 业务实体DAO基类
 */
public abstract class BaseEntityDaoImpl<K extends Serializable, T extends BaseDO<K>, M extends BaseEntityMapper<T>>
        implements BaseEntityDao<K, T> {

    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    private M baseMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(T entity) {
        return getBaseMapper().insert(entity);
    }

    /**
     * {@inheritDoc} 暂时直接单条插入，后期优化为生成批量插入语句
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatch(Collection<T> entities) {
        int result = 0;
        for (T entity : entities) {
            result += this.insert(entity);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteById(K id) {
        return getBaseMapper().deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteBatchIds(Collection<K> ids) {
        return getBaseMapper().deleteBatchIds(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(T entity) {
        return getBaseMapper().updateById(entity);
    }

    /**
     * {@inheritDoc} 暂时直接单条更新，后期优化为生成批量更新语句
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatch(Collection<T> entities) {
        int result = 0;
        for (T entity : entities) {
            result += this.update(entity);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T selectById(K id) {
        return getBaseMapper().selectById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> listByIds(Collection<K> ids) {
        return getBaseMapper().selectBatchIds(ids);
    }

}
