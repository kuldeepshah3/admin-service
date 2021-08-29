package com.oracle.demo.service;

import com.oracle.demo.entity.SurveyDtl;
import com.oracle.demo.model.SurveyDTO;

import java.util.List;

public interface AdminService {

    List<SurveyDtl> listSurveys();

    String createSurvey(SurveyDTO surveyData, String username);

    String deleteSurvey(Long surveyId);

}
