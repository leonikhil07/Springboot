package com.tus.accounts.repository;

import org.springframework.stereotype.Repository;
import com.tus.accounts.entity.Customer;

import jakarta.transaction.Transactional;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Optional<Customer> findByMobileNumber(String mobile);

	@Transactional
	@Modifying
	void deleteByCustomerId(Long customerId);

}
