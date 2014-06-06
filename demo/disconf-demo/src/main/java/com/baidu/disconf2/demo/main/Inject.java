package com.baidu.disconf2.demo.main;

import java.lang.reflect.Field;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.demo.model.ConfA;

/**
 * 
 * @author liaoqiqi
 * @version 2014-5-26
 */
public class Inject {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(Inject.class);

    public static void main(String[] args) {

        inject2ConfClass(1000);
    }

    public static void inject2ConfClass(int multi) {

        //
        // 配置文件
        //
        ScanBaseUtils.getInstance().printFileItem();

        LOGGER.info("======= 配置文件项 =======");
        Set<Field> af1 = ScanBaseUtils.getInstance().getReflections()
                .getFieldsAnnotatedWith(DisconfFileItem.class);

        for (Field item : af1) {

            LOGGER.info(item.toString());
            DisconfFileItem disconfFileItem = item
                    .getAnnotation(DisconfFileItem.class);

            Class<?> itemType = item.getType();
            LOGGER.info(itemType.toString());

            try {

                item.setAccessible(true);
                int value = item.getInt(null);
                item.set(null, value + multi * 10);

            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        for (Field item : af1) {

            LOGGER.info(item.toString());
            DisconfFileItem disconfFileItem = item
                    .getAnnotation(DisconfFileItem.class);

            try {
                LOGGER.info(String.valueOf(item.get(null)));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        LOGGER.info(String.valueOf(ConfA.getVarA()));

    }
}
