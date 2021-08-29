package com.oracle.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_SURVEY_ANSWER")
public class UserSurveyAnswer {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "userSurveyAnswerSeqGen", sequenceName = "USER_SURVEY_ANSWER_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSurveyAnswerSeqGen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SURVEY_ID")
    private UserSurvey userSurvey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_QUESTION_ID")
    private SurveyQuestion surveyQuestion;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "ANSWER_DATE")
    private Date answerDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserSurvey getUserSurvey() {
        return userSurvey;
    }

    public void setUserSurvey(UserSurvey userSurvey) {
        this.userSurvey = userSurvey;
    }

    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }
}
