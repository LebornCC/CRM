package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
public class DicValueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @RequestMapping("workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        List<DicValue> appellation = dicValueService.selectDicValueBytypeCode("appellation");//称呼
        List<DicValue> clueState = dicValueService.selectDicValueBytypeCode("clueState");//线索状态
        List<DicValue> source = dicValueService.selectDicValueBytypeCode("source");//线索来源

        request.setAttribute("users",users);
        request.setAttribute("appellation",appellation);
        request.setAttribute("clueState",clueState);
        request.setAttribute("source",source);

        return "workbench/clue/index";
    }
}
