package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TransactionRemark;

import java.util.List;

public interface TransRemarkService {
    List<TransactionRemark> queryTransRemarkForDetailByTransId(String transId);
}
