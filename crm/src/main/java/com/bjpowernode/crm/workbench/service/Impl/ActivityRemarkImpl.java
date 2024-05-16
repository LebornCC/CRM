package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("activityRemarkService")
public class ActivityRemarkImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> selectActivityRemarkById(String id) {
        return activityRemarkMapper.selectActivityRemarkById(id);
    }
}
