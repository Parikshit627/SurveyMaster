package com.insuretech.survey.entity;

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
@Table(name = "ai_images", schema = "insuredb")
@NamedQuery(name = "AiImages.findAll", query = "SELECT a FROM AiImages a")
public class AiImages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long aiImagesGenId;
	
	private long aiComponentGenId;

	@Column(length = 100)
	private String imagesUuid;

	private Timestamp createdDtm=new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
	
}
