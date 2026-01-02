package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Ticket;

@Repository
public interface TicketRepo extends JpaRepository<Ticket,Long>{
    
	@Query(value="SELECT MAX(t.ticketId) FROM Ticket t")
	Long findMaxValue();
	
	Ticket findByTicketId(String tickeId);
}
