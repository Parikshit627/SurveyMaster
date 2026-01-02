package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.VehicleCompanyDetails;
import com.insuretech.survey.entity.VehicleMasterDetails;

@Repository
public interface VehicleCompanyDetailsRepo extends JpaRepository<VehicleCompanyDetails,Long>{

//	VehicleCompanyDetails findByCompanyId(long companyId);

	List<VehicleCompanyDetails> findByCompanyIdIn(Set<Long> companyIds);


	VehicleCompanyDetails findByCompnayIgnoreCase(String company);


	List<VehicleCompanyDetails> findByCompnayIgnoreCaseContaining(String searchKey);


//	VehicleCompanyDetails findByCompanyIgnoreCase(String company);

    
	
}
