package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TransHistory;

import java.util.List;

public interface TransHistoryService {
    List<TransHistory> queryTransHistoryForDetailByTransId(String transId);
}
