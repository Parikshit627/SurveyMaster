//package com.insuretech.survey.entity;
//
//import java.sql.Timestamp;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.NamedQuery;
//import jakarta.persistence.SequenceGenerator;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "vehicle_varient_all", schema = "insuredb")
//@NamedQuery(name = "VehicleVarientAll.findAll", query = "SELECT a FROM VehicleVarientAll a")
//public class VehicleVarientAll {
//
//    @Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long varientId;
//	
//	private String varient;
//	
//	private Timestamp createdDtm;
//	
//}
// 