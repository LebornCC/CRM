package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.domain.TransactionRemark;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.bjpowernode.crm.workbench.service.TransRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TransRemarkService")
public class TransRemarkServiceImpl implements TransRemarkService {

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;
    @Override
    public List<TransactionRemark> queryTransRemarkForDetailByTransId(String transId) {
        return transactionRemarkMapper.selectTranRemarkForDetailByTransId(transId);
    }
}
