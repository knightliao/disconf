package com.baidu.dsp.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.ub.common.db.DaoPageResult;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * Service层工具
 *
 * @author liaoqiqi
 * @version 2014-2-20
 */
public class ServiceUtil {

    /**
     * 将结果进行转化
     *
     * @return
     */
    public static <ENTITIFROM, ENTITYTO> DaoPageResult<ENTITYTO> getResult(DaoPageResult<ENTITIFROM> daoPageFrom,
                                                                           DataTransfer<ENTITIFROM,
                                                                                           ENTITYTO> dataTransfer) {

        List<ENTITYTO> entitytos = new ArrayList<ENTITYTO>();

        //
        // 转换
        //
        for (ENTITIFROM entitifrom : daoPageFrom.getResult()) {

            ENTITYTO entityto = dataTransfer.transfer(entitifrom);
            entitytos.add(entityto);
        }

        //
        // result
        //
        DaoPageResult<ENTITYTO> daoPageResult = new DaoPageResult<ENTITYTO>();

        daoPageResult.setResult(entitytos);
        daoPageResult.setTotalCount(daoPageFrom.getTotalCount());

        return daoPageResult;
    }

    /**
     * 转换成Map
     *
     * @param enTs
     *
     * @return
     */
    public static <T extends BaseObject<Long>> Map<Long, T> conver2Map(List<T> enTs) {

        Map<Long, T> map = new HashMap<Long, T>();

        if (enTs == null) {
            return map;
        }

        for (T t : enTs) {

            map.put(t.getId(), t);
        }

        return map;
    }

}
