package com.insuretech.survey.model;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDocumentUploadModel {
	private String docUuid;
    private String docName;
    private String docType;
    private Timestamp docUploadTime;
    private String vehicleNumber;
    private String companyGenId;
    private Long insurenceGenId;
}