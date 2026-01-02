package com.insuretech.survey.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.insuretech.survey.model.EmailNameDTO;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.AccountService;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImp extends AbstractMasterRepository implements AccountService {

	@Autowired
	private EntityManager entityManager;

	@Override
    public ResponseModel getEmailAndName(String companyGenId) {
        ResponseModel model = new ResponseModel();
        try {
            List<Map<String, Object>> result = registeredRepo.findByCompanyRegGenIdAndRegistrationBy(companyGenId);

            List<EmailNameDTO> emailNameList = result.stream()
                .map(obj -> new EmailNameDTO(
                    (String) obj.get("column1"),
                    (String) obj.get("column2")
                ))
                .collect(Collectors.toList());

            log.info("Successfully retrieved {} records for companyGenId: {}", 
                     emailNameList.size(), companyGenId);
            
            model.setMessage("Data retrieved successfully");
            model.setData(emailNameList); 
            model.setHttpStatus(HttpStatus.SC_OK);
            
            return model;

        } catch (DataAccessException e) {
            log.error("Database error retrieving email and name for companyGenId: {}", 
                      companyGenId, e);
            model.setMessage("Failed to retrieve data: " + e.getMessage());
            model.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return model;
        } catch (Exception e) {
            log.error("Unexpected error retrieving email and name for companyGenId: {}", 
                      companyGenId, e);
            model.setMessage("Unexpected error occurred: " + e.getMessage());
            model.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return model;
        }
    }


}
