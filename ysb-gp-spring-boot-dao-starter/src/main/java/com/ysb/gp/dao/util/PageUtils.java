package com.ysb.gp.dao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysb.gp.common.model.PageQuery;
import com.ysb.gp.common.model.Pagination;

import org.springframework.util.CollectionUtils;

/**
 * 分页辅助工具
 */
public class PageUtils {

    /**
     * Mybatis Plus分页数据对象{@link IPage}转换为通用分页数据对象{@link Pagination}
     * 
     * @param <T> 业务实体类型
     * @param page Mybatis Plus分页数据对象{@link IPage}
     * @return 通用分页数据对象{@link Pagination}
     */
    public static <T> Pagination<T> toPagination(IPage<T> page) {
        if (page == null) {
            return null;
        }

        Pagination<T> pagination = new Pagination<>();

        pagination.setCurrent(page.getCurrent());
        List<T> list = page.getRecords();
        if (list != null) {
            pagination.setRecords(new ArrayList<>(list));
        }
        pagination.setSize(page.getSize());
        pagination.setTotal(page.getTotal());

        return pagination;
    }

    /**
     * 通用分页查询对象{@link PageQuery}转换为Mybatis Plus分页数据对象{@link IPage}
     * 
     * @param <T> 业务实体类型
     * @param pageQuery 通用分页查询对象{@link PageQuery}
     * @return Mybatis Plus分页数据对象{@link IPage}
     */
    public static <T> IPage<T> fromPageQuery(PageQuery pageQuery) {
        Page<T> result = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        if (!CollectionUtils.isEmpty(pageQuery.getOrders())) {
            List<OrderItem> orders = pageQuery.getOrders()
                    .stream()
                    .map(o -> new OrderItem(StringUtils.camelToUnderline(o.getColumn()), o.isAsc()))
                    .collect(Collectors.toList());
            result.setOrders(orders);
        }
        return result;
    }
}