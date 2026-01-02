package com.insuretech.survey.entity;

import java.io.Serializable;
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
@Table(name = "image_annotations", schema = "insuredb")
@NamedQuery(name = "ImageAnnotation.findAll", query = "SELECT a FROM ImageAnnotation a")
public class ImageAnnotation implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageAnnotationId;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "class_id", nullable = false)
    private Integer classId;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "insurance_gen_id", nullable = false)
    private String insuranceGenId;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Timestamp createdDate;
    
    @Column(name = "x_coordinate", nullable = false)
    private Double x;

    @Column(name = "y_coordinate", nullable = false)
    private Double y;

    @Column(name = "width", nullable = false)
    private Double width;

    @Column(name = "height", nullable = false)
    private Double height;
    
    @Column(name = "b_box_uuid", nullable = false)
    private String bboxUuid;

}