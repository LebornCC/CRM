package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DataUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TransHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransHistoryMapper transHistoryMapper;
    @Override
    public void saveCreateTrans(Map<String,Object> map) {
        String name = (String) map.get("customerName");
        User user = (User) map.get(Contants.SESSEIONUSER_NAME);
        Customer customer = customerMapper.selectCustomerByName(name);
        if (customer == null){
            customer = new Customer();
            customer.setOwner(user.getId());
            customer.setName(name);
            customer.setId(UUIDUtil.UUID());
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DataUtil.formateDateTime(new Date()));
            customerMapper.insertCustomer(customer);
        }



        Transaction tran=new Transaction();
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtil.UUID());
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DataUtil.formateDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));
        transactionMapper.insertTransaction(tran);


        TransHistory tranHistory=new TransHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DataUtil.formateDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtil.UUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());

        transHistoryMapper.insertTransHistory(tranHistory);

    }

    @Override
    public Transaction queryTransForDetailById(String id) {
        return transactionMapper.selectTransForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTrans() {
        return transactionMapper.selectCountOfTrans();
    }


}
