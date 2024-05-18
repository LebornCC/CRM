package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.TransHistory;
import com.bjpowernode.crm.workbench.domain.Transaction;
import com.bjpowernode.crm.workbench.domain.TransactionRemark;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.TransHistoryService;
import com.bjpowernode.crm.workbench.service.TransRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TransController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @Autowired
    private TransRemarkService transRemarkService;
    @Autowired
    private TranService tranService;

    @Autowired
    private TransHistoryService transHistoryService;
    @RequestMapping("workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        List<DicValue> transactionType = dicValueService.selectDicValueBytypeCode("transactionType");
        List<DicValue> stage = dicValueService.selectDicValueBytypeCode("stage");
        List<DicValue> source = dicValueService.selectDicValueBytypeCode("source");
        request.setAttribute("transactionType",transactionType);
        request.setAttribute("stage",stage);
        request.setAttribute("source",source);

        return "workbench/transaction/index";
    }
    @RequestMapping("workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        List<DicValue> transactionType = dicValueService.selectDicValueBytypeCode("transactionType");
        List<DicValue> stage = dicValueService.selectDicValueBytypeCode("stage");
        List<DicValue> source = dicValueService.selectDicValueBytypeCode("source");
        request.setAttribute("users",users);
        request.setAttribute("transactionType",transactionType);
        request.setAttribute("stage",stage);
        request.setAttribute("source",source);

        return "workbench/transaction/save";

    }

    @RequestMapping("workbench/transaction/getPosssibilityByStage.do")
    public @ResponseBody Object getPosssibilityByStage(String stageValue){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String string = bundle.getString(stageValue);
        return string;

    }

    @RequestMapping("workbench/transaction/queryCustomerNameByName.do")
    public @ResponseBody Object queryCustomerNameByName(String customerName){
        List<String> list = customerService.queryAllCustomerByName(customerName);
        return list;
    }

    @RequestMapping("workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(@RequestParam Map<String ,Object> map, HttpSession session){
        map.put(Contants.SESSEIONUSER_NAME,session.getAttribute(Contants.SESSEIONUSER_NAME));
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.saveCreateTrans(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存交易失败");
            throw new RuntimeException(e);
        }

        return returnObject;
    }

    @RequestMapping("workbench/transaction/transDetail.do")
    public String transDetail(String id,HttpServletRequest request){
        Transaction transaction = tranService.queryTransForDetailById(id);
        List<TransactionRemark> transactionRemarkList = transRemarkService.queryTransRemarkForDetailByTransId(transaction.getId());
        List<DicValue> stage = dicValueService.selectDicValueBytypeCode("stage");
        List<TransHistory> transHistories = transHistoryService.queryTransHistoryForDetailByTransId(transaction.getId());

        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(transaction.getStage());
        transaction.setPossibility(possibility);


        request.setAttribute("transHistories",transHistories);
        request.setAttribute("transaction",transaction);
        request.setAttribute("transactionRemarkList",transactionRemarkList);
        request.setAttribute("possibility",possibility);
        request.setAttribute("stage",stage);
        return "workbench/transaction/detail";
    }
}
