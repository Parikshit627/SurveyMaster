package com.insuretech.survey.helper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.insuretech.survey.model.ApacheModel;
import com.insuretech.survey.model.ApacheModel;

@Component
public class DmsServer {

	Logger log = LoggerFactory.getLogger(DmsServer.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${DMS_SAVE_URL}")
	private String dmsSaveUrl;
	
	@Value("${DMS_GET_UR}")
	private String dmsGetUrl;
	

	public ResponseEntity<ApacheModel> saveDMS(byte[] file) {
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

	        // Prepare headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<ApacheModel> response = restTemplate.postForEntity(dmsSaveUrl, requestEntity, ApacheModel.class);

			log.info("Respond : file saved in DMS successfully " + " Method Name" + methodName + " Class : "
					+ this.getClass());
			return response;

		} catch (Exception e) {
			log.error("Respond : error occured while saving file in DMS" + " Method Name" + methodName + " Class : "
					+ this.getClass()+ " "+e.getMessage());
		}
		return null;
	}
	
	public ResponseEntity<ApacheModel> getFileDMS(String docUUId){
		String methodName = "getFileDMS";
		if(docUUId==null || docUUId.trim().equals("")) {
			log.info("Respond : Invalid UUID " + " Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
		try {
			log.info("Request : get file from DMS " + " Method Name" + methodName + " Class : " + this.getClass());
			 
			 HttpHeaders headers = new HttpHeaders();
			 headers.setContentType(MediaType.APPLICATION_JSON);
			 
			 Map<String,String> body=new HashMap<>();
			 body.put("uuid",docUUId);
			 
			 HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(body, headers);
			 ResponseEntity<ApacheModel> response = restTemplate.postForEntity(dmsGetUrl, requestEntity, ApacheModel.class);

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
