package com.oracle.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.demo.entity.SurveyDtl;
import com.oracle.demo.exception.ValidationException;
import com.oracle.demo.model.SurveyDTO;
import com.oracle.demo.service.AdminService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Return list of surveys
     *
     * @param username username
     * @return list of surveys
     * @throws JsonProcessingException Potential exception while converting object to string
     */
    @GetMapping(value = "/list")
    public String listSurveys(String username) throws JsonProcessingException {
        List<SurveyDtl> surveyList = adminService.listSurveys();
        return objectMapper.writeValueAsString(surveyList);
    }

    /**
     * Create new survey and Create new version for a survey
     *
     * @param surveyData survey details
     * @param request    HttpServletRequest
     * @return "SUCCESS" if survey is created successfully
     */
    @PostMapping(value = "/create")
    public String createSurvey(@RequestBody SurveyDTO surveyData, HttpServletRequest request) {
        String username = request.getParameter("username");
        if (Strings.isBlank(username)) {
            throw new ValidationException("User information not found");
        }
        return adminService.createSurvey(surveyData, username);
    }

    /**
     * Delete a survey version, delete survey if this is last version
     *
     * @param surveyId Survey version ID
     * @param request  HttpServletRequest
     * @return "SUCCESS" if survey is created successfully
     */
    @DeleteMapping(value = "/delete")
    public String deleteSurvey(Long surveyId, HttpServletRequest request) {
        String username = request.getParameter("username");
        if (Strings.isBlank(username)) {
            throw new ValidationException("User information not found");
        }
        return adminService.deleteSurvey(surveyId);
    }

}
