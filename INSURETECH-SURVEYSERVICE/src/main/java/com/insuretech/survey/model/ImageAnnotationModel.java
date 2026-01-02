package com.insuretech.survey.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImageAnnotationModel {
    private String image;
    private Integer classId;
    private String label;
    private String insurenceGenId;
    private String uuid;
    private String bboxUuid;
    
    private List<Double> bbox; // Correctly defined to match JSON: [x, y, width, height]

    public void setBbox(List<Double> bboxes) {
        this.bbox = bboxes;
    }

    public List<Double> getBbox() {
        return bbox;
    }
}