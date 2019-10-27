package cn.edu.nju.ldchao.huatai.aop;

import cn.edu.nju.ldchao.huatai.constant.LimitTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ldchao on 2019/10/27.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LimitAnnotaiton {
    /**
     * 资源的名称
     */
    String name() default "";

    /**
     * Key的prefix
     */
    String prefix() default "LIMIT_";

    /**
     * 给定的时间段
     * 单位秒
     */
    int period();

    /**
     * 最多的访问限制次数
     */
    int count();

    /**
     * 限流类型
     */
    LimitTypeEnum limitType() default LimitTypeEnum.IP;


}
