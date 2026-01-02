package com.insuretech.survey.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.Consignee;
import com.insuretech.survey.entity.Consigner;
import com.insuretech.survey.entity.InsuranceInfo;
import com.insuretech.survey.entity.Invoice;
import com.insuretech.survey.entity.InvoiceNoCounter;
import com.insuretech.survey.entity.Item;
import com.insuretech.survey.entity.Receiver;
import com.insuretech.survey.entity.Registered;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.model.InvoiceModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.InvoiceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceSericeImpl extends AbstractMasterRepository implements InvoiceService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseModel save(InvoiceModel invoiceModel) {

		ResponseModel response = new ResponseModel();

		  try {
		        log.info("✅ [START] SaveOrUpdate Invoice process started for Invoice No: {}", invoiceModel.getInvoiceNo());

//		        Invoice invoice = invoiceRepository.findByInvoiceNo(invoiceModel.getInvoiceNo());
		        
		        UserSurveyorBasicDetails userSurveyorBasicDetails = new UserSurveyorBasicDetails();
		        		
		       if(invoiceModel.getReferenceNumber() != null) {
		    	  userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.findByReferenceNo(invoiceModel.getReferenceNumber());
			    }else if(invoiceModel.getSubjectMatter() != null) {
			      userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.findByAsset(invoiceModel.getSubjectMatter());
				}
		        Invoice invoice = invoiceRepository.findByReferenceNumber(userSurveyorBasicDetails.getReferenceNo());
		        
		        boolean isUpdate = true;
		        
		        if(invoice == null) {
		        	isUpdate = false;
		        }

		        if (!isUpdate) {
		            invoiceModel = generateInvoiceNo(invoiceModel); // if you want new number
		            invoice = new Invoice();
			        invoice.setDate(invoiceModel.getDate());
			        invoice.setInvoiceNo(invoiceModel.getInvoiceNo());

		        }

		        // 1️⃣ Basic Fields
		        invoice.setUserId(invoiceModel.getUserId());
		        invoice.setCompanyGenId(invoiceModel.getCompanyGenId());
		       
//		        invoice.setDate(invoiceModel.getDate());

		        invoice.setReferenceNumber(userSurveyorBasicDetails.getReferenceNo());
		        invoice.setSubjectMatter(userSurveyorBasicDetails.getAsset());
		        
		        invoice.setBillToOption(invoiceModel.getBillToOption());
		        invoice.setShippedToOption(invoiceModel.getShippedToOption());

		        invoice.setEstimate(invoiceModel.getEstimate());
		        invoice.setAssessment(invoiceModel.getAssessment());
		        invoice.setSubtotal(invoiceModel.getSubtotal());
		        invoice.setSgst(invoiceModel.getSgst());
		        invoice.setCgst(invoiceModel.getCgst());
		        invoice.setIgst(invoiceModel.getIgst());
		        invoice.setTotalValue(invoiceModel.getTotalValue());
		        invoice.setRoundoff(invoiceModel.getRoundoff());
		        invoice.setTotalValueWords(invoiceModel.getTotalValueWords());
		        invoice.setPaymentInfo(invoiceModel.getPaymentInfo());

		        // 2️⃣ Consigner
		        Consigner consigner = invoice.getConsigner() != null ? invoice.getConsigner() : new Consigner();
		        consigner.setName(invoiceModel.getConsigner().getName());
		        consigner.setAddress(invoiceModel.getConsigner().getAddress());
		        consigner.setEmail(invoiceModel.getConsigner().getEmail());
		        consigner.setContact(invoiceModel.getConsigner().getContact());
		        consigner.setPhone(invoiceModel.getConsigner().getPhone());
		        consigner.setGstin(invoiceModel.getConsigner().getGstin());
		        consigner.setState(invoiceModel.getConsigner().getState());
		        consigner.setStateCode(invoiceModel.getConsigner().getStateCode());
		        invoice.setConsigner(consigner);

		        // 3️⃣ Receiver
		        Receiver receiver = invoice.getReceiver() != null ? invoice.getReceiver() : new Receiver();
		        receiver.setInsurerName(invoiceModel.getReceiver().getInsurerName());
		        receiver.setOfficeName(invoiceModel.getReceiver().getOfficeName());
		        receiver.setOfficeCode(invoiceModel.getReceiver().getOfficeCode());
		        receiver.setAddress(invoiceModel.getReceiver().getAddress());
		        receiver.setGstn(invoiceModel.getReceiver().getGstn());
		        receiver.setStateCode(invoiceModel.getReceiver().getStateCode());
		        receiver.setPan(invoiceModel.getReceiver().getPan());
		        receiver.setGst(invoiceModel.getReceiver().getGst());
		        receiver.setState(invoiceModel.getReceiver().getState());
		        invoice.setReceiver(receiver);

		        // 4️⃣ Consignee
		        Consignee consignee = invoice.getConsignee() != null ? invoice.getConsignee() : new Consignee();
		        consignee.setInsurerName(invoiceModel.getConsignee().getInsurerName());
		        consignee.setOfficeName(invoiceModel.getConsignee().getOfficeName());
		        consignee.setOfficeCode(invoiceModel.getConsignee().getOfficeCode());
		        consignee.setAddress(invoiceModel.getConsignee().getAddress());
		        consignee.setGstn(invoiceModel.getConsignee().getGstn());
		        consignee.setStateCode(invoiceModel.getConsignee().getStateCode());
		        consignee.setState(invoiceModel.getConsignee().getState());
		        invoice.setConsignee(consignee);

		        // 5️⃣ Insurance Info
		        InsuranceInfo insuranceInfo = invoice.getInsuranceInfo() != null ? invoice.getInsuranceInfo() : new InsuranceInfo();
		        insuranceInfo.setReportRefNumber(invoiceModel.getInsuranceInfo().getReportRefNumber());
		        insuranceInfo.setInsuredName(invoiceModel.getInsuranceInfo().getInsuredName());
		        insuranceInfo.setPolicyNumber(invoiceModel.getInsuranceInfo().getPolicyNumber());
		        insuranceInfo.setPolicyStartDate(invoiceModel.getInsuranceInfo().getPolicyStartDate());
		        insuranceInfo.setClaimNumber(invoiceModel.getInsuranceInfo().getClaimNumber());
		        insuranceInfo.setDateOfLoss(invoiceModel.getInsuranceInfo().getDateOfLoss());
		        invoice.setInsuranceInfo(insuranceInfo);
		        
		        // 7️⃣ Save invoice
		        Invoice saved = invoiceRepository.save(invoice);
		        
		        List<Item> itemList = itemRepo.findByInvoiceId(saved.getId());

		        if (!itemList.isEmpty()) {
		        	List<Item> existingItems = itemRepo.findByInvoiceId(saved.getId());
//		        	List<ItemModel> newItemModels = invoiceModel.getItems();
		        	List<Item> finalItemsToSave = new ArrayList<>();

		        	for (int i = 0; i < invoiceModel.getItems().size(); i++) {
//		        	    ItemModel itemModel = newItemModels.get(i);

		        	    if (i < existingItems.size()) {
		        	        // Update existing item
		        	        Item existingItem = existingItems.get(i);
		        	        BeanUtils.copyProperties(invoiceModel.getItems().get(i), existingItem, "id", "invoiceId");
		        	        finalItemsToSave.add(existingItem);
		        	    } else {
		        	        // Create new item
		        	        Item newItem = new Item();
		        	        BeanUtils.copyProperties(invoiceModel.getItems().get(i), newItem);
		        	        newItem.setInvoiceId(saved.getId()); // Set foreign key to invoice
		        	        finalItemsToSave.add(newItem);
		        	    }
		        	}

		        	// Save all updated and new items
		        	itemList = itemRepo.saveAll(finalItemsToSave);
		        } else {
		            // Save new items
		            List<Item> newItems = invoiceModel.getItems().stream().map(model -> {
		                Item item = new Item();
		                BeanUtils.copyProperties(model, item);
		                item.setInvoiceId(saved.getId());  // Link the item to the saved invoice
		                return item;
		            }).collect(Collectors.toList());

		            itemList = itemRepo.saveAll(newItems);
		        }


////		        // 6️⃣ Handle Items (clear and replace)
////		        if (invoice.getItems() == null) {
////		            invoice.setItems(new ArrayList<>()); // Initialize for new invoices
////		        }
////		        if (isUpdate) {
////		            invoice.getItems().clear(); // Clear existing items (triggers orphanRemoval)
////		        }
////		        List<Item> itemList = invoice.getItems(); // existing managed list
////
////		        for (com.insuretech.survey.model.Item itemModel : invoiceModel.getItems()) {
////		            Item item;
////
////		            if (itemModel.getId() != 0L) {
////		                // Try to find the existing item by ID and invoice
////		                Optional<Item> existingItemOpt = itemRepo.findByIdAndInvoiceId(itemModel.getId(), invoice.getId());
////		                
////		                if (existingItemOpt.isPresent()) {
////		                    // Item exists → update it
////		                    item = existingItemOpt.get();
////		                } else {
////		                    // Not found → create new item
////		                    item = new Item();
////		                    item.setInvoiceId(invoice.getId());                    
////		                    itemList.add(item);
////		                }
////		            } else {
////		                // New item (no ID) → create and add
////		                item = new Item();
////		                item.setInvoiceId(invoice.getId());
////		                itemList.add(item);
////		            }
//
//		            // Common set/update fields
//		            item.setDescription(itemModel.getDescription());
//		            item.setHsn(itemModel.getHsn());
//		            item.setQty(itemModel.getQty());
//		            item.setPrice(itemModel.getPrice());
//		            item.setTaxable(itemModel.getTaxable());
//		            item.setGst(itemModel.getGst());
//		            item.setTotal(itemModel.getTotal());
//		        }
//
//		        invoice.setItems(itemList);


		        // 8️⃣ Build response model
		        InvoiceModel savedModel = new InvoiceModel();
		        savedModel.setUserId(saved.getUserId());
		        savedModel.setCompanyGenId(saved.getCompanyGenId());
		        savedModel.setInvoiceNo(saved.getInvoiceNo());
		        savedModel.setDate(saved.getDate());
		        savedModel.setReferenceNumber(saved.getReferenceNumber());
		        savedModel.setBillToOption(saved.getBillToOption());
		        savedModel.setShippedToOption(saved.getShippedToOption());
		        
		        userSurveyorBasicDetails.setCurrentStatus(CommonConstants.CLOSED_STATUS);
		        userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);

		        response.setHttpStatus("OK");
		        response.setMessage(isUpdate ? "Invoice updated successfully." : "Invoice created successfully.");
		        response.setData(savedModel);

		        log.info("✅ [END] Invoice {} successfully. Invoice No: {}", (isUpdate ? "updated" : "created"), saved.getInvoiceNo());

		    } catch (Exception e) {
		        log.error("❌ [Error] Failed to process invoice. Reason: ", e);
		        response.setHttpStatus("error");
		        response.setMessage("Something went wrong while saving the invoice.");
		        response.setData(null);
		    }

		return response;
	}

	@Override
	public ResponseModel getAllInvoiceByCompanyGenIdAndReferenceNumberOrsubjectMatter(String companyGenId, String referenceNumber, String subjectMatter) {
		String methodName = "getAllInvoiceByCompanyGenId";
		ResponseModel response = new ResponseModel();
		try {
		    log.info("Request : Finding All Invoice By CompanyGenId" + companyGenId + "  Method Name" + methodName
		            + " Class : " + this.getClass());
		    if (companyGenId == null || companyGenId.trim().equals("")) {
		        log.info("Respond : Invalid Parameters" + "  Method Name" + methodName + " Class : " + this.getClass());
		        response.setHttpStatus(HttpStatus.OK);
		        response.setMessage("Invalid Parameter");
		        return response;
		    }

		    List<Invoice> invoiceList = new ArrayList<Invoice>();
		    
		    if (referenceNumber != null) {
		    	referenceNumber = referenceNumber.toUpperCase();
		        invoiceList = invoiceRepository.findByCompanyGenIdAndReferenceNumber(companyGenId, referenceNumber);
		        
		        if (!invoiceList.isEmpty()) {
		            Invoice invoice = invoiceList.get(invoiceList.size() - 1);
		            // Fetch items by invoice_id
		            List<Item> items = itemRepo.findByInvoiceId(invoice.getId());
//		            items.forEach(
//		            		item -> item.setInvoice(null));
		            invoice.setItems(items);
//		            invoiceList = List.of(in  voice);
		        }
		    }else if (subjectMatter != null) {
		    	subjectMatter = subjectMatter.toUpperCase();
		        invoiceList = invoiceRepository.findByCompanyGenIdAndSubjectMatter(companyGenId, subjectMatter);
		        
		        if (!invoiceList.isEmpty()) {
		            Invoice invoice = invoiceList.get(invoiceList.size() - 1);
		            // Fetch items by invoice_id
		            List<Item> items = itemRepo.findByInvoiceId(invoice.getId());
//		            items.forEach(
//		            		item -> item.setInvoice(null));
		            invoice.setItems(items);
//		            invoiceList = List.of(in  voice);
		        }

		    }  else {
//		        invoiceList = invoiceRepository.findByCompanyGenId(companyGenId);
		        invoiceList = invoiceRepository.findByCompanyGenIdOrderByIdAsc(companyGenId);
		      
//			    invoiceList.forEach(invoice -> {
//			        if (invoice.getItems() != null) {
//			            invoice.getItems().forEach(item -> item.setInvoice(null));
//			        }
//			    });
		    }
		    
		    if (invoiceList != null && !invoiceList.isEmpty()) {
		        response.setData(invoiceList);
		        response.setHttpStatus(HttpStatus.OK);
		        response.setMessage("Data get Successfully");

		        log.info("Respond : data founded successfully All Invoice By CompanyGenId " + companyGenId
		                + "  Method Name" + methodName + " Class : " + this.getClass());
		    } else {
		        response.setHttpStatus(HttpStatus.NO_CONTENT);
		        response.setMessage("No Data Found in db ");

		        log.info("Respond : No Data Found in db for Invoice By CompanyGenId " + companyGenId + "  Method Name"
		                + methodName + " Class : " + this.getClass());
		    }

		} catch (Exception e) {
		    log.error("An error occurred while Finding All Invoice By CompanyGenId " + companyGenId + e.getMessage(),
		            "  Method Name" + methodName + " Class : " + this.getClass());
		    e.printStackTrace();
		    response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		    response.setMessage("Exception :  " + e.getMessage());
		}
		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public InvoiceModel generateInvoiceNo(InvoiceModel invoiceModel) {
		String methodName = "generateInvoiceNo";
		String result = "";
		String invoiceNo = "";
		String formattedCounter = "";
		Map<String, String> map = new HashMap<>();
		try {
			log.info("Request : generate next invoiceNo  for : companyGenId " + invoiceModel.getCompanyGenId()
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			if (invoiceModel.getCompanyGenId() == null || invoiceModel.getCompanyGenId().trim().equals("")) {
				log.info("Response : Invalid paramater for : companyGenId " + invoiceModel.getCompanyGenId()
						+ "  Method Name" + methodName + " Class : " + this.getClass());
				return invoiceModel;
			}

			Registered registered = registeredRepo.findByCompanyRegGenIdAndRegCodeIsNotNull(invoiceModel.getCompanyGenId());
			if (registered == null) {
				log.info("Response : No registation found by companyGenId : " + invoiceModel.getCompanyGenId()
				+ "  Method Name" + methodName + " Class : " + this.getClass());
				return invoiceModel;
			}
			// financial year
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR) % 100;
			int nextYearShort = (currentYear + 1) % 100;
			String financialYear = String.format("%02d", currentYear) + "-" + String.format("%02d", nextYearShort);

			InvoiceNoCounter invoiceNoCounter = invoiceNoCounterRepo.findByCompanyGenId(invoiceModel.getCompanyGenId());
			if (invoiceNoCounter == null) {
				invoiceNoCounter = new InvoiceNoCounter();
				invoiceNoCounter.setCompanyGenId(invoiceModel.getCompanyGenId());
				invoiceNoCounter.setCounter(0L);
				invoiceNoCounter.setCreatedBy(invoiceModel.getCompanyGenId());
				invoiceNoCounter.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
				invoiceNoCounter.setUpdatedBy(invoiceModel.getCompanyGenId());
				invoiceNoCounter.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));

			} else {
//				String dateStr = currentYear + "-03-31 23:59:59";
//				Timestamp march31EndTimestamp = Timestamp.valueOf(dateStr);
//				Timestamp updatedDtm = invoiceNoCounter.getUpdatedDtm();
//				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
//
//				if (!updatedDtm.after(march31EndTimestamp) && currentTimestamp.after(march31EndTimestamp)) {
//					invoiceNoCounter.setCounter(0L);
//				}
				invoiceNoCounter.setUpdatedBy(invoiceModel.getCompanyGenId());
				invoiceNoCounter.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
			}
			invoiceNoCounter.setCounter(invoiceNoCounter.getCounter() + 1);
			formattedCounter = String.format("%05d", invoiceNoCounter.getCounter());
			

			// abbrevation/financial year/counter
			invoiceNo = registered.getRegAbbreviation() + "/" + financialYear + "/" + formattedCounter;
			invoiceNoCounter.setRegCode(registered.getRegCode());
			invoiceNoCounter.setInvoiceNo(invoiceNo);
			invoiceModel.setInvoiceNo(invoiceNo);
            
			invoiceNoCounterRepo.save(invoiceNoCounter);
			log.info("invoiceNo  generated successfully , invoiceNo :  " + invoiceNo + "  Method Name" + methodName
					+ " Class : " + this.getClass());

		} catch (Exception e) {
			log.error("error on -- generate invoiceNo  for : companyGenId : " + invoiceModel.getCompanyGenId()
					+ "Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
		}
		return invoiceModel;
	}

	@Override
	public ResponseModel deleteItemByRefrenceIdAndCompanyGenIdAndSNo(String referenceId, String companyGenId,
			String sNo) { 
		ResponseModel response = new ResponseModel();
		String methodName = "deleteItemByRefrenceIdAndCompanyGenIdAndSNo";
		
		try {
		        log.info("✅ [START] delete Item By RefrenceId: " + referenceId + " companyGenId: " + companyGenId + " Item sNo need to delete: " + sNo);

		        List<Invoice> invoiceList = invoiceRepository.findByCompanyGenIdAndReferenceNumber(companyGenId, referenceId);

		        if(invoiceList.size() >= 0) {
		        	Invoice invoice = invoiceList.get(invoiceList.size()-1);
		        	
		        	List<Item> items = itemRepo.findByInvoiceId(invoice.getId());
		        	
		        	int index = Integer.parseInt(sNo);
		        	// Validate index
		            if (index < 0 || index >= items.size()) {
		            	response.setHttpStatus(HttpStatus.NO_CONTENT);
				        response.setMessage("error, Invalid index: " + index);
		            }

		            // Remove the item at the specified index
		            Item item =  items.get(index);
		            itemRepo.deleteById(item.getId());
		        	
		        	response.setHttpStatus(HttpStatus.OK);
			        response.setMessage("Item deleted successfully");
			        
			        log.info("Respond : Data Updateds in db for Invoice By CompanyGenId " + companyGenId + "  Method Name"
			                + methodName + " Class : " + this.getClass());
		        }else {
			        response.setHttpStatus(HttpStatus.NO_CONTENT);
			        response.setMessage("No Data Found in db ");

			        log.info("Respond : No Data Found in db for Invoice By CompanyGenId " + companyGenId + "  Method Name"
			                + methodName + " Class : " + this.getClass());
			    }
		}catch (Exception e) {
			log.error("delete Item By RefrenceId: " + referenceId + " companyGenId: " + companyGenId + " Item sNo need to delete: "+ sNo
			+ "Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
		}
		        
		return response;
	}

//
//@Override
//public String updateCounter(InvoiceModel invoiceModel) {
//	String methodName = "updateCounter";
//	Map<String,String> map=new HashMap<>();
//	try {
//		log.info("Request : updateCounter for : companyGenId " + invoiceModel.getCompanyGenId() + "  Method Name"
//				+ methodName + " Class : " + this.getClass());
//
//		if (invoiceModel.getCompanyGenId() == null || invoiceModel.getCompanyGenId().trim().equals("")) {
//			log.info("Response : Invalid paramater for : companyGenId " + invoiceModel.getCompanyGenId() + "  Method Name"
//					+ methodName + " Class : " + this.getClass());
//			
//			return "failure";
//		}
//		
//		// financial year
//		Calendar calendar = Calendar.getInstance();
//		int currentYear = calendar.get(Calendar.YEAR) % 100;
//		int nextYearShort = (currentYear + 1) % 100;
//		String financialYear = String.format("%02d", currentYear) + "-" + String.format("%02d", nextYearShort);
//        
//		InvoiceNoCounter invoiceNoCounter = invoiceNoCounterRepo.findByRegCodeAndCompanyGenId(invoiceModel.getRegCode(), invoiceModel.getCompanyGenId());
//		if (invoiceNoCounter == null) {
//			invoiceNoCounter = new InvoiceNoCounter();
//			invoiceNoCounter.setCompanyGenId(invoiceModel.getCompanyGenId());
//			invoiceNoCounter.setInvoiceNo(invoiceModel.getInvoiceNo());
//			invoiceNoCounter.setCounter(1L);
//		} else {
//			String dateStr = currentYear + "-03-31 23:59:59";
//			Timestamp march31EndTimestamp = Timestamp.valueOf(dateStr);
//			Timestamp updatedDtm = invoiceNoCounter.getUpdatedDtm();
//			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
//
//			if (!updatedDtm.after(march31EndTimestamp) && currentTimestamp.after(march31EndTimestamp)) {
//				invoiceNoCounter.setCounter(0L);
//			}
//			formattedCounter = String.format("%05d",invoiceNoCounter.getCounter()+1);
//		}
//		
//		// abbrevation/financial year/counter
//		invoiceNo = registered.getRegAbbreviation() + "/" + financialYear + "/" + formattedCounter;
//		map.put("invoiceNo", invoiceNo);
//		
//		response.setData(map);
//		response.setHttpStatus(HttpStatus.OK);
//		response.setMessage("invoiceNo  generated successfully regCode :" +regCode + " and companyGenId : " + companyGenId);
//		log.info("invoiceNo  generated successfully , invoiceNo :  "+invoiceNo + "  Method Name" + methodName + " Class : "
//				+ this.getClass());
//
//	} catch (Exception e) {
//		log.error("error on -- generate next invoiceNo  for : companyGenId : "+ companyGenId +"Method Name" + methodName + " Class : "
//				+ this.getClass());
//		e.printStackTrace();
//	}
//	return response;
//}

//	
//private InvoiceFlatResponse mapToFlatResponse(Invoice invoice) {
//    InvoiceFlatResponse dto = new InvoiceFlatResponse();
//    dto.setId(invoice.getId());
//    dto.setUserId(invoice.getUserId());
//    dto.setCompanyGenId(invoice.getCompanyGenId());
//    dto.setInvoiceNo(invoice.getInvoiceNo());
//    dto.setDate(invoice.getDate());
//
//    // Consigner
//    if (invoice.getConsigner() != null) {
//        dto.setConsigner(invoice.getConsigner());
//
////        dto.setConsignerName(invoice.getConsigner().getName());
////        dto.setConsignerEmail(invoice.getConsigner().getEmail());
//    }
//
//    // Receiver
//    if (invoice.getReceiver() != null) {
//        dto.setReceiver(invoice.getReceiver());
//
//    }
//
//    // Consignee
//    if (invoice.getConsignee() != null) {
//        dto.setConsignee(invoice.getConsignee());
//    }
//
//    // Insurance Info
//    if (invoice.getInsuranceInfo() != null) {
//        dto.setInsuranceInfo(invoice.getInsuranceInfo());
//
//    }
//
//    // Totals
//    dto.setTotalValue(invoice.getTotalValue());
//
//    return dto;
//}
//
//
//
//@Override
//public ResponseModel findFlatById(Long id) { 
//	
//	ResponseModel response = new ResponseModel();
//    Optional<Invoice> optional = invoiceRepository.findById(id);
////System.out.println(optional);
//    if (optional.isPresent()) {
//        response.setHttpStatus("OK");
//        response.setMessage("Invoice found");
//        
//        response.setData(optional);
//    } else {
//        response.setHttpStatus("error");
//        response.setMessage("Invoice not found");
//        response.setData(null);
//    }
//
//    return response;
//}

}
