package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CharController {
    @Autowired
    private TranService tranService;
    @RequestMapping("workbench/chart/transacction/index.do")
    public String index(){
        return "workbench/chart/transaction/index";
    }
    @RequestMapping("workbench/chart/transaction/queryCountOfTrans.do")
    public @ResponseBody Object queryCountOfTrans(){
        List<FunnelVO> funnelVOS = tranService.queryCountOfTrans();

        return funnelVOS;

    }
}
