package com.insuretech.survey.model;

import java.util.List;
import lombok.Data;

@Data
public class PhotoWizardModel {
    private List<DocumentInfo> documents;
    private String companyGenId;
    private Long insurenceGenId;

    @Data
    public static class DocumentInfo {
        private Long documentId;
        private Integer page;
        private Integer slot;
    }
}
