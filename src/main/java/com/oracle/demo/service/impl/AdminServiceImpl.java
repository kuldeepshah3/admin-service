package com.oracle.demo.service.impl;

import com.oracle.demo.entity.*;
import com.oracle.demo.exception.ValidationException;
import com.oracle.demo.model.QuestionDTO;
import com.oracle.demo.model.SurveyDTO;
import com.oracle.demo.repository.SurveyRepository;
import com.oracle.demo.repository.SurveyVersionRepository;
import com.oracle.demo.service.AdminService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyVersionRepository surveyVersionRepository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    /**
     * Return list of surveys
     *
     * @return List of surveys
     */
    @Override
    public List<SurveyDtl> listSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        List<SurveyDtl> surveyDtls = new ArrayList<>();
        if (!surveys.isEmpty()) {
            surveys.forEach(survey -> {
                List<SurveyVersion> surveyVersions = survey.getSurveyVersions();
                if (surveyVersions != null && !surveyVersions.isEmpty()) {
                    surveyVersions.forEach(surveyVersion -> {
                        SurveyDtl surveyDtl = new SurveyDtl();
                        surveyDtl.setId(surveyVersion.getId());
                        surveyDtl.setTitle(survey.getTitle());
                        surveyDtl.setDescription(survey.getDescription());
                        surveyDtl.setVersion(surveyVersion.getVersion());
                        surveyDtl.setCreateDate(sdf.format(survey.getCreateDate()));
                        surveyDtl.setCreatedBy(survey.getCreatedBy());
                        surveyDtls.add(surveyDtl);
                    });
                }
            });
            surveyDtls.sort(Comparator.comparing(SurveyDtl::getId).reversed());
        }
        return surveyDtls;
    }

    /**
     * Create new survey and Create new version for a survey
     *
     * @param surveyData survey details
     * @param username   admin username
     * @return "SUCCESS" if survey is created successfully
     */
    @Override
    @Transactional
    public String createSurvey(SurveyDTO surveyData, String username) {
        Date currentDate = new Date();

        Survey survey = null;
        if (isGreaterThanZero(surveyData.getId())) {
            survey = surveyRepository.findBySurveyVersion(surveyData.getId());
            Long versionCount = surveyVersionRepository.checkSurveyVersionExistence(survey.getId(), surveyData.getVersion());
            if (isGreaterThanZero(versionCount)) {
                throw new ValidationException("Survey already exists with same version number.");
            }
        }

        if (survey == null) {
            Survey surveyByTitle = surveyRepository.findSurveyByTitleIgnoreCase(surveyData.getTitle());
            if (surveyByTitle != null) {
                throw new ValidationException("Survey already exists with same title.");
            }
            survey = new Survey();
            survey.setTitle(surveyData.getTitle());
            survey.setDescription(surveyData.getDescription());
            survey.setCreateDate(currentDate);
            survey.setCreatedBy(username);
            survey.setUpdateDate(currentDate);
            survey.setUpdatedBy(username);
        }

        List<SurveyVersion> surveyVersions = null;
        if (isGreaterThanZero(survey.getId())) {
            surveyVersions = surveyVersionRepository.findSurveyVersions(survey.getId());
        }
        if (surveyVersions == null) {
            surveyVersions = new ArrayList<>();
        }
        SurveyVersion surveyVersion = new SurveyVersion();
        surveyVersion.setSurvey(survey);
        surveyVersion.setVersion(surveyData.getVersion());

        if (CollectionUtils.isEmpty(surveyData.getQuestions())) {
            throw new ValidationException("At least one question is required to create survey.");
        }
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (QuestionDTO question : surveyData.getQuestions()) {
            String answerType = question.getAnswerType();

            SurveyQuestion surveyQuestion = new SurveyQuestion();
            surveyQuestion.setSurveyVersion(surveyVersion);
            surveyQuestion.setQuestion(question.getQuestion());
            surveyQuestion.setAnswerType(answerType);
            surveyQuestion.setCreateDate(currentDate);
            surveyQuestion.setCreatedBy(username);
            surveyQuestion.setUpdateDate(currentDate);
            surveyQuestion.setUpdatedBy(username);

            if ("single".equalsIgnoreCase(answerType) || "multiple".equalsIgnoreCase(answerType)) {
                List<SurveyAnswer> surveyAnswers = new ArrayList<>();

                String tAnswer = question.getAnswer();
                if (Strings.isBlank(tAnswer)) {
                    throw new ValidationException("Answer missing!");
                }
                String[] answers = tAnswer.split(";");
                Set<String> answerSet = new HashSet<>();
                IntStream.range(0, answers.length).forEachOrdered(i -> {
                    String answer = answers[i].trim();
                    if (Strings.isBlank(answer)) {
                        throw new ValidationException("Answer value is empty or invalid.");
                    }
                    if (answerSet.contains(answer)) {
                        throw new ValidationException("Invalid or duplicate value found in Answer field.");
                    }
                    answerSet.add(answer);
                    SurveyAnswer surveyAnswer = new SurveyAnswer();
                    surveyAnswer.setSurveyQuestion(surveyQuestion);
                    surveyAnswer.setAnswer(answer);
                    surveyAnswer.setAnswerOrder(i + 1);
                    surveyAnswers.add(surveyAnswer);
                });
                surveyQuestion.setSurveyAnswers(surveyAnswers);
            }
            surveyQuestions.add(surveyQuestion);
        }

        surveyVersion.setSurveyQuestions(surveyQuestions);
        survey.setSurveyVersions(surveyVersions);

        if (isGreaterThanZero(surveyData.getId())) {
            surveyVersionRepository.save(surveyVersion);
        } else {
            surveyRepository.save(survey);
            surveyVersionRepository.save(surveyVersion);
        }
        return "SUCCESS";
    }

    private boolean isGreaterThanZero(Long value) {
        return value != null && value > 0;
    }

    /**
     * Delete a survey version, delete survey if this is last version
     *
     * @param surveyId Survey version ID
     * @return "SUCCESS" if survey is created successfully
     */
    @Override
    @Transactional
    public String deleteSurvey(Long surveyId) {
        if (surveyId != null && surveyId > 0) {
            Survey survey = surveyRepository.findBySurveyVersion(surveyId);
            if (survey == null) {
                throw new ValidationException("Survey not found");
            }
            surveyVersionRepository.deleteById(surveyId);
            Long versionCount = surveyVersionRepository.getVersionCount(survey.getId());
            if (versionCount == null || versionCount == 0) {
                surveyRepository.delete(survey);
            }
            return "SUCCESS";
        } else {
            throw new ValidationException("Survey ID not found");
        }
    }
}