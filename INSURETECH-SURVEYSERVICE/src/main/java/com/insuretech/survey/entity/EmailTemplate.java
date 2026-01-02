package com.insuretech.survey.entity;

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
@Table(name = "email_template", schema = "insuredb")
@NamedQuery(name = "EmailTemplate.findAll", query = "SELECT a FROM EmailTemplate a")
public class EmailTemplate {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailTempGenId;

	private String emailTempId;
    private String templateName;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;
}
