package com.insuretech.survey.model;

import lombok.Data;

@Data
public class AlfrescoModel {
	String base64File;
	String fileName;
	
	private String docContentType;
	private String docName;
	private String docByteStream;
	private String docFileName;
	private String docUUID;
	private String docId;
	private String errorCode;
	private boolean isDeleted;

}
