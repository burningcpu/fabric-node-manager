package com.webank.fabric.node.manager.api.transaction;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import org.springframework.stereotype.Repository;

/**
 * interface of Transaction in db.
 */
@Repository
public interface TransactionMapper extends BaseMapper<TransactionDO> {
    // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
    boolean saveOrUpdate(TransactionDO entity, Wrapper<TransactionDO> updateWrapper);
}
