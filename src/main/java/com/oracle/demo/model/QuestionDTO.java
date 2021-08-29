package com.oracle.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long seq;
    private String question;
    private String answerType;
    private String answer;

}
