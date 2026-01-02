package com.insuretech.survey.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CattleDashboardModel {
	private Long cattleIntimationGenId;

    private String referenceNo;

    private Timestamp dateIntimation;

    private Timestamp dateLoss;

    private String placeLoss;

    private String assetDetail;

    private String policyNo;

    private String claimNo;

    private String insurer;

    private String insurerName;

    private String workshopName;

    private BigDecimal estimatedAmount;

    private BigDecimal provisionalAmount;

    private String branch;

    private String surveyor;

    private String backOfficer;

    private String createdBy;

    private Integer currentStatus;

    private String assignTo;

    private String currentStatusDes;
}
