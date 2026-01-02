package com.insuretech.survey.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "status", schema = "insuredb")
@NamedQuery(name = "Status.findAll", query = "SELECT a FROM Status a")
public class Status implements Serializable {

	
	private static final long serialVersionUID = 1L;


	@Id	
	@Column(name="status_id")
	private int statusId;	
	
	@Column(name="description")
	private String description;
	
}
