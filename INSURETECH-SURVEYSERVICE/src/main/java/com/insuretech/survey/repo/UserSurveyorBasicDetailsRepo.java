package com.insuretech.survey.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insuretech.survey.entity.UserSurveyorBasicDetails;

public interface UserSurveyorBasicDetailsRepo extends JpaRepository<UserSurveyorBasicDetails, Long> {

    
    UserSurveyorBasicDetails findFirstByOrderByInsuranceClaimIdDesc();

    UserSurveyorBasicDetails findByAssetAndCompanyGenId(String subjectMatter, String companyRegGenId);

    UserSurveyorBasicDetails findByReferenceNoAndCompanyGenId(String referenceNumber, String companyRegGenId);


    UserSurveyorBasicDetails findByInsuranceClaimIdAndCompanyGenId(Long insuranceClaimId, String companyRegGenId);
    UserSurveyorBasicDetails findByInsuranceClaimId(Long insuranceClaimId);
    

        @Query("SELECT u FROM UserSurveyorBasicDetails u WHERE " +
                "(u.insuranceClaimId = :refId OR :refId IS NULL) AND " +
                "(u.asset = :vehicleNo OR :vehicleNo IS NULL)")
        UserSurveyorBasicDetails findByInsuranceClaimIdOrAsset(@Param("refId") Long refId, @Param("vehicleNo") String vehicleNo);

        UserSurveyorBasicDetails findByAsset(String regNo);


        UserSurveyorBasicDetails findByPolicyNo(String policyNo);

        UserSurveyorBasicDetails findByInsuredMobile(Long phoneNo);

          //      Change -- Aman -- START   
//      @Query("SELECT COUNT(u) FROM UserSurveyorBasicDetails u WHERE u.currentStatus IN :statuses")
//      long countByCurrentStatuses(@Param("statuses") List<Integer> statuses);

//      @Query("SELECT COUNT(u) FROM UserSurveyorBasicDetails u WHERE u.currentStatus IN :statuses AND u.createdBy = :createdBy")
//      long countByCurrentStatusesAndCreatedBy(
//          @Param("statuses") List<Integer> statuses,
//          @Param("createdBy") String createdBy
//      );

        UserSurveyorBasicDetails findByReferenceNo(String referenceNo);

        long countByCurrentStatusInAndSurveyorEmail(List<Integer> currentStatuses, String email);

//      long countByCreatedBy(String createdBy);
//      Change -- Aman -- END 
        
//      Change -- Aman -- START ---- MAIN DASHBOARD --- dashboardType - ALL, PENDING, CLOSED CASED
        
//Initiator
        @Query("SELECT u FROM UserSurveyorBasicDetails u WHERE u.createdBy = :createdBy AND u.companyGenId = :companyGenId " +
                "AND (:dashboardType = 'all' " +
                "     OR (:dashboardType = 'closed' AND u.currentStatus = 26) " +
                "     OR (:dashboardType = 'cancel' AND u.currentStatus = 30)" +
                "     OR (:dashboardType = 'pending' AND u.currentStatus != 26 AND u.currentStatus != 30)) " +
                "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                "ORDER BY u.insuranceClaimId DESC " +
                "LIMIT :limit OFFSET :offset")
 List<UserSurveyorBasicDetails> findByCreatedByAndCompanyGenIdAndDashboardAndForDashboardTypeRangeAndBranchIdOrderByInsuranceClaimIdDesc(
         @Param("createdBy") String userSurveyorLoginId,
         @Param("companyGenId") String companyRegGenId,
         @Param("dashboardType") String dashboardType,
         @Param("offset") int offset,
         @Param("limit") int limit,
         @Param("branchId") Long branchId);
        
//  Admin
        @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                "WHERE u.companyGenId = :companyGenId " +
                "AND (:dashboardType = 'all' " +
                "     OR (:dashboardType = 'closed' AND u.currentStatus = 26) " +
                "     OR (:dashboardType = 'cancel' AND u.currentStatus = 30)" +
                "     OR (:dashboardType = 'pending' AND u.currentStatus != 26 AND u.currentStatus != 30)) " +
                "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                "ORDER BY u.insuranceClaimId DESC " +
                "LIMIT :limit OFFSET :offset")
        List<UserSurveyorBasicDetails> findByCompanyGenIdAndDashboardAndForDashboardTypeRangeAndBranchIdOrderByInsuranceClaimIdDesc(
                @Param("companyGenId") String companyGenId,
                @Param("dashboardType") String dashboardType,
                @Param("offset") int offset,
                @Param("limit") int limit,
                @Param("branchId") Long branchId);

//Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
        @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                "WHERE u.assignTo = :assignTo " +
                "AND u.companyGenId = :companyGenId " +
                "AND (:dashboardType = 'all' " +
                "     OR (:dashboardType = 'closed' AND u.currentStatus = 26) " +
                "     OR (:dashboardType = 'cancel' AND u.currentStatus = 30)" +
                "     OR (:dashboardType = 'pending' AND u.currentStatus != 26 AND u.currentStatus != 30)) " +
                "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                "ORDER BY u.insuranceClaimId DESC " +
                "LIMIT :limit OFFSET :offset")
        List<UserSurveyorBasicDetails> findByAssignToAndCompanyGenIdAndForDashboardTypeRangeAndBranchIdOrderByInsuranceClaimIdDesc(
                @Param("assignTo") String assignTo,
                @Param("companyGenId") String companyGenId,
                @Param("dashboardType") String dashboardType,
                @Param("offset") int offset,
                @Param("limit") int limit,
                @Param("branchId") Long branchId);

//      Change -- Aman -- END ---- MAIN DASHBOARD --- dashboardType - ALL, PENDING, CLOSED CASED
        
        
//      Change -- Aman -- Start ---- MAIN DASHBOARD Count ALL, PENDING, CLOSED, Cancelled CASED     
        //Initiator
        @Query("SELECT " +
                   "COUNT(*) AS all_count, " +
                   "SUM(CASE WHEN u.currentStatus != 26 AND u.currentStatus != 30 THEN 1 ELSE 0 END) AS pending_count, " +
                   "SUM(CASE WHEN u.currentStatus = 26 THEN 1 ELSE 0 END) AS closed_count, " +
                   "SUM(CASE WHEN u.currentStatus = 30 THEN 1 ELSE 0 END) AS cancelled_count, " +
                   "SUM(CASE WHEN u.createdDate >= :startOfToday AND u.createdDate < :startOfTomorrow THEN 1 ELSE 0 END) AS today_count " +
                   "FROM UserSurveyorBasicDetails u " +
                   "WHERE u.createdBy = :createdBy " +
                   "AND u.companyGenId = :companyGenId " +
                   "AND (u.branchId = :branchId OR :branchId IS NULL)")
            Map<String, Long> countAllPendingClosedCancelledByCreatedByAndCompanyGenIdAndBranchId(
                    @Param("createdBy") String createdBy,
                    @Param("companyGenId") String companyGenId,
                    @Param("branchId") Long branchId,
                    @Param("startOfToday") LocalDateTime startOfToday,
                    @Param("startOfTomorrow") LocalDateTime startOfTomorrow);



//      Admin    
        @Query("SELECT " +
                   "COUNT(*) AS all_count, " +
                   "SUM(CASE WHEN u.currentStatus != 26 AND u.currentStatus != 30 THEN 1 ELSE 0 END) AS pending_count, " +
                   "SUM(CASE WHEN u.currentStatus = 26 THEN 1 ELSE 0 END) AS closed_count, " +
                   "SUM(CASE WHEN u.currentStatus = 30 THEN 1 ELSE 0 END) AS cancelled_count, " +
                   "SUM(CASE WHEN u.createdDate >= :startOfToday AND u.createdDate < :startOfTomorrow THEN 1 ELSE 0 END) AS today_count " +
                   "FROM UserSurveyorBasicDetails u " +
                   "WHERE u.companyGenId = :companyGenId " +
                   "AND (u.branchId = :branchId OR :branchId IS NULL)")
            Map<String, Long> countAllPendingClosedCancelledByCompanyGenIdAndBranchId(
                    @Param("companyGenId") String companyGenId,
                    @Param("branchId") Long branchId,
                    @Param("startOfToday") LocalDateTime startOfToday,
                    @Param("startOfTomorrow") LocalDateTime startOfTomorrow);

        
        
//Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
        @Query("SELECT " +
                   "COUNT(*) AS all_count, " +
                   "SUM(CASE WHEN u.currentStatus != 26 AND u.currentStatus != 30 THEN 1 ELSE 0 END) AS pending_count, " +
                   "SUM(CASE WHEN u.currentStatus = 26 THEN 1 ELSE 0 END) AS closed_count, " +
                   "SUM(CASE WHEN u.currentStatus = 30 THEN 1 ELSE 0 END) AS cancelled_count, " +
                   "SUM(CASE WHEN u.createdDate >= :startOfToday AND u.createdDate < :startOfTomorrow THEN 1 ELSE 0 END) AS today_count " +
                   "FROM UserSurveyorBasicDetails u " +
                   "WHERE u.assignTo = :assignTo " +
                   "AND u.companyGenId = :companyGenId " +
                   "AND (u.branchId = :branchId OR :branchId IS NULL)")
            Map<String, Long> countAllPendingClosedCancelledByAssignToAndCompanyGenIdAndBranchId(
                    @Param("assignTo") String assignTo,
                    @Param("companyGenId") String companyGenId,
                    @Param("branchId") Long branchId,
                    @Param("startOfToday") LocalDateTime startOfToday,
                    @Param("startOfTomorrow") LocalDateTime startOfTomorrow);
        
//      Change -- Aman -- End ---- MAIN DASHBOARD Count ALL, PENDING, CLOSED, Cancelled CASED   
        
        
//      Change -- Aman -- Start ---- MAIN DASHBOARD Today Case  
        //Initiator
        @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                   "WHERE u.createdBy = :createdBy " +
                   "AND u.companyGenId = :companyGenId " +
                   "AND u.branchId = :branchId OR :branchId IS NULL " +
                   "AND DATE(u.createdDate) = :today " +
                   "ORDER BY u.insuranceClaimId DESC " +
                   "LIMIT :limit OFFSET :offset")
            List<UserSurveyorBasicDetails> findByCreatedByAndCompanyGenIdAndBranchIdAndCreatedTodayOrderByInsuranceClaimIdDesc(
                    @Param("createdBy") String userSurveyorLoginId,
                    @Param("companyGenId") String companyRegGenId,
                    @Param("offset") int offset,
                    @Param("limit") int limit,
                    @Param("branchId") Long branchId,
                    @Param("today") LocalDate today);
        
//      Admin   
        @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                   "WHERE u.companyGenId = :companyGenId " +
                   "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                   "AND DATE(u.createdDate) = :today " +
                   "ORDER BY u.insuranceClaimId DESC " +
                   "LIMIT :limit OFFSET :offset")
            List<UserSurveyorBasicDetails> findByCompanyGenIdAndBranchIdAndCreatedTodayOrderByInsuranceClaimIdDesc(
                    @Param("companyGenId") String companyGenId,
                    @Param("offset") int offset,
                    @Param("limit") int limit,
                    @Param("branchId") Long branchId,
                    @Param("today") LocalDate today);
        
        //Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
        @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                   "WHERE u.assignTo = :assignTo " +
                   "AND u.companyGenId = :companyGenId " +
                   "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                   "AND DATE(u.createdDate) = :today " +
                   "ORDER BY u.insuranceClaimId DESC " +
                   "LIMIT :limit OFFSET :offset")
            List<UserSurveyorBasicDetails> findByAssignToAndCompanyGenIdAndBranchIdAndCreatedTodayOrderByInsuranceClaimIdDesc(
                    @Param("assignTo") String assignTo,
                    @Param("companyGenId") String companyGenId,
                    @Param("offset") int offset,
                    @Param("limit") int limit,
                    @Param("branchId") Long branchId,
                    @Param("today") LocalDate today);
