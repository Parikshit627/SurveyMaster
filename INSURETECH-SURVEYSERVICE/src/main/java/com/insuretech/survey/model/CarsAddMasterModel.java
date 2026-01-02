package com.insuretech.survey.model;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarsAddMasterModel {

	private  String type;

	private String model;
	private String variant;
	private String fule;
	private Integer cubicCapacity;
	private Integer seatingCapacity;
	private String ladenWeight;
	private String unladenWeight;
	private String company;
}
