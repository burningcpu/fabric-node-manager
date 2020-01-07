package com.webank.fabric.node.manager.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * comment method.
 */
@Slf4j
public class BeanUtils {

    /**
     * Creating objects by reflection,
     * example:   Block blockInfo = FrontUtils.getInstance(Block.class,Arrays.asList(block));
     */
    public static <T> T getInstanceByReflection(Class<T> clazz, List<Object> params) {
        try {
            Class[] paramClazzArr = params.stream().map(p -> p.getClass()).toArray(Class[]::new);
            Constructor<T> constructor = clazz.getDeclaredConstructor(paramClazzArr);
            constructor.setAccessible(true);
            return constructor.newInstance(params.toArray());
        } catch (Exception ex) {
            log.error("create instance by reflect exception", ex);
        }
        return null;
    }
}
