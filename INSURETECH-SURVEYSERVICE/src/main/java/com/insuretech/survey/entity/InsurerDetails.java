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
@Table(name = "insurer_details", schema = "insuredb")
@NamedQuery(name = "InsurerDetails.findAll", query = "SELECT a FROM InsurerDetails a")
public class InsurerDetails implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sr;

    private String abbreviation;
    private String insurer;
    private String type;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String website;
    private String gstNumber;
	private Timestamp added_date;
	
	private Timestamp updatedDate;
}