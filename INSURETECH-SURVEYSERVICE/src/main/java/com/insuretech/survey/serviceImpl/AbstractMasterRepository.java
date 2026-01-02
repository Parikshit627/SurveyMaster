package com.insuretech.survey.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.insuretech.survey.repo.AIComponentDetailsRepo;
import com.insuretech.survey.repo.AiDamagesRepo;
import com.insuretech.survey.repo.AiImagesRepo;
import com.insuretech.survey.repo.AssemblyDetailsRepo;
import com.insuretech.survey.repo.BranchMasterRepo;
import com.insuretech.survey.repo.CalculationLabourAssessmentProRepo;
import com.insuretech.survey.repo.CalculationPartAssessmentProRepo;
import com.insuretech.survey.repo.CattleCommentsRepo;
import com.insuretech.survey.repo.CattleIntimationDetailsRepo;
import com.insuretech.survey.repo.ClaimProcessingOfficeRepo;
import com.insuretech.survey.repo.CompanyRegisteredRepo;
import com.insuretech.survey.repo.ConclusionAssessmentProRepo;
import com.insuretech.survey.repo.ConclusionRepo;
import com.insuretech.survey.repo.DamageDetailsRepo;
import com.insuretech.survey.repo.DriverParticularsDetailsRepo;
import com.insuretech.survey.repo.EfficiencyRepo;
import com.insuretech.survey.repo.EmailHistoryRepo;
import com.insuretech.survey.repo.EmailTemplateRepo;
import com.insuretech.survey.repo.EnclosuresRepo;
import com.insuretech.survey.repo.ExteriorCarPartsRepo;
import com.insuretech.survey.repo.FinalConclusionRepo;
import com.insuretech.survey.repo.FinalDocumentSubmissionRepo;
import com.insuretech.survey.repo.FuelTypeRepo;
import com.insuretech.survey.repo.ImageAnnotationRepository;
import com.insuretech.survey.repo.InsurerDetailsRepo;
import com.insuretech.survey.repo.InvoiceNoCounterRepo;
import com.insuretech.survey.repo.InvoiceRepository;
import com.insuretech.survey.repo.ItemRepo;
import com.insuretech.survey.repo.JasperDataRepo;
import com.insuretech.survey.repo.LabourAssessmentProRepo;
import com.insuretech.survey.repo.LabourDetailsRepo;
import com.insuretech.survey.repo.LinkGeneratedHistoryRepo;
import com.insuretech.survey.repo.LossDetailsRepo;
import com.insuretech.survey.repo.MasterDocumentRequiredRepo;
import com.insuretech.survey.repo.MetalCalculationRepository;
import com.insuretech.survey.repo.MetalTypeRepo;
import com.insuretech.survey.repo.NotesRepo;
import com.insuretech.survey.repo.ObservationsRepo;
import com.insuretech.survey.repo.PartAssessmentProRepo;
import com.insuretech.survey.repo.PhotoPendingAiRepo;
import com.insuretech.survey.repo.PolicyDetailsRepo;
import com.insuretech.survey.repo.ReferenceNoCounterRepo;
import com.insuretech.survey.repo.RegisteredRepo;
import com.insuretech.survey.repo.ReportDocumentUploadRepo;
import com.insuretech.survey.repo.StatusRepo;
import com.insuretech.survey.repo.TicketRepo;
import com.insuretech.survey.repo.UnderWritingOfficeRepo;
import com.insuretech.survey.repo.UserSurveyorBasicDetailsRepo;
import com.insuretech.survey.repo.VehicleCompanyDetailsRepo;
import com.insuretech.survey.repo.VehicleDetailsRepo;
import com.insuretech.survey.repo.VehicleMasterDetailsRepo;
import com.insuretech.survey.repo.VehicleModelRepo;
import com.insuretech.survey.repo.VehicleTypeDetailsRepo;
import com.insuretech.survey.repo.VehicleVariantRepo;
import com.insuretech.survey.repo.WorkShopDetailsRepo;

abstract class AbstractMasterRepository {

	@Autowired
	UserSurveyorBasicDetailsRepo userSurveyorBasicDetailsRepo;

	@Autowired
	StatusRepo statusRepo;

	@Autowired
	EfficiencyRepo efficiencyRepo;

