package com.oracle.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "SURVEY_ANSWER")
public class SurveyAnswer {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "surveyAnswerSeqGen", sequenceName = "SURVEY_ANSWER_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "surveyAnswerSeqGen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SURVEY_QUESTION_ID")
    private SurveyQuestion surveyQuestion;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "ANSWER_ORDER")
    private Integer answerOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAnswerOrder() {
        return answerOrder;
    }

    public void setAnswerOrder(Integer answerOrder) {
        this.answerOrder = answerOrder;
    }
}
