package com.insuretech.survey.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.insuretech.survey.model.ApacheModel;

@Component
public class FileSystem {

	Logger log = LoggerFactory.getLogger(DmsServer.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${FILESYETEM_SAVE_BY_REFRENCE_NO_URL}")
	private String fileSaveUrl;
	
	@Value("${FILESYETEM_GET_BY_UUID_AND_REGABB_URL}")
	private String filegetByUuidAndRegAbbUrl;
	
	
	
	@Value("${DMS_GET_UR}")
	private String dmsGetUrl;
	

	public ResponseEntity<ApacheModel> saveDMS(byte[] file,String referenceNo,String uploadType) {
		String methodName = "saveDMS";
		log.info("Request : saving file in DMS " + " Method Name" + methodName + " Class : " + this.getClass());

		if (file == null || file.length == 0) {
			log.info("Respond : file is corrupted " + " Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
		try {
			 // Wrap byte[] into a resource
	        ByteArrayResource fileAsResource = new ByteArrayResource(file) {
	            @Override
	            public String getFilename() {
	                return "report.pdf";  // Important: must provide a filename
	            }
	        };

	        // Prepare multipart body
	        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	        body.add("file", fileAsResource); // 'file' must match the server-side parameter name
	        body.add("referenceNo", referenceNo);
	        body.add("uploadType", uploadType);
	        
	        // Prepare headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<ApacheModel> response = restTemplate.postForEntity(fileSaveUrl, requestEntity, ApacheModel.class);

			log.info("Respond : file saved in DMS successfully " + " Method Name" + methodName + " Class : "
					+ this.getClass());
			return response;

		} catch (Exception e) {
			log.error("Respond : error occured while saving file in DMS" + " Method Name" + methodName + " Class : "
					+ this.getClass()+ " "+e.getMessage());
		}
		return null;
	}
	
	public ResponseEntity<ApacheModel> getFileDMS(String docUUId,String regAbbreviation,String uploadType){
		String methodName = "getFileDMS";
		if(docUUId==null || docUUId.trim().equals("")) {
			log.info("Respond : Invalid UUID " + " Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
		try {
			log.info("Request : get file from DMS " + " Method Name" + methodName + " Class : " + this.getClass());
			 
//			 HttpHeaders headers = new HttpHeaders();
//			 headers.setContentType(MediaType.APPLICATION_JSON);
			 
			 UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(filegetByUuidAndRegAbbUrl)
				        .queryParam("uuid", docUUId)
				        .queryParam("regAbbreviation", regAbbreviation)
				        .queryParam("uploadType", uploadType);

				HttpHeaders headers = new HttpHeaders();
				HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

				ResponseEntity<ApacheModel> response = restTemplate.exchange(
				        builder.toUriString(),
				        HttpMethod.GET,
				        requestEntity,
				        ApacheModel.class
				);

			 log.info("Respond : file fetched in DMS successfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
				return response;

			} catch (Exception e) {
				log.error("Respond : error occured while fetching file from DMS" + " Method Name" + methodName + " Class : "
						+ this.getClass()+ " "+e.getMessage());
			}
			return null;
	}
}
