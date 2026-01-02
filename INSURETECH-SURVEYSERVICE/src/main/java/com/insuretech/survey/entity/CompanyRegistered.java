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
@Table(name = "company_registered", schema = "insuredb")
@NamedQuery(name = "CompanyRegistered.findAll", query = "SELECT a FROM CompanyRegistered a")
public class CompanyRegistered implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long registeredId;

	@Column(name = "company_gen_id")
	private String companyGenId;

	private Timestamp createdDate;

	private String fullName;
	private String password;
	private String emailId;
	private long phoneNo;
	private String state;
	private String district;
	private long pinCode;
	private String landMark;
	private String location;
	private String companyType;
	private String gstNumber;
	private String panNumber;
	private String licenseNumber;
	private Date licenseValidity;
	private String bankName;
	private String ifscCode;
	private long accountNumber;
	// upload Logo with name And Maintain Flag logo is uploaded or not
	private String logoUUID;
	private String logoName;
	private Boolean logoFlag;
	//upload sign survey
	private String signName;
	private String 	signUUID;

	
	
	private Timestamp updatedDate;

	private Boolean moter = false;
	private Boolean eng = false;
	private Boolean fire = false;
	private Boolean marine_cargo = false;
	private Boolean misc = false;
	private Boolean other = false;
	private Boolean analytics = false;
	private Boolean account = false;

}