package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> selectActivityRemarkById(String id);

    int saveActivityRemarkFromDetail(ActivityRemark activityRemark);

    int deleteActivityMarkById(String id);
    int updateActivityMarkById(ActivityRemark remark);
}
