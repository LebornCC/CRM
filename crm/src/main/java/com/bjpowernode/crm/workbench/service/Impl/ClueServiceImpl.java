package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DataUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;
    @Override
    public int insertCreateClue(Clue clue) {
        return clueMapper.insertCreateClue(clue);
    }

    @Override
    public Clue selectClueForDetailById(String id) {
        return clueMapper.selectClueForDetaillById(id);
    }



    @Override
    public void saveConvertClue(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");
        String isCreateTran=(String) map.get("isCreateTran");
        User user = (User) map.get(Contants.SESSEIONUSER_NAME);
        Clue clue = clueMapper.selectClueForConvertById(clueId);

        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DataUtil.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtil.UUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());

        customerMapper.insertCustomer(customer);

        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DataUtil.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtil.UUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());

        contactsMapper.insertContact(contacts);

        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);

        if (clueRemarks != null && clueRemarks.size()>0){
            CustomerRemark customerRemark = null;
            ContactsRemark contactsRemark = null;
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            for (ClueRemark clueRemark : clueRemarks) {
                customerRemark=new CustomerRemark();
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setId(UUIDUtil.UUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemarkList.add(customerRemark);

                contactsRemark=new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setId(UUIDUtil.UUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemarkList.add(contactsRemark);
            }
        }

        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationService.selectClueActivityRelationForConvert(clueId);

        if(clueActivityRelationList!=null && clueActivityRelationList.size() >0){
            ContactsActivityRelation contactsActivityRelation=null;
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                contactsActivityRelation=new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setId(UUIDUtil.UUID());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            clueActivityRelationService.insertClueActivityRelationByActivityList(clueActivityRelationList);

        }
        if("true".equals(isCreateTran)){
            Transaction tran=new Transaction();
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DataUtil.formateDateTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtil.UUID());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));
            transactionMapper.insertTransaction(tran);
            if (clueRemarks != null && clueRemarks.size()>0){
                TransactionRemark transactionRemark = new TransactionRemark();
                List<TransactionRemark> transactionRemarkList = new ArrayList<>();
                for (ClueRemark clueRemark : clueRemarks) {
                    transactionRemark.setCreateBy(clueRemark.getCreateBy());
                    transactionRemark.setCreateTime(clueRemark.getCreateTime());
                    transactionRemark.setEditBy(clueRemark.getEditBy());
                    transactionRemark.setEditFlag(clueRemark.getEditFlag());
                    transactionRemark.setEditTime(clueRemark.getEditTime());
                    transactionRemark.setId(UUIDUtil.UUID());
                    transactionRemark.setNoteContent(clueRemark.getNoteContent());
                    transactionRemark.setTranId(tran.getId());
                    transactionRemarkList.add(transactionRemark);

                }

                transactionRemarkMapper.insertTransactionRemark(transactionRemarkList);

            }
        }

        clueRemarkMapper.deleteByPrimaryKey(clueId);

        clueActivityRelationMapper.deleteClueActivityById(clueId);

        clueMapper.deleteByPrimaryKey(clueId);







    }
}
