package com.insuretech.survey.model;

import java.util.List;

import com.insuretech.survey.entity.Enclosures;
import com.insuretech.survey.entity.Notes;
import com.insuretech.survey.entity.Observations;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConclusionModel {

	private long sr;

	private String officerId;
	private String officerName;
	private String remark;
	
	@Column(nullable=true)
	private List<String> enclosures;
	@Column(nullable=true)
	private List<String> observations;
	@Column(nullable=true)
	private List<String> notes;
	
}