	@Autowired
	LinkGeneratedHistoryRepo linkGeneratedHistoryRepo;

	@Autowired
	InsurerDetailsRepo insurerDetailsRepo;

	@Autowired
	UnderWritingOfficeRepo underWritingOfficeRepo;

	@Autowired
	ClaimProcessingOfficeRepo claimProcessingOfficeRepo;

	@Autowired
	WorkShopDetailsRepo workShopDetailsRepo;

	@Autowired
	PolicyDetailsRepo policyDetailsRepo;

	@Autowired
	VehicleDetailsRepo vehicleDetailsRepo;

	@Autowired
	DriverParticularsDetailsRepo driverParticularsDetailsRepo;

	@Autowired
	LossDetailsRepo lossDetailsRepo;

	@Autowired
	AssemblyDetailsRepo assemblyDetailsRepo;

	@Autowired
	LabourDetailsRepo labourDetailsRepo;

	@Autowired
	DamageDetailsRepo damageDetailsRepo;

	@Autowired
	ExteriorCarPartsRepo partsMasterDetailsRepo;

	@Autowired
	ConclusionRepo conclusionRepo;

	@Autowired
	EnclosuresRepo enclosuresRepo;

	@Autowired
	ObservationsRepo observationsRepo;

	@Autowired
	NotesRepo notesRepo;

	@Autowired
	VehicleTypeDetailsRepo vehicleTypeDetailsRepo;

	@Autowired
	MetalTypeRepo metalTypeRepo;

	@Autowired
	FinalDocumentSubmissionRepo finalDocumentSubmissionRepo;

	@Autowired
	FinalConclusionRepo finalConclusionRepo;

	@Autowired
	ReportDocumentUploadRepo reportDocumentUploadRepo;

	@Autowired
	JasperDataRepo jasperDataRepo;

	@Autowired
	EmailTemplateRepo emailTemplateRepo;

	@Autowired
	CompanyRegisteredRepo companyRegisteredRepo;

	@Autowired
	BranchMasterRepo branchMasterRepo;

	@Autowired
	MetalCalculationRepository metalCalculationRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	ReferenceNoCounterRepo referenceNoCounterRepo;

	@Autowired
	TicketRepo ticketRepo;

	@Autowired
	InvoiceNoCounterRepo invoiceNoCounterRepo;

	@Autowired
	RegisteredRepo registeredRepo;

	@Autowired
	ItemRepo itemRepo;

	@Autowired
	VehicleMasterDetailsRepo vehicleMasterDetailsRepo;

	@Autowired
	VehicleCompanyDetailsRepo vehicleCompanyDetailsRepo;

	@Autowired
	EmailHistoryRepo emailHistoryRepo;
	
	@Autowired
	FuelTypeRepo fuelTypeRepo;
	
	@Autowired
	VehicleModelRepo vehicleModelRepo;
	
	@Autowired
	VehicleVariantRepo vehicleVariantRepo;
	
	@Autowired
	PartAssessmentProRepo partAssessmentProRepo;

	@Autowired
	LabourAssessmentProRepo labourAssessmentProRepo;
	
	@Autowired
	CalculationPartAssessmentProRepo calculationPartAssessmentProRepo;
	
//	Change -- Aman -- Start --- 01-09-2025
	@Autowired
	PhotoPendingAiRepo photoPendingAiRepo;
//	Change -- Aman -- End --- 01-09-2025

	@Autowired
	CalculationLabourAssessmentProRepo calculationLabourAssessmentProRepo;
	
	@Autowired
	ConclusionAssessmentProRepo conclusionAssessmentProRepo;
	
	@Autowired
	MasterDocumentRequiredRepo masterDocumentRequiredRepo; 
	
	@Autowired
	ImageAnnotationRepository imageAnnotationRepository;
	
	@Autowired
	AIComponentDetailsRepo aiComponentDetailsRepo;
	
	@Autowired
	AiDamagesRepo aiDamagesRepo;
	
	@Autowired
	AiImagesRepo aiImagesRepo;
	
	@Autowired
	CattleIntimationDetailsRepo cattleIntimationDetailsRepo;
	
	@Autowired
	CattleCommentsRepo cattleCommentsRepo;
}
