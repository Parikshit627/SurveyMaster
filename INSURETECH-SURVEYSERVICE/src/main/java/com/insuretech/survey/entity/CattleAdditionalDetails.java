package com.insuretech.survey.entity;

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
@Table(name = "cattle_additional_details", schema = "cattledb")
@NamedQuery(name = "CattleAdditionalDetails.findAll", query = "SELECT a FROM CattleAdditionalDetails a")
public class CattleAdditionalDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cattleAdditionalDetailsGenId;
	
	private Long cattleIntimationGenId;

    @Column(name = "finance_bank", length = 200)
    private String financeBank;

    @Column(name = "policy_type", length = 100)
    private String policyType;

    @Column(name = "subject_matter", length = 150)
    private String subjectMatter;

    @Column(name = "endorsement", length = 255)
    private String endorsement;

    @Column(name = "total_sum_insured", length = 50)
    private String totalSumInsured;

    @Column(name = "relevant_sum_insured", length = 50)
    private String relevantSumInsured;

    @Column(name = "loss_revealed_date", length = 50)
    private String lossRevealedDate;

    @Column(name = "person_at_loss", length = 150)
    private String personAtLoss;

    @Column(name = "witness_at_loss", length = 255)
    private String witnessAtLoss;

    @Column(name = "survey_location", length = 150)
    private String surveyLocation;

    @Column(name = "representative_at_survey", length = 150)
    private String representativeAtSurvey;

    @Column(name = "fir_status", length = 50)
    private String firStatus;

    @Column(name = "other_authority", length = 100)
    private String otherAuthority;

    @Column(name = "purpose_of_livestock", length = 255)
    private String purposeOfLivestock;

    @Column(name = "number_of_livestock", length = 100)
    private String numberOfLivestock;

	private Timestamp createdDtm = new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
    
}
