package com.insuretech.survey.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "exterior_car_parts", schema = "insuredb")
@NamedQuery(name = "ExteriorCarParts.findAll", query = "SELECT a FROM ExteriorCarParts a")
public class ExteriorCarParts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sr;
	
	private String hsn;
	private String partName;
	private String type;
	private long vehicleTypeId;
	private String vehicleTypeName;
	private String cost;
	private String gst;
	
}
