package com.ysb.gp.common.constant;

/**
 * 通用错误编码
 */
public class ErrorCode {

   /**
    * 成功，未发生错误
    */
   public static final String SUCCESS = "40001";

   /**
    * 系统异常或其他需要报警的错误
    */
   public static final String EXCEPTION = "40002";

   /**
    * 一般错误（无需报警的错误）
    */
   public static final String ERROR = "40003";

   /**
    * 参数错误
    */
   public static final String PARAM_ERROR = "40004";

   /**
    * 该操作需要登录
    */
   public static final String NOT_LOGIN = "40020";

   /**
    * 无权限
    */
   public static final String NO_PRIVILEGE = "40030";

}
