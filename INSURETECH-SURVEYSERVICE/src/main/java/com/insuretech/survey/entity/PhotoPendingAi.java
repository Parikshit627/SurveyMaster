package com.insuretech.survey.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "photo_pending_ai", schema = "insuredb")
@NamedQuery(name = "PhotoPendingAi.findAll", query = "SELECT a FROM PhotoPendingAi a")
public class PhotoPendingAi implements Serializable {
		private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long photoPendingAiId;

	private String docUuid;
    private String docName;
    private String docType;
    private Timestamp docUploadTime;
    private String vehicleNumber;
    private String companyGenId;
    private String referenceNo;
    private Long insurenceGenId;
    private String uploadType;
    private String ticketId;
    
    
    
}