//      Change -- Aman -- End ---- MAIN DASHBOARD Today Case        
        
        
//      Change -- Aman -- START --- Motor Dashboard 
      //Initiator
            @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                       "WHERE u.createdBy = :createdBy " +
                       "AND u.companyGenId = :companyGenId " +
                       "AND LOWER(u.department) = LOWER(:department)" +
                        "AND (:dashboardType = 'all' " +
                        "     OR (:dashboardType = 'closed' AND u.currentStatus = 26) " +
                        "     OR (:dashboardType = 'cancel' AND u.currentStatus = 30)" +
                        "     OR (:dashboardType = 'pending' AND u.currentStatus != 26)) " +
                        "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                        "ORDER BY u.insuranceClaimId DESC " +
                        "LIMIT :limit OFFSET :offset")
                List<UserSurveyorBasicDetails> findByCreatedByAndCompanyGenIdAndDepartmentIgnoreCaseOrderByInsuranceClaimIdDesc(
                        @Param("createdBy") String createdBy,
                        @Param("companyGenId") String companyGenId,
                        @Param("department") String department,
                         @Param("dashboardType") String dashboardType,
                        @Param("offset") int offset,
                        @Param("limit") int limit,
                        @Param("branchId") Long branchId);

//          Admin 
            @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                       "WHERE u.companyGenId = :companyGenId " +
                       "AND LOWER(u.department) = LOWER(:department) " +
                        "AND (:dashboardType = 'all' " +
                        "     OR (:dashboardType = 'closed' AND u.currentStatus = 26) " +
                        "     OR (:dashboardType = 'cancel' AND u.currentStatus = 30)" +
                        "     OR (:dashboardType = 'pending' AND u.currentStatus != 26)) " +
                        "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                        "ORDER BY u.insuranceClaimId DESC " +
                        "LIMIT :limit OFFSET :offset")
                List<UserSurveyorBasicDetails> findByCompanyGenIdAndDepartmentIgnoreCaseOrderByInsuranceClaimIdDesc(
                        @Param("companyGenId") String companyRegGenId,
                        @Param("department") String department,
                         @Param("dashboardType") String dashboardType,
                        @Param("offset") int offset,
                        @Param("limit") int limit,
                        @Param("branchId") Long branchId);
            
            //Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
        @Query("SELECT u FROM UserSurveyorBasicDetails u " +
                   "WHERE u.assignTo = :assignTo " +
                   "AND u.companyGenId = :companyGenId " +
                   "AND LOWER(u.department) = LOWER(:department)"+
                    "AND (:dashboardType = 'all' " +
                    "     OR (:dashboardType = 'closed' AND u.currentStatus = 26) " +
                    "     OR (:dashboardType = 'cancel' AND u.currentStatus = 30)" +
                    "     OR (:dashboardType = 'pending' AND u.currentStatus != 26)) " +
                    "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                    "ORDER BY u.insuranceClaimId DESC " +
                    "LIMIT :limit OFFSET :offset")
        List<UserSurveyorBasicDetails> findByAssignToAndCompanyGenIdAndDepartmentIgnoreCaseOrderByInsuranceClaimIdDesc(
                 @Param("assignTo") String assignTo,
                    @Param("companyGenId") String companyGenId,
                    @Param("department") String department,
                     @Param("dashboardType") String dashboardType,
                    @Param("offset") int offset,
                    @Param("limit") int limit,
                    @Param("branchId") Long branchId);
//  Change -- Aman -- End --- Motor Dashboard 

        UserSurveyorBasicDetails findByReferenceNoOrAsset(String refId, String vehicleNo);

