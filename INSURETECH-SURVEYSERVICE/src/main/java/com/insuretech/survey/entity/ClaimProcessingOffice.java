package com.insuretech.survey.entity;

import java.io.Serializable;
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
@Table(name = "claim_processing_office", schema = "insuredb")
@NamedQuery(name = "ClaimProcessingOffice.findAll", query = "SELECT a FROM ClaimProcessingOffice a")
public class ClaimProcessingOffice  implements Serializable {
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sr;

	 public String insurerAbbreviation;
	    public String insurerName;
//	    @Column(unique=true)
	    private String claimProcessingOfficeCode;
	    private String claimProcessingOfficeName;
	    private String emailId;
	    private long contactNumbers;
	    private String gstNumber;
	    private String district;
	    private String state;
	    private long pinCode;
	    private String location;
	    private String landMark;
	    private long stateCode;
		private Timestamp added_date;
		
		private Timestamp updatedDate;
}