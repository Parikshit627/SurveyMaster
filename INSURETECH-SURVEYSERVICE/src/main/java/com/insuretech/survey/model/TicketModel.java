package com.insuretech.survey.model;

import java.sql.Timestamp;
import java.util.List;

import com.insuretech.survey.entity.FinalDocumentSubmission;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TicketModel {

	private Long ticketGenId;

	private String title;
	private String ticketId;
	
	private String description;
	
	private String type;
	private String status;
	private String userEmail;
	private String userName;
	private String userId;
	private String referenceNo;
	private String remark;
	private List<FinalDocumentSubmission> docList;

	private Timestamp createdDtm;
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
}
