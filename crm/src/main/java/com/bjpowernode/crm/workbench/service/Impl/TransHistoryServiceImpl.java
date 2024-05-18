package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.domain.TransHistory;
import com.bjpowernode.crm.workbench.mapper.TransHistoryMapper;
import com.bjpowernode.crm.workbench.service.TransHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("transHistoryService")
public class TransHistoryServiceImpl implements TransHistoryService {
    @Autowired
    private TransHistoryMapper transHistoryMapper;
    @Override
    public List<TransHistory> queryTransHistoryForDetailByTransId(String transId) {
        return transHistoryMapper.selectTransHistoryForDetailByTransId(transId);
    }
}
