package com.oracle.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.demo.entity.Survey;
import com.oracle.demo.entity.SurveyVersion;
import com.oracle.demo.model.QuestionDTO;
import com.oracle.demo.model.SurveyDTO;
import com.oracle.demo.repository.SurveyRepository;
import com.oracle.demo.repository.SurveyVersionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SurveyRepository surveyRepository;

    @MockBean
    private SurveyVersionRepository surveyVersionRepository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    @Test
    void listSurveys_status200() throws Exception {
        Date date = new Date();
        List<Survey> surveys = new ArrayList<>();
        Survey survey = getSurvey(date);

        List<SurveyVersion> surveyVersions = new ArrayList<>();
        surveyVersions.add(getSurveyVersion(1L, "1.0", survey));
        surveyVersions.add(getSurveyVersion(2L, "1.1", survey));
        survey.setSurveyVersions(surveyVersions);
        surveys.add(survey);

        Mockito.when(surveyRepository.findAll()).thenReturn(surveys);

        mvc.perform(get("/admin/list").contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("[{\"id\":2,\"title\":\"Survey 1\",\"description\":\"Survey description 1\",\"version\":\"1.1\",\"createDate\":\"" + sdf.format(date) + "\",\"createdBy\":\"admin\"},{\"id\":1,\"title\":\"Survey 1\",\"description\":\"Survey description 1\",\"version\":\"1.0\",\"createDate\":\"" + sdf.format(date) + "\",\"createdBy\":\"admin\"}]"));
    }

    @Test
    void createSurvey_withoutUsername_status500() throws Exception {
        List<QuestionDTO> questions = new ArrayList<>();
        questions.add(QuestionDTO.builder().seq(1L).question("Question 1?").answerType("single").answer("Answer 1;Answer 2;Answer 3").build());
        questions.add(QuestionDTO.builder().seq(2L).question("Question 2?").answerType("multiple").answer("Answer 4;Answer 5;Answer 6").build());
        questions.add(QuestionDTO.builder().seq(3L).question("Question 3?").answerType("freetext").answer("").build());

        SurveyDTO surveyDTO = SurveyDTO.builder().id(1L).title("Survey 1").description("Survey description 1").version("1.0").questions(questions).build();

        mvc.perform(post("/admin/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(surveyDTO)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void createSurvey_noVersion_missingAnswer_status500() throws Exception {
        List<QuestionDTO> questions = new ArrayList<>();
        questions.add(QuestionDTO.builder().seq(1L).question("Question 1?").answerType("single").build());
        questions.add(QuestionDTO.builder().seq(2L).question("Question 2?").answerType("multiple").answer("Answer 4;Answer 5;Answer 6").build());
        questions.add(QuestionDTO.builder().seq(3L).question("Question 3?").answerType("freetext").build());

        SurveyDTO surveyDTO = SurveyDTO.builder().title("Survey 1").description("Survey description 1").version("1.0").questions(questions).build();

        Exception exception = mvc.perform(post("/admin/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyDTO))
                        .param("username", "admin"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResolvedException();
        assertNotNull(exception);
        assertEquals("Answer missing!", exception.getMessage());
    }

    @Test
    void createSurvey_noVersion_status200() throws Exception {
        List<QuestionDTO> questions = new ArrayList<>();
        questions.add(QuestionDTO.builder().seq(1L).question("Question 1?").answerType("single").answer("Answer 1;Answer 2;Answer 3").build());
        questions.add(QuestionDTO.builder().seq(2L).question("Question 2?").answerType("multiple").answer("Answer 4;Answer 5;Answer 6").build());
        questions.add(QuestionDTO.builder().seq(3L).question("Question 3?").answerType("freetext").answer("").build());

        SurveyDTO surveyDTO = SurveyDTO.builder().title("Survey 1").description("Survey description 1").version("1.0").questions(questions).build();

        mvc.perform(post("/admin/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyDTO))
                        .param("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    void createSurvey_hasVersion_sameVersion_status500() throws Exception {
        List<QuestionDTO> questions = new ArrayList<>();
        questions.add(QuestionDTO.builder().seq(1L).question("Question 1?").answerType("single").answer("Answer 1;Answer 2;Answer 3").build());
        questions.add(QuestionDTO.builder().seq(2L).question("Question 2?").answerType("multiple").answer("Answer 4;Answer 5;Answer 6").build());
        questions.add(QuestionDTO.builder().seq(3L).question("Question 3?").answerType("freetext").answer("").build());

        SurveyDTO surveyDTO = SurveyDTO.builder().id(1L).title("Survey 1").description("Survey description 1").version("1.0").questions(questions).build();

        Survey survey = getSurvey(null);

        Mockito.when(surveyRepository.findBySurveyVersion(1L)).thenReturn(survey);
        Mockito.when(surveyVersionRepository.checkSurveyVersionExistence(1L, "1.0")).thenReturn(1L);

        Exception exception = mvc.perform(post("/admin/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyDTO))
                        .param("username", "admin"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResolvedException();
        assertNotNull(exception);
        assertEquals("Survey already exists with same version number.", exception.getMessage());
    }

    @Test
    void createSurvey_hasVersion_newVersion_status200() throws Exception {
        Date date = new Date();
        List<QuestionDTO> questions = new ArrayList<>();
        questions.add(QuestionDTO.builder().seq(1L).question("Question 1?").answerType("single").answer("Answer 1;Answer 2;Answer 3").build());
        questions.add(QuestionDTO.builder().seq(2L).question("Question 2?").answerType("multiple").answer("Answer 4;Answer 5;Answer 6").build());
        questions.add(QuestionDTO.builder().seq(3L).question("Question 3?").answerType("freetext").answer("").build());

        SurveyDTO surveyDTO = SurveyDTO.builder().id(1L).title("Survey 1").description("Survey description 1").version("1.1").questions(questions).build();

        Survey survey = getSurvey(date);

        List<SurveyVersion> surveyVersions = new ArrayList<>();
        surveyVersions.add(getSurveyVersion(1L, "1.0", survey));

        Mockito.when(surveyRepository.findBySurveyVersion(survey.getId())).thenReturn(survey);
        Mockito.when(surveyVersionRepository.checkSurveyVersionExistence(survey.getId(), "1.1")).thenReturn(0L);
        Mockito.when(surveyVersionRepository.findSurveyVersions(survey.getId())).thenReturn(surveyVersions);

        mvc.perform(post("/admin/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyDTO))
                        .param("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    void deleteSurvey_withoutUsername_thenStatus500() throws Exception {
        mvc.perform(delete("/admin/delete").param("surveyId", "1"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void deleteSurvey_noSurveyId_thenStatus500() throws Exception {
        Exception exception = mvc.perform(delete("/admin/delete").param("username", "admin"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResolvedException();
        assertNotNull(exception);
        assertEquals("Survey ID not found", exception.getMessage());
    }

    @Test
    void deleteSurvey_noSurvey_thenStatus500() throws Exception {
        Mockito.when(surveyRepository.findBySurveyVersion(1L)).thenReturn(null);

        Exception exception = mvc.perform(delete("/admin/delete").param("surveyId", "1").param("username", "admin"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResolvedException();
        assertNotNull(exception);
        assertEquals("Survey not found", exception.getMessage());
    }

    @Test
    void deleteSurvey_lastVersion_thenStatus200() throws Exception {
        Survey survey = new Survey();
        survey.setId(1L);

        Mockito.when(surveyRepository.findBySurveyVersion(1L)).thenReturn(survey);
        Mockito.when(surveyVersionRepository.getVersionCount(survey.getId())).thenReturn(0L);

        mvc.perform(delete("/admin/delete").param("surveyId", "1").param("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    void deleteSurvey_hasVersions_thenStatus200() throws Exception {
        Survey survey = new Survey();
        survey.setId(1L);

        Mockito.when(surveyRepository.findBySurveyVersion(1L)).thenReturn(survey);
        Mockito.when(surveyVersionRepository.getVersionCount(survey.getId())).thenReturn(1L);

        mvc.perform(delete("/admin/delete").param("surveyId", "1").param("username", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("SUCCESS"));
    }

    private SurveyVersion getSurveyVersion(Long id, String version, Survey survey) {
        SurveyVersion surveyVersion = new SurveyVersion();
        surveyVersion.setId(id);
        surveyVersion.setVersion(version);
        surveyVersion.setSurvey(survey);
        return surveyVersion;
    }

    private Survey getSurvey(Date date) {
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setTitle("Survey 1");
        survey.setDescription("Survey description 1");
        survey.setCreateDate(date);
        survey.setCreatedBy("admin");
        survey.setUpdateDate(date);
        survey.setUpdatedBy("admin");
        return survey;
    }

}
