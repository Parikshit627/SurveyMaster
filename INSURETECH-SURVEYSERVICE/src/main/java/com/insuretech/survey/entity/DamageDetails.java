package com.insuretech.survey.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "damage_details", schema = "insuredb")
@NamedQuery(name = "DamageDetails.findAll", query = "SELECT a FROM DamageDetails a")
public class DamageDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sr;
	
	private String companyGenId;
	private Long insurenceGenId;
	private String workedBySurveyName;
	private String workedByUserId;

	private String partsName;
	@Column(length = 1200)
	private String description;

	private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
