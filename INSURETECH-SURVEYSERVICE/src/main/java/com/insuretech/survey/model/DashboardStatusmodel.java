package com.insuretech.survey.model;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatusmodel {

	private long totalRequest;
    private long totalConclusion;
   
	
}
