package com.bjpowernode.crm.workbench.service;

import java.util.List;

public interface CustomerService {
    List<String> queryAllCustomerByName(String customerName);
}
