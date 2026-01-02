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
@Table(name = "cattel_comments", schema = "cattledb")
@NamedQuery(name = "CattleComments.findAll", query = "SELECT a FROM CattleComments a")
public class CattleComments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cattleCommentsGenId;
	
	private Long cattleIntimationGenId;
	
	@Column(name = "comments", length = 500)
	private String comments;
	
	private Timestamp createdDtm = new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
}
