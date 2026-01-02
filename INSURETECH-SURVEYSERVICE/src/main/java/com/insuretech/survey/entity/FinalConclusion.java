package com.insuretech.survey.entity;

import java.sql.Timestamp;
import java.util.List;

import com.insuretech.survey.entity.VehicleMasterDetails;

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
@Table(name = "final_conclusion_details", schema = "insuredb")
@NamedQuery(name = "FinalConclusion.findAll", query = "SELECT a FROM Conclusion a")
public class FinalConclusion {
//	Conclusion
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sr;

	private Timestamp createdDtm;
	private Timestamp updatedDtm;

//	final Conclusion
	private String companyGenId;
	private Long insurenceGenId;

	@Column(length = 2000)
	private String finalRemark;
	private String officerId;
	private String officerName;
	private String lessMetalParts;
	private String lessassessment;
	private String salvageCharges;
	private String averageClause;
	private String compulsoryClause;
	private String otherDeductibles;
	private Boolean cashless;
	@Column(length = 2000)
	private String finalenclosures;
	@Column(length = 2000)
	private String finalObservations;
	@Column(length = 2000)
	private String finalNotes;
	private String grossLossAmount;
	private String grossLossDep;
	private String grossLossGst;
	private String labourPaintTotal;
	private String finalTotalEstimate;
	@Column(length=15)
	private String netLossValue;
	@Column(length=2000)
	private String finalObservationsandFinding;
}
