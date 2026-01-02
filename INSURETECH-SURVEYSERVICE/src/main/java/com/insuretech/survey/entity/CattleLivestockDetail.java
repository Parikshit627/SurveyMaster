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
@Table(name = "cattle_livestock_details", schema = "cattledb")
@NamedQuery(name = "CattleLivestockDetail.findAll", query = "SELECT a FROM CattleLivestockDetail a")
public class CattleLivestockDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cattleLivestockDetailsGenId;

    @Column(name = "health_certificate_date", length = 50)
    private String healthCertificateDate;

    @Column(name = "health_certificate_no", length = 100)
    private String healthCertificateNo;

    @Column(name = "value_in_health_certificate", length = 150)
    private String valueInHealthCertificate;

    @Column(name = "treatment_by_doctor", length = 150)
    private String treatmentByDoctor;

    @Column(name = "disease_start_on", length = 100)
    private String diseaseStartOn;

    @Column(name = "treatment_details", length = 255)
    private String treatmentDetails;

    @Column(name = "post_mortem_held_by", length = 150)
    private String postMortemHeldBy;

    @Column(name = "date_time_death", length = 50)
    private String dateTimeDeath;

    @Column(name = "post_mortem_held", length = 50)
    private String postMortemHeld;

    @Column(name = "identification_tag_no", length = 100)
    private String identificationTagNo;

    @Column(name = "identification_tag_intact", length = 100)
    private String identificationTagIntact;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(name = "age", length = 50)
    private String age;

    @Column(name = "colour", length = 50)
    private String colour;

    @Column(name = "horns", length = 100)
    private String horns;

    @Column(name = "marks", length = 255)
    private String marks;

    @Column(name = "condition", length = 100)
    private String condition;

    @Column(name = "milk_yield_max", length = 50)
    private String maltster;

    @Column(name = "milk_yield_two_month_before", length = 50)
    private String milk2MonthBefore;

    @Column(name = "milk_yield_at_death", length = 50)
    private String milkAtDeath;

    @Column(name = "valuation_by_doctor", length = 50)
    private String valuationByDoctor;

    @Column(name = "market_value_at_loss", length = 50)
    private String marketValueAtLoss;

    @Column(name = "disposal_method", length = 255)
    private String disposalMethod;

    @Column(name = "cause_of_loss_by_doctor", length = 255)
    private String causeOfLossByDoctor;
    
    private Timestamp createdDtm = new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
    
}
