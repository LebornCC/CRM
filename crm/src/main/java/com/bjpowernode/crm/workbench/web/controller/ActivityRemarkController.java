package com.bjpowernode.crm.workbench.web.controller;


import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("workbench/activity/saveActivityRemarkFromDetail.do")
    public @ResponseBody Object saveActivityRemarkFromDetail(ActivityRemark remark,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSEIONUSER_NAME);
        remark.setId(UUIDUtil.UUID());
        remark.setCreateTime(DataUtil.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);
        ReturnObject returnObject = new ReturnObject();

        try {
            int ret = activityRemarkService.saveActivityRemarkFromDetail(remark);
            if (ret > 0){
                returnObject.setCode(Contants.REMARK_EDIT_FLAG_EDITED);
                returnObject.setRetData(remark);
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加评论失败");
            throw new RuntimeException(e);
        }

        return returnObject;

    }
}
