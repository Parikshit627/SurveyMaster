package com.insuretech.survey.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle_type_details", schema = "insuredb")
@NamedQuery(name = "VehicleTypeDetails.findAll", query = "SELECT a FROM VehicleTypeDetails a")
public class VehicleTypeDetails {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long typeId;
	
	private String type;
	

//    @OneToMany(mappedBy = "carType", cascade = CascadeType.ALL)
//    private List<VehicleModel> models;
	
	private Timestamp createdDtm;
	private Timestamp updatedDtm;
	
}
 