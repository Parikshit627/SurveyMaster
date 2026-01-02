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
@Table(name = "work_shop_Details", schema = "insuredb")
@NamedQuery(name = "WorkShopDetails.findAll", query = "SELECT a FROM WorkShopDetails a")
public class WorkShopDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sr;

	private String workshopName;
	private long contactNumbers;
	private String emailId;
	private String panNumber;
	private String bankName;
	private String ifscCode;
	private long accountNumber;
	private String gstNumber;
	private String state;
	private String district;
	private long pinCode;
	private String landMark;
	private String location;
    private long stateCode;
	private Timestamp added_date;
	
	private Timestamp updatedDate;
}
