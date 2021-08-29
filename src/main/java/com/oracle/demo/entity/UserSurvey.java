package com.oracle.demo.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER_SURVEY")
public class UserSurvey {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "userSurveySeqGen", sequenceName = "USER_SURVEY_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSurveySeqGen")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_VERSION_ID")
    private SurveyVersion surveyVersion;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "SUBMIT_DATE")
    private Date submitDate;

    @OneToMany(mappedBy = "userSurvey", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<UserSurveyAnswer> userSurveyAnswers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SurveyVersion getSurveyVersion() {
        return surveyVersion;
    }

    public void setSurveyVersion(SurveyVersion surveyVersion) {
        this.surveyVersion = surveyVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public List<UserSurveyAnswer> getUserSurveyAnswers() {
        return userSurveyAnswers;
    }

    public void setUserSurveyAnswers(List<UserSurveyAnswer> userSurveyAnswers) {
        this.userSurveyAnswers = userSurveyAnswers;
    }
}
