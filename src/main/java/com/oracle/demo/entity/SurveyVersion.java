package com.oracle.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SURVEY_VERSION")
public class SurveyVersion {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "surveyVersionSeqGen", sequenceName = "SURVEY_VERSION_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "surveyVersionSeqGen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;

    @Column(name = "VERSION")
    private String version;

    @OneToMany(mappedBy = "surveyVersion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<SurveyQuestion> surveyQuestions;

    @OneToMany(mappedBy = "surveyVersion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<UserSurvey> userSurveys;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    public List<UserSurvey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(List<UserSurvey> userSurveys) {
        this.userSurveys = userSurveys;
    }
}
