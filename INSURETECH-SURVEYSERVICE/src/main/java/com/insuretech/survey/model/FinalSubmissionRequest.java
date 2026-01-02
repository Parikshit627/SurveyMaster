package com.insuretech.survey.model;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalSubmissionRequest {
    private String vehicleNumber;
    private String companyGenId;
    private String action;
    private String referenceNo;
    private Long insurenceGenId;
    private List<ImageData> images;
    private List<VideoData> video;
    private List<DocumentData> documents;
}