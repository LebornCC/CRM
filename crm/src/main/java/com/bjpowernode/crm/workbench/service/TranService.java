package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Transaction;
import com.bjpowernode.crm.workbench.domain.TransactionRemark;

import java.util.List;
import java.util.Map;

public interface TranService {

    void saveCreateTrans(Map<String,Object> map);

    Transaction queryTransForDetailById(String id);

    List<FunnelVO> queryCountOfTrans();


}
