package com.insuretech.survey.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "master_document_required", schema = "insuredb")
@NamedQuery(name = "MasterDocumentRequired.findAll", query = "SELECT a FROM MasterDocumentRequired a")
public class MasterDocumentRequired implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docRequiredGenId;
	
    private String docName;
	
	@Column(length = 100)
	private String vehicleType;
	
	private Boolean active;
	
	private Timestamp createdDtm=new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	@Column(length = 100)
	private String createdBy;
	@Column(length = 100)
	private String updatedBy;
	
}
