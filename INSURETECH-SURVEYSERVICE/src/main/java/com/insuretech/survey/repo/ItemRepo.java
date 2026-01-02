package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>{

	Optional<Item> findByIdAndInvoiceId(long id, Long id2);

	List<Item> findByInvoiceId(Long invoiceId);

}
