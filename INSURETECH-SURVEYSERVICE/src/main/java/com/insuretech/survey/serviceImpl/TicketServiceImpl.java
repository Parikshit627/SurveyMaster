package com.insuretech.survey.serviceImpl;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.Ticket;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.TicketModel;
import com.insuretech.survey.service.TicketService;

@Service
public class TicketServiceImpl extends AbstractMasterRepository implements TicketService{

	Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);
	
	@Override
	public ResponseModel saveTicket(TicketModel ticketModel) {
		String methodName = "saveTicket";
		ResponseModel response=new ResponseModel();
		try {
			
			log.info("Request : saving ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
            
			Long ticketId=ticketRepo.findMaxValue()==null?10001:ticketRepo.findMaxValue()+1;
			Ticket ticket=new Ticket();
			BeanUtils.copyProperties(ticketModel, ticket, "ticketGenId");
			ticket.setCreatedBy(ticketModel.getUserId());
			ticket.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
			ticket.setTicketId(ticketId.toString());
			ticket.setStatus(CommonConstants.TICKET_PENDING.toString());
			Ticket ticketSaved=ticketRepo.save(ticket);
			
//			for(int i=0;i<ticketModel.getDocUUIDS().size();i++) {
//				FinalDocumentSubmission doc=new FinalDocumentSubmission();
//				String docUUID=ticketModel.getDocUUIDS().get(i);
//				String fileName=ticketId+"_doc_"+(i+1);
//				doc.setDocName(fileName);
//				doc.setTicketId(ticketId);
//				finalDocumentSubmissionRepo.save(doc);
//			}
			
			log.info("Respond : ticket saved -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
            
			response.setData(ticketSaved);
			response.setMessage("Ticket raised successfully by userId : " + ticketModel.getUserId());
			response.setHttpStatus(HttpStatus.OK);

		} catch (Exception e) {
			log.info("Respond :error occured while saving ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			e.printStackTrace();
			
			response.setMessage("error occures : " + e.getLocalizedMessage());
			response.setHttpStatus(HttpStatus.OK);
		}
		return response;
	}

	@Override
	public ResponseModel updateTicket(TicketModel ticketModel) {
		String methodName = "updateTicket";
		ResponseModel response=new ResponseModel();
		try {
			
			log.info("Request : updating ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
            
			Ticket ticket=ticketRepo.findByTicketId(ticketModel.getTicketId());
//			BeanUtils.copyProperties(ticketModel, ticket, "ticketGenId","createdDtm","createdBy");
			if(ticketModel.getStatus()==(CommonConstants.TICKET_CLOSED_STATUS)) {
				ticket.setStatus(CommonConstants.TICKET_CLOSED.toString());
			}
			else if(ticketModel.getStatus()==(CommonConstants.TICKET_PENDING_STATUS)) {
				ticket.setStatus(CommonConstants.TICKET_PENDING.toString());
			}
			ticket.setRemark(ticketModel.getRemark());
			ticket.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
			ticket.setUpdatedBy(ticketModel.getUserId());
			Ticket ticketSaved=ticketRepo.save(ticket);
			
			log.info("Respond : ticket updated -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
            
			response.setData(ticketSaved);
			response.setMessage("Ticket raised successfully by userId : " + ticketModel.getUserId());
			response.setHttpStatus(HttpStatus.OK);

		} catch (Exception e) {
			log.info("Respond :error occured while updating ticket -- raised by userId : " + ticketModel.getUserId() + " title : " + ticketModel.getTitle() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			e.printStackTrace();
			
			response.setMessage("error occures : " + e.getLocalizedMessage());
			response.setHttpStatus(HttpStatus.OK);
		}
		return response;
	}

}
