package com.insuretech.survey.model;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
    private String imgUuid;
    private String imgName;
    private String imgType;
    private String imgTime;
    private Timestamp imgUploadTime;
}