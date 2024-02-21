package com.tus.accounts.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tus.accounts.constants.AccountConstants;
import com.tus.accounts.dto.AccountsDto;
import com.tus.accounts.dto.CustomerDto;
import com.tus.accounts.entity.Accounts;
import com.tus.accounts.entity.Customer;
import com.tus.accounts.exception.CustomerExistenceException;
import com.tus.accounts.exception.ResourceNotFoundException;
import com.tus.accounts.mapper.AccountsMapper;
import com.tus.accounts.mapper.CustomerMapper;
import com.tus.accounts.repository.AccountsRepository;
import com.tus.accounts.repository.CustomerRepository;

@Service
public class AccountsServiceImpl implements IAccountsService {

	@Autowired
	private AccountsRepository ar;

	@Autowired
	private CustomerRepository cr;

	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer c = CustomerMapper.mapToCustomer(customerDto, new Customer());
		Optional<Customer> optCust = cr.findByMobileNumber(customerDto.getMobileNumber());
		if (optCust.isPresent()) {
			throw new CustomerExistenceException(
					"Customer already exists for mobile number: " + customerDto.getMobileNumber());
		}
		c.setCreatedBy("default");
		c.setUpdatedBy("default");
		c.setUpdatedAt(LocalDateTime.now());
		c.setCreatedAt(LocalDateTime.now().minusHours(3));
		Customer created = cr.save(c);
		ar.save(createNewAccount(created));

	}

	private Accounts createNewAccount(Customer c) {
		Accounts newAcc = new Accounts();
		newAcc.setCustomerId(c.getCustomerId());
		long random = 10000000000L + new Random().nextInt(999990000);
		newAcc.setAccountNumber(random);
		newAcc.setBranchAddress(AccountConstants.ADDRESS);
		newAcc.setAccountType(AccountConstants.SAVINGS);
		newAcc.setCreatedAt(LocalDateTime.now().minusHours(3));
		newAcc.setUpdatedAt(LocalDateTime.now());
		newAcc.setCreatedBy("acc_default");
		newAcc.setUpdatedBy("acc_default");

		return newAcc;
	}

	@Override
	public CustomerDto fetchAccount(String mobileNumber) {
		Customer customer = cr.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		Accounts accounts = ar.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
		return customerDto;
	}

	@Override
	public boolean updateAccount(CustomerDto customerDto) {

		boolean isUpdated = false;
		AccountsDto accountsDto = customerDto.getAccountsDto();
		if (accountsDto != null) {
			Accounts accounts = ar.findById(accountsDto.getAccountNumber())
					.orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber",
							accountsDto.getAccountNumber().toString()));
			AccountsMapper.mapToAccounts(accountsDto, accounts);
			accounts = ar.save(accounts);
			Long customerId = accounts.getCustomerId();
			Customer customer = cr.findById(customerId)
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())

					);
			CustomerMapper.mapToCustomer(customerDto, customer);
			cr.save(customer);
			isUpdated = true;
		}
		return isUpdated;
	}

	@Override
	public boolean deleteAccount(String mobileNumber) {
	Customer customer = cr.findByMobileNumber(mobileNumber).orElseThrow( () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber) );
	ar.deleteByCustomerId(customer.getCustomerId());
	cr.deleteById(customer.getCustomerId());
	return true;
	}

}
