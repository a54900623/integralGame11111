package com.party.game.common.annotation;

import java.lang.annotation.*;

/**
 * 签名忽略字段
 * @author yifeng
 * @version 2016/7/13 0013
 */

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ignore {
}
