package com.insuretech.survey.entity;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name = "registered", schema = "insuredb")
@NamedQuery(name="Registered.findAll", query="SELECT a FROM Registered a")
public class Registered implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long regId;

	private String authPassword;
	private String emailId;
	private String fullName;
	private long phoneNo;
	private String gender;
	private String userName;
	private String professionalType;
	private String registrationBy;
	private String roleId;
	private Timestamp createdDate;
	
	//Update 
	private String pic;
    private String panNumber;
    private long aadharNumber;
    private String licenseNumber;
    private Date licenseValidity;
    private String bankName;
    private String ifscCode;
    private long accountNumber;
    private String gstNumber;
    private String state;
    private String district;
    private long pinCode;
    private String landMark;
    private String location;
    private String workinCity;
    private String logoUUID;
	private String logoName;
	private Boolean logoFlag;
	private String 	signUUID;
	private String signName;


	private Timestamp updatedDate;
    
    private String companyRegGenId;
    private String insurenceGenId;
    private Boolean locked=false;
    private String regCode;
    private String regAbbreviation;
	
}