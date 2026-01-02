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
public class VideoData {

	private String videoUUID;
    private String videoName;
    private String videoType;
	private Timestamp videoUploadTime;
	
}
