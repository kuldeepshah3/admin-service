package com.oracle.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {

    private Long id;
    private String title;
    private String description;
    private String version;
    private List<QuestionDTO> questions;

}
