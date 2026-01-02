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
@Table(name = "ticket", schema = "insuredb")
@NamedQuery(name = "Ticket.findAll", query = "SELECT a FROM Ticket a")
public class Ticket {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketGenId;
	
	@Column(unique=true)
	private String ticketId;
	
	private String title;
	
	@Column(length=1200)
    private String description;
    private String type;
    private String status; 
    private String userEmail;
    private String userName;
    private String userId;
    private String referenceNo;
    
    @Column(length=500)
    private String remark;
	
	private Timestamp createdDtm;
    private Timestamp updatedDtm;

    private String createdBy;
    private String updatedBy;
	
}
