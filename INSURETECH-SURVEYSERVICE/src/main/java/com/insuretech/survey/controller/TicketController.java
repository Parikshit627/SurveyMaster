package com.insuretech.survey.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveDefineDataDetailsModel;
import com.insuretech.survey.model.TicketModel;
import com.insuretech.survey.serviceImpl.TicketServiceImpl;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	
	Logger log = LoggerFactory.getLogger(UserSurveyorController.class);
	
	TicketServiceImpl ticketServiceImpl;
	
	@PostMapping("/saveTicket")
	public ResponseModel saveTicket(@RequestBody TicketModel ticketModel) {
		String methodName = "saveTicket";
		ResponseModel response=new ResponseModel(); 
		try {
			log.info("Request : saving ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			response= ticketServiceImpl.saveTicket(ticketModel);

		} catch (Exception e) {
			log.info("Respond :error occured while saving ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
		}
		return response;
	}
	
	@PostMapping("/updateTicket")
	public ResponseModel updateTicket(@RequestBody TicketModel ticketModel) {
		String methodName = "updateTicket";
		ResponseModel response=new ResponseModel(); 
		try {
			log.info("Request : updating ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			response= ticketServiceImpl.updateTicket(ticketModel);

		} catch (Exception e) {
			log.info("Respond :error occured while updateing ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
		}
		return response;
	}

}
