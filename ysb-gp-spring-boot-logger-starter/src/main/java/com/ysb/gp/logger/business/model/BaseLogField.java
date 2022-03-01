package com.ysb.gp.logger.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author laosy
 * @date 2022/2/11
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseLogField implements Serializable {
    /**
     * 业务id
     */
    private String bid;
    /**
     * 主要行为
     */
    private String action;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 诊所id
     */
    private String clinicId;
}
