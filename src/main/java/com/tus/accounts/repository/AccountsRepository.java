package com.tus.accounts.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tus.accounts.entity.Accounts;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long>{
	
	Optional<Accounts> findByCustomerId(Long custId);
	
	@Transactional
	@Modifying
	void deleteByCustomerId(Long custId);

}
