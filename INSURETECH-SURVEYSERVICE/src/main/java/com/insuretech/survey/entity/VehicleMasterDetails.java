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
@Table(name = "vehicle_master_details", schema = "insuredb")
@NamedQuery(name = "VehicleMasterDetails.findAll", query = "SELECT a FROM VehicleMasterDetails a")
public class VehicleMasterDetails {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long vehicleMasterId;
	
	private long companyId;
	private  String type;

	private String model;
	private String variant;
	private String fule;
	private Integer cubicCapacity;
	private Integer seatingCapacity;
	private String ladenWeight;
	private String unladenWeight;
	
	private Timestamp createdDtm;
	private Timestamp updatedDtm;
}
