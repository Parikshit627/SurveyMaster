package com.insuretech.survey.service;

import com.insuretech.survey.model.ResponseModel;

public interface AccountService {

	ResponseModel getEmailAndName(String companyGenId);

}
