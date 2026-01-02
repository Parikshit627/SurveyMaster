package com.insuretech.survey.service;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.TicketModel;

public interface TicketService {
	
	ResponseModel saveTicket(TicketModel ticketModel);
	
	ResponseModel updateTicket(TicketModel ticketModel);
}
