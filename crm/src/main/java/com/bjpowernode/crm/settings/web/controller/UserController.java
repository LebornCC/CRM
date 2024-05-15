package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtil;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired

    private UserService userService;
    @RequestMapping("settings/qx/user/login.html")
    public String toLogin(){
        return "settings/qx/user/login";
    }


    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPWD, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            //登录失败，用户名或密码错误
            returnObject.setCode("0");
            returnObject.setMessage("用户名或密码错误");
        } else {

            if (user.getExpireTime().compareTo(DataUtil.formateDateTime(new Date())) < 0) {
                //登陆失败,用户过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("用户过期");
            } else if ("0".equals(user.getLockState())) {
                //登陆失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //登录失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            } else {
                //登陆成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(Contants.SESSEIONUSER_NAME,user);

                if ("true".equals(isRemPWD)){
                    Cookie loginAct1 = new Cookie("loginAct", user.getLoginAct());
                    loginAct1.setMaxAge(10*24*60*60);
                    response.addCookie(loginAct1);

                    Cookie loginAct2 = new Cookie("loginPwd", user.getLoginPwd());
                    loginAct1.setMaxAge(10*24*60*60);
                    response.addCookie(loginAct2);

                }else {
                    Cookie loginAct1 = new Cookie("loginAct", "1");
                    loginAct1.setMaxAge(0);
                    response.addCookie(loginAct1);

                    Cookie loginAct2 = new Cookie("loginPwd", "2");
                    loginAct2.setMaxAge(0);
                    response.addCookie(loginAct2);
                }

            }
        }
        return returnObject;
    }
    @RequestMapping("settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookie
        Cookie loginAct1 = new Cookie("loginAct", "1");
        loginAct1.setMaxAge(0);
        response.addCookie(loginAct1);

        Cookie loginAct2 = new Cookie("loginPwd", "1");
        loginAct2.setMaxAge(0);
        response.addCookie(loginAct2);
        //摧毁session

        session.invalidate();
        return "redirect:/";


    }
}
