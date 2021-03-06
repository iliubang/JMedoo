package cn.iliubang.jmedoo.annotation;


import cn.iliubang.jmedoo.sharding.ShardingStrategyInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {Insert class description here}
 *
 * @author <a href="mailto:it.liubang@gmail.com">liubang</a>
 * @version $Revision: {Version} $ $Date: 2018/5/24 13:45 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value();

    String[] keys() default {};

    String separator() default "_";

    Class<? extends ShardingStrategyInterface> shardingStrategy() default ShardingStrategyInterface.class;
}
