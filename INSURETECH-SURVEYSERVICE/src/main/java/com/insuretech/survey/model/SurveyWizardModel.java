package com.insuretech.survey.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SurveyWizardModel {
	private String officerId;
	private String officerName;
	private String companyGenId;
	private int flag;
	
	private Long   insurenceGenId;
	private String workedBySurveyName;
	private String workedByUserId;
	private String surveyType;
	




	private PolicyDetailsModel policyForm;
	private VehicleDetailsModel vehicleForm;
	private DriverParticularsDetailsModel driverParticularsForm;
	private LossDetailsModel lossDetailsForm;
	private LossDetailsModel pannelCalculation;
	private List<AssemblyDetailsModel> assemblyForm;
	private List<LabourDetailsModel> labourForm;
	private List<DamageDetailsModel> damageDetails;
	private ConclusionModel conclusionForm;
	private FinalConclusionModel finalConclusionForm;
	
	
	
}
