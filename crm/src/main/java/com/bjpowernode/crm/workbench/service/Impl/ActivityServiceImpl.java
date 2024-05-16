package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activities;
    @Override
    public int saveCreateActivity(Activity activity) {
        return activities.insertActivity(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map<String, Object> map) {
        return activities.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectActivityByCondition(Map<String, Object> map) {
        return activities.selectActivityByCondition(map);
    }

    @Override
    public int deleteActivityById(String[] id) {
        return activities.deletActivityById(id);
    }

    @Override
    public Activity updateActivityById(String id) {
        return activities.updateActivityById(id);
    }

    @Override
    public int saveActivityByUpdate(Activity activity) {
        return activities.saveActivityByUpdate(activity);
    }

    @Override
    public List<Activity> exportAllActivity() {
        return activities.exportAllActivity();
    }

    @Override
    public int importActivity(List<Activity> activity) {
        return activities.importActivity(activity);
    }

    @Override
    public Activity queryActivityForDetail(String id) {
        return activities.queryActivityForDetail(id);
    }


}
