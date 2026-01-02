package com.insuretech.survey.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	Logger log = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	AccountService accountService;


	@GetMapping("/getEmailAndName")
	public ResponseModel getEmailAndName(@RequestParam String companyGenId){
		String methodName = "getEmailAndName";
		try {
			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return accountService.getEmailAndName(companyGenId);

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
	
}
