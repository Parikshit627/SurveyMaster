package com.insuretech.survey.model;


import java.util.List;

import lombok.Data;

@Data
public class LabourAssessmentProJson {
	List<LabourAssessmentProModel> labour;
	CalculationLabourAssessmentProModel totals;

}
