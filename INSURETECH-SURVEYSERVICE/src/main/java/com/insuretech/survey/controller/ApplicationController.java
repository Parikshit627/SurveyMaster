package com.insuretech.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.ApplicationService;
import com.insuretech.survey.service.CalculationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/apk")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;


	@GetMapping("/apkDashboard")
	public ResponseModel getpannelcalculation() {
		String methodName = "apkDashboard";
		try {
			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			return applicationService.apkDashboard();

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
	
	
	
}
