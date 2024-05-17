package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> selectActivityByConditionForPage(Map<String ,Object> map);

    int selectActivityByCondition(Map<String ,Object> map);

    int deleteActivityById(String[] id);

    Activity updateActivityById(String id);

    int saveActivityByUpdate(Activity activity);

    List<Activity> exportAllActivity();

    int importActivity(List<Activity> activities);

    Activity queryActivityForDetail(String id);

    List<Activity> selectActivityForDetailByCludeId(String clueId);

    List<Activity> selectActivityForDetailByNameClueId(Map<String,Object> map);

    List<Activity> selectActivityByClueActivityRelation(String[] id);


}
