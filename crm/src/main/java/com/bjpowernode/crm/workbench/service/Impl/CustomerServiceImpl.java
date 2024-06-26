package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public List<String> queryAllCustomerByName(String customerName) {
        return customerMapper.selectAllCustomer(customerName);
    }
}
