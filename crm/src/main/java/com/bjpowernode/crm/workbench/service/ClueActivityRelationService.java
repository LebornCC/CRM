package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int insertClueActivityRelationByActivityList(List<ClueActivityRelation> clueActivityRelationList);
}
