package com.baidu.unbiz.common.genericdao.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.baidu.unbiz.common.genericdao.annotation.Table.ColumnStyle;
import com.baidu.unbiz.common.genericdao.util.ClassUtils;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 一个映射项的对象
 */
public class MappingItem {

    public MappingItem(Field field, Method setterMethod, Method getterMethod, ColumnStyle columnStyle) {
        this(field, setterMethod, getterMethod, columnStyle, false);
    }

    public MappingItem(Field field, Method setterMethod, Method getterMethod, ColumnStyle columnStyle,
                       boolean columnsModified) {
        this.field = field;
        this.getter = getterMethod;
        this.setter = setterMethod;

        // 获取字段的注解，如果没有，则从getter或者setter上获取注解
        Column column = getColumnAnnotation(field, setter, getter);

        // 如果数据库映射字段不为空，则按照映射关系设置字段
        if (column == null) {
            // generate column name by ColumnStyle
            // 1:to lower case(default);2:camel to underscores;3:and may be
            // others
            this.dbColumn = columnStyle.convert(field.getName());
            modifiable = columnsModified;
        } else if (column.value().equals("ignore") || column.ignore()) {
            // ignore
            // 当annotation为ignore时，dbColumn的value为null，这时表示这个MappingItem是一个无效的item
        } else {
            this.dbColumn = column.value();
            modifiable = column.maybeModified();
        }
    }

    private String dbColumn;

    private boolean modifiable;

    private Field field;

    private Method getter;

    private Method setter;

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public Field getField() {
        return field;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * 获取某个字段的annotation，从继承链最下面获取
     *
     * @param f
     * @param set
     * @param get
     *
     * @return
     */
    static Column getColumnAnnotation(Field f, Method set, Method get) {

        // 三个地方都有可能出现column
        Column column = f.getAnnotation(Column.class);
        // FIXME 如果不遵循java bean规范，get set 可能为 null，抛出NullPointerException
        Column gColumn = get.getAnnotation(Column.class);
        Column sColumn = set.getAnnotation(Column.class);

        // 预先获取出get与set所在的类
        Class<?> sClass = set.getDeclaringClass();
        Class<?> gClass = get.getDeclaringClass();

        // 如果get上定义了annotation，且get定义的地方是子类
        if (gColumn != null && !gClass.isAssignableFrom(sClass)) {
            return gColumn;
        }

        // 如果是set上定义的annotation，且set方法不在父类中定义
        if (sColumn != null && !sClass.isAssignableFrom(gClass)) {
            return sColumn;
        }

        return column;
    }

    public static List<MappingItem> getMappingItems(Class<?> clazz) {

        if (!ClassUtils.isBaiduClass(clazz)) {
            return new ArrayList<MappingItem>(0);
        }

        // 如果不是BaseObject的子類，則一定是keyClass
        boolean isKeyClass = !BaseObject.class.isAssignableFrom(clazz);

        List<MappingItem> mappingItems = new ArrayList<MappingItem>(32);

        Set<Field> fields = ClassUtils.getAllFiled(clazz);
        Set<Method> methods = ClassUtils.getAllMethod(clazz);
        Map<String, Method> methodMap = ClassUtils.filter2Map(methods);

        Table table = isKeyClass ? null : clazz.getAnnotation(Table.class);
        String keyColumn = isKeyClass ? null : table.keyColumn();

        // 循环处理所有字段，过滤出该类加载为对象时需要调用的setter方法map
        for (Field f : fields) {
            // 静态字段则自动pass
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            // 不做关联加载的工作
            Class<?> fType = f.getType();
            boolean isBaiduClass = ClassUtils.isBaiduClass(fType);
            if (isBaiduClass || Collection.class.isAssignableFrom(fType) || fType.isArray()) {
                continue;
            }

            // 字段名字
            String name = f.getName().toLowerCase();
            boolean isKey = name.equals("id");
            if (isKey && isBaiduClass) {
                continue;
            }

            // 其他字段获取field，getter，setter
            Method set = methodMap.get("set" + name);
            Method get = methodMap.get("get" + name);
            if (get == null) {
                get = methodMap.get("is" + name);
            }
            // FIXME
            MappingItem item =
                    new MappingItem(f, set, get, (table != null) ? table.columnStyle() : ColumnStyle.LOWER_CASE,
                            (table != null) ? table.columnsModified() : false);
            if (item.isIgnore()) {
                continue;
            }
            item.dbColumn = isKey ? keyColumn : item.dbColumn;
            mappingItems.add(item);
        }
        return mappingItems;
    }

    /**
     * 判断该item是否是有效item 当property上注释为ignore时，这个接口会返回true，这时会将其在mapping中省略掉。
     *
     * @return
     */
    private boolean isIgnore() {
        return dbColumn == null;
    }

    public static void main(String[] args) {
        System.out.println(Object.class.isAssignableFrom(Integer.class));
    }
}
