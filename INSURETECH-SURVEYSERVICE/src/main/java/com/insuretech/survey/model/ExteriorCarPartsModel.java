package com.insuretech.survey.model;

import lombok.Data;

@Data
public class ExteriorCarPartsModel {

	private String hsn;
	private String partName;
	private String type;
	private long vehicleTypeId;
	private String cost;
	private String gst;
	
}
