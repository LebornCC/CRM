package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClueActivityRelationController {

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("workbench/clue/saveClueActivityRelationByList.do")
    public @ResponseBody Object saveClueActivityRelationByList(String[] activityId,String clueId){
        ReturnObject returnObject = new ReturnObject();
        List<ClueActivityRelation> clueActivityRelationList =new ArrayList<>();
        ClueActivityRelation clueActivityRelation = null;
        for (String s : activityId) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setActivityId(s);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtil.UUID());
            clueActivityRelationList.add(clueActivityRelation);
        }
        int ret = clueActivityRelationService.insertClueActivityRelationByActivityList(clueActivityRelationList);

        if (ret > 0){
            List<Activity> activityList = activityService.selectActivityByClueActivityRelation(activityId);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityList);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");

        }

        return returnObject;
    }

    @RequestMapping("workbench/clue/deleteClueActivityRelation.do")
    public @ResponseBody Object deleteClueActivityRelation(String clueId,String activityId){
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(clueId);
        clueActivityRelation.setActivityId(activityId);
        ReturnObject returnObject = new ReturnObject();

        int ret = clueActivityRelationService.deleteClueActivityRelation(clueActivityRelation);

        if (ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("解除关联失败");
        }
        return returnObject;
    }
}
