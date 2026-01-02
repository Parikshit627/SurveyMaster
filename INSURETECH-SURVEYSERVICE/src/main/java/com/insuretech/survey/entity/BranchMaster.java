package com.insuretech.survey.entity;

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
@Table(name = "branch_master", schema = "insuredb")
@NamedQuery(name = "BranchMaster.findAll", query = "SELECT a FROM BranchMaster a")
public class BranchMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long branchGenId;
	
	private String companyGenId;
	private String branchName;
	private String branchAbbreviation;
	private String brancheEmail;
	private String branchAddress;
	private String contactNumbers;
	private String district;
    private String state;
    private long pinCode;
    private String location;
//    private String landMark;
    private long stateCode;
	
	private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
