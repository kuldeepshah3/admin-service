package com.oracle.demo.repository;

import com.oracle.demo.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    /**
     * Return survey details Survey version ID
     *
     * @param versionId survey version ID
     * @return Survey details
     */
    @Query(value = "SELECT s.* FROM SURVEY s INNER JOIN SURVEY_VERSION sv ON s.ID=sv.SURVEY_ID AND sv.ID=:versionId", nativeQuery = true)
    Survey findBySurveyVersion(@Param("versionId") Long versionId);

}
