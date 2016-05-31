package com.baidu.unbiz.common.genericdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.knightliao.apollo.utils.common.StringUtil;

/**
 * 表示主键名的注释，添加<code>@Inherited</code>以便零注解
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * 在哪个DB中
     *
     * @return
     */
    String db();

    /**
     * 表名 添加默认值方便<code>@Inherited</code>后零注解
     *
     * @return
     */
    String name() default "";

    /**
     * 该表做了多少个切片拆分
     *
     * @return
     */
    int shardCount() default 0;

    /**
     * 主键字段的名字
     *
     * @return
     */
    String keyColumn() default "id";

    /**
     * 加一个全局的字段修改标志，默认<code>false</code>向前兼容
     *
     * @return
     */
    boolean columnsModified() default false;

    /**
     * 映射规则
     *
     * @return
     */
    ColumnStyle columnStyle() default ColumnStyle.LOWER_CASE;

    public static enum ColumnStyle {

        LOWER_CASE {
            @Override
            public String convert(String field) {
                return field.toLowerCase();
            }
        },
        /**
         * 驼峰 --> 下划线
         * <p/>
         * <pre>
         * aBc-- &gt; a_bc
         * </pre>
         */
        JAVA_TO_MYSQL {
            @Override
            public String convert(String field) {
                return StringUtil.toLowerCaseWithUnderscores(field);
            }
        };

        public abstract String convert(String field);

    }

    /**
     * 是否为视图
     */
    boolean isView() default false;
}
