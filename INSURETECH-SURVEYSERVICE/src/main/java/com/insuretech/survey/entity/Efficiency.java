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
@Table(name = "efficiency", schema = "insuredb")
@NamedQuery(name="Efficiency.findAll", query="SELECT a FROM Efficiency a")
public class Efficiency implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long efficiencyId;
	
	@Column(name = "used_name")
	private String userName;

	@Column(name = "used_credit")
	private Long usedCredit;
	
	@Column(name = "assigned_credit")
	private Long assignedCredit;

	@Column(name = "tat")
	private Long tat;
	
	@Column(name = "update_date")
	private Timestamp updateDate;
	
	@Column(name = "created_date")
	private Timestamp createdDate;

}