//              @Query(
//                                value = "SELECT * FROM insuredb.user_surveyor_basic_details usb " +
//                                        "WHERE usb.asset = :vehicleNo " +
//                                        "AND usb.date_of_loss = :dol " +
//                                        "AND ( usb.asset ~ '^[A-Z]{2}-[0-9]{2}[A-Z]{2}-[0-9]{4}$' " + 
//                                        "     OR usb.asset ~ '^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$')",  
//                                nativeQuery = true
//                              )
                                UserSurveyorBasicDetails findByAssetAndDateOfloss(@Param("vehicleNo") String vehicleNo,
                                                                                  @Param("dol") Date dol);

                                

                        //  Change -- Aman -- Start -28-08-25   --- for search
                                //Initiator
                            //  Change -- Aman -- Start -03-09-25
                                @Query("SELECT u FROM UserSurveyorBasicDetails u WHERE u.createdBy = :createdBy AND u.companyGenId = :companyGenId " +
                                               "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                                               "AND (:searchText IS NULL OR :searchText = '' OR " +
                                               "UPPER(u.asset) LIKE UPPER(CONCAT('%', :searchText, '%')) OR UPPER(u.referenceNo) LIKE UPPER(CONCAT('%', :searchText, '%'))) " +
//                                               " OR u.insuredName LIKE %:searchText% OR u.policyNo LIKE %:searchText% OR " +
//                                               "u.claimNo LIKE %:searchText%) " +
                                               "ORDER BY u.insuranceClaimId DESC")
                                        List<UserSurveyorBasicDetails> findByCreatedByAndCompanyGenIdAndBranchIdOrderByInsuranceClaimIdDesc(
                                                         @Param("createdBy") String createdBy, 
                                                         @Param("companyGenId") String companyGenId, 
                                                         @Param("branchId") Long branchId, 
                                                         @Param("searchText") String searchText);
                                
//                    Admin 
                                @Query("SELECT u FROM UserSurveyorBasicDetails u WHERE u.companyGenId = :companyGenId " +
                                               "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                                               "AND (:searchText IS NULL OR :searchText = '' OR " +
                                               "UPPER(u.asset) LIKE UPPER(CONCAT('%', :searchText, '%')) OR UPPER(u.referenceNo) LIKE UPPER(CONCAT('%', :searchText, '%'))) " +
//                                               "u.insuredName LIKE %:searchText% OR u.policyNo LIKE %:searchText% OR " +
//                                               "u.claimNo LIKE %:searchText%) " +
                                               "ORDER BY u.insuranceClaimId DESC")
                                        List<UserSurveyorBasicDetails> findByCompanyGenIdAndBranchIdOrderByInsuranceClaimIdDesc(
                                                         @Param("companyGenId") String companyGenId, 
                                                         @Param("branchId") Long branchId, 
                                                         @Param("searchText") String searchText);


                                 //Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
                                @Query("SELECT u FROM UserSurveyorBasicDetails u WHERE u.assignTo = :assignTo " +
                                               "AND u.companyGenId = :companyGenId " +
                                               "AND (u.branchId = :branchId OR :branchId IS NULL) " +
                                               "AND (:searchText IS NULL OR :searchText = '' OR " +
                                               "UPPER(u.asset) LIKE UPPER(CONCAT('%', :searchText, '%')) OR UPPER(u.referenceNo) LIKE UPPER(CONCAT('%', :searchText, '%'))) " +
//                                               " OR u.insuredName LIKE %:searchText% OR u.policyNo LIKE %:searchText% OR " +
//                                               "u.claimNo LIKE %:searchText%) " +
                                               "ORDER BY u.insuranceClaimId DESC")
                                        List<UserSurveyorBasicDetails> findByAssignToAndCompanyGenIdAndBranchIdOrderByInsuranceClaimIdDesc(
                                                        @Param("assignTo") String assignTo, 
                                                         @Param("companyGenId") String companyGenId, 
                                                         @Param("branchId") Long branchId, 
                                                         @Param("searchText") String searchText);
                            //  Change -- Aman -- End -03-09-25
                        //  Change -- Aman -- End -28-08-25   --- for search


             // change --parikshit -- start -15-09-2025
             @Query("SELECT u FROM UserSurveyorBasicDetails u WHERE LOWER(TRIM(u.claimNo)) = LOWER(:claimNo)")
             UserSurveyorBasicDetails findByClaimNo(@Param("claimNo") String claimNo);
             // change --parikshit -- end -15-09-2025
}

