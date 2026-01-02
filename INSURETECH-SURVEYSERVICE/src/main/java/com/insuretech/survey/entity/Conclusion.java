package com.insuretech.survey.entity;
import java.sql.Timestamp;

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
@Table(name = "conclusion_details", schema = "insuredb")
@NamedQuery(name = "Conclusion.findAll", query = "SELECT a FROM Conclusion a")
public class Conclusion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sr;
	
	private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId; 
    
    private String officerId;
    private String officerName;
    @Column(length = 1000)
    private String remark;
    
	
	private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
