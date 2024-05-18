package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

public interface ClueService {
    int insertCreateClue(Clue clue);

    Clue selectClueForDetailById(String id);

    void saveConvertClue(Map<String,Object> map);
}
