package com.insuretech.survey.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(schema = "insuredb")
public class ReportDocumentUpload {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long documentId;

	private String docUuid;
    private String docName;
    private String docType;
    private Timestamp docUploadTime;
    private String vehicleNumber;
    private String companyGenId;
    private Long insurenceGenId;
}
