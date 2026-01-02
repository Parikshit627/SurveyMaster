package com.insuretech.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "metal_type", schema = "insuredb")

public class MetalType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long materialTypeGenId;
	
	@Column(nullable = false)
	private String code; // M, P, G, S, O

	@Column(nullable = false)
	private String materialType;

	@Column(nullable = false)
	private String depreciation; // e.g., 15%, 50%, etc

}
