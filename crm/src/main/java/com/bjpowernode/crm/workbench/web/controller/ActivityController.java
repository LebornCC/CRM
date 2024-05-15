package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DataUtil;
import com.bjpowernode.crm.commons.utils.HSSFFUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userService.queryAllUsers();
        request.setAttribute("userlist", users);
        return "workbench/activity/index";
    }

    @RequestMapping("workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSEIONUSER_NAME);
        activity.setId(UUIDUtil.UUID());
        activity.setCreateTime(DataUtil.formateDate(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityService.saveCreateActivity(activity);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
            throw new RuntimeException(e);
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/selectActivityByConditionForPage.do")
    public @ResponseBody Object selectActivityByConditionForPage(String name, String owner, String startDate, String endDate
            , int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Activity> activities = activityService.selectActivityByConditionForPage(map);
        int row = activityService.selectActivityByCondition(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activities);
        retMap.put("totalRows", row);
        return retMap;
    }

    @RequestMapping("workbench/activity/deleteActivityById.do")
    public @ResponseBody Object deleteActivityById(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.deleteActivityById(id);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("后台忙，请稍等");
            throw new RuntimeException(e);
        }
        return returnObject;

    }

    @RequestMapping("workbench/activity/updateActivityById.do")
    public @ResponseBody Object updateActivityById(String id) {

        Activity activity = activityService.updateActivityById(id);
        return activity;
    }

    @RequestMapping("workbench/activity/saveActivityByUpdate.do")
    public Object saveActivityByUpdate(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSEIONUSER_NAME);
        activity.setCreateTime(DataUtil.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.saveActivityByUpdate(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
            throw new RuntimeException(e);
        }
        return returnObject;
    }

    @RequestMapping("workbench/activity/downLoad.do")
    public void downLoad(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream,charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename = abc.xls");


        FileInputStream fileInputStream = new FileInputStream("E:\\网页下载\\P020240221508433482621.xlsx");
        byte[] bt = new byte[256];
        int len = 0;
        while ((len = fileInputStream.read(bt)) != -1) {
            outputStream.write(bt, 0, len);
        }
        fileInputStream.close();
        outputStream.flush();
    }

    @RequestMapping("workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        List<Activity> activities = activityService.exportAllActivity();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("activity");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始时间");
        cell = row.createCell(4);
        cell.setCellValue("结束时间");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("编辑时间");
        cell = row.createCell(10);
        cell.setCellValue("编辑人");

        if (activities != null && activities.size() > 0) {
            for (int i = 0; i < activities.size(); i++) {
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activities.get(i).getId());
                cell = row.createCell(1);
                cell.setCellValue(activities.get(i).getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activities.get(i).getName());
                cell = row.createCell(3);
                cell.setCellValue(activities.get(i).getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activities.get(i).getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activities.get(i).getCost());
                cell = row.createCell(6);
                cell.setCellValue(activities.get(i).getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activities.get(i).getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activities.get(i).getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activities.get(i).getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activities.get(i).getEditBy());
            }
        }

//        FileOutputStream os = new FileOutputStream("E:\\网页下载\\activityList.xlsx");
//        wb.write(os);
//        os.close();
//        wb.close();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment,filename=activityList.xls");
        ServletOutputStream outputStream = response.getOutputStream();
//        FileInputStream is = new FileInputStream("E:\\网页下载\\activityList.xlsx");
//        byte[] bt = new byte[256];
//        int len = 0;
//        while ((len = is.read(bt)) != -1) {
//            outputStream.write(bt);
//        }
//        is.close();
        wb.write(outputStream);
        wb.close();
        outputStream.flush();


    }

    @RequestMapping("workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,HttpSession session) throws IOException {
        ReturnObject returnObject = new ReturnObject();
        try {
            User user = (User) session.getAttribute(Contants.SESSEIONUSER_NAME);
//            String originalFilename = activityFile.getOriginalFilename();
//            File file = new File("E:\\", originalFilename);
//            activityFile.transferTo(file);
//            FileInputStream is = new FileInputStream("E:\\" + originalFilename);
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            List<Activity> activityList = new ArrayList<>();

            for (int i = 1 ; i < sheet.getLastRowNum()+1 ; i++) {
                Activity activity = new Activity();
                activity.setId(UUIDUtil.UUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DataUtil.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
                row=sheet.getRow(i);//行的下标，下标从0开始，依次增加
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValueForStr = HSSFFUtil.getCellValueForStr(cell);
                    if(j ==0){
                       activity.setName(cellValueForStr);
                    }else if(j==1){
                        activity.setStartDate(cellValueForStr);
                    }else if(j==2){
                        activity.setEndDate(cellValueForStr);
                    }else if(j==3){
                        activity.setCost(cellValueForStr);
                    }else if(j==4){
                        activity.setDescription(cellValueForStr);
                    }
                }

                activityList.add(activity);
            }
            int ret = activityService.importActivity(activityList);
            if(ret!=0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(ret);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败");
            }

        } catch (IOException e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
            throw new RuntimeException(e);
        }
        return returnObject;
    }
}
