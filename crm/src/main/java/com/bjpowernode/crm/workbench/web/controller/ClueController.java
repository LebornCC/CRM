package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private DicValueService dicValueService;
    @RequestMapping("workbench/clue/insertCreateClue.do")
    public @ResponseBody Object insertCreateClue(HttpSession session,Clue clue){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSEIONUSER_NAME);
        clue.setId(UUIDUtil.UUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DataUtil.formateDateTime(new Date()));
        try {
            int ret = clueService.insertCreateClue(clue);
            if(ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存线索失败");
            throw new RuntimeException(e);
        }
        return returnObject;
    }

    @RequestMapping("workbench/clue/queryClueForDetailById.do")
    public String queryClueForDetailById(String id, HttpServletRequest request){
        Clue clue = clueService.selectClueForDetailById(id);
        List<ClueRemark> clueRemarks = clueRemarkService.selectClueRemarkForDetailByClueId(clue.getId());
        List<Activity> activityList = activityService.selectActivityForDetailByCludeId(clue.getId());


        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarks",clueRemarks);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }
    @RequestMapping("workbench/clue/selectActivityForDetailLine.do")
    public @ResponseBody Object selectActivityForDetailLine(String activityName,String clueId){

        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.selectActivityForDetailByNameClueId(map);


        return activityList;
    }
    @RequestMapping("workbench/clue/ClueTransformer.do")
    public String ClueTransformer(String id,HttpServletRequest request){
        Clue clue = clueService.selectClueForDetailById(id);

        List<DicValue> stage = dicValueService.selectDicValueBytypeCode("stage");

        request.setAttribute("clue",clue);
        request.setAttribute("stage",stage);

        return "workbench/clue/convert";

    }
    @RequestMapping("workbench/clue/selectActivityForConvert.do")
    public @ResponseBody Object selectActivityForConvert(String activityName,String clueId){
        Map<String,Object> map =new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForConvertByNameClue(map);
        return activityList;
    }
    @RequestMapping("workbench/clue/convertClue.do")
    public @ResponseBody Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        User user = (User) session.getAttribute(Contants.SESSEIONUSER_NAME);
        map.put(Contants.SESSEIONUSER_NAME,user);
        ReturnObject returnObject = new ReturnObject();
        try {
            clueService.saveConvertClue(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("转换失败请重试");
            throw new RuntimeException(e);
        }
        return returnObject;

    }
}
