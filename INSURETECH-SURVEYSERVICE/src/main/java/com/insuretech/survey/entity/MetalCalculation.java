package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.checkerframework.checker.units.qual.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Data
@Table(name = "metal_calculation", schema = "insuredb")
@NamedQuery(name = "MetalCalculation.findAll", query = "SELECT a FROM MetalCalculation a")
public class MetalCalculation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sr;

	private String companyGenId;
	 @Column(length=15)
	private String taxPaidToggle;
	private Long insurenceGenId;
	private BigDecimal metalAllowed;
	private BigDecimal metalGST;
	private BigDecimal metalDep;

	private BigDecimal plasticAllowed;
	private BigDecimal plasticGST;
	private BigDecimal plasticDep;

	private BigDecimal glassAllowed;
	private BigDecimal glassGST;
	private BigDecimal glassDep;

	private BigDecimal iiHandAllowed;
	private BigDecimal iiHandGST;
	private BigDecimal iiHandDep;

	private BigDecimal othersAllowed;
	private BigDecimal othersGST;
	private BigDecimal othersDep;

	private BigDecimal totalestimate;
	private BigDecimal gstPortion;
	private BigDecimal subtotal;

	private Timestamp createdDtm;
	private Timestamp updatedDtm;
	

}
