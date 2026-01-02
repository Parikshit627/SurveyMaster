package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.ReportDocumentUpload;

@Repository
public interface ReportDocumentUploadRepo extends JpaRepository<ReportDocumentUpload, Long>{


}
