package com.oracle.demo.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SURVEY_QUESTION")
public class SurveyQuestion {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "surveyQuestionSeqGen", sequenceName = "SURVEY_QUESTION_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "surveyQuestionSeqGen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_VERSION_ID")
    private SurveyVersion surveyVersion;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ANSWER_TYPE")
    private String answerType;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @OneToMany(mappedBy = "surveyQuestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<SurveyAnswer> surveyAnswers;

    @OneToMany(mappedBy = "surveyQuestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<UserSurveyAnswer> userSurveyAnswers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveyVersion getSurveyVersion() {
        return surveyVersion;
    }

    public void setSurveyVersion(SurveyVersion surveyVersion) {
        this.surveyVersion = surveyVersion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<SurveyAnswer> getSurveyAnswers() {
        return surveyAnswers;
    }

    public void setSurveyAnswers(List<SurveyAnswer> surveyAnswers) {
        this.surveyAnswers = surveyAnswers;
    }

    public List<UserSurveyAnswer> getUserSurveyAnswers() {
        return userSurveyAnswers;
    }

    public void setUserSurveyAnswers(List<UserSurveyAnswer> userSurveyAnswers) {
        this.userSurveyAnswers = userSurveyAnswers;
    }
}
