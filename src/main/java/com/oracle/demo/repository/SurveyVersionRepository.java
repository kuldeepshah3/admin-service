package com.oracle.demo.repository;

import com.oracle.demo.entity.SurveyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyVersionRepository extends JpaRepository<SurveyVersion, Long> {

    /**
     * Return all versions of a survey
     *
     * @param id Survey ID
     * @return List of Survey versions
     */
    @Query("from SurveyVersion where survey.id=:id")
    List<SurveyVersion> findSurveyVersions(@Param("id") Long id);

    /**
     * Check if a survey version already exists
     *
     * @param id      Survey ID
     * @param version Survey Version
     * @return Number of surveys existing with same survey id and version
     */
    @Query("select count(1) from SurveyVersion where survey.id=:id and version=:version")
    Long checkSurveyVersionExistence(@Param("id") Long id, @Param("version") String version);

    /**
     * Return number of survey versions for a survey id
     *
     * @param id survey id
     * @return Number of survey versions for a survey id
     */
    @Query("select count(1) from SurveyVersion where survey.id=:id")
    Long getVersionCount(@Param("id") Long id);
}
