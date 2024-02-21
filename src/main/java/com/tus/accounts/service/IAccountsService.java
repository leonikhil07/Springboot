package com.tus.accounts.service;

import com.tus.accounts.dto.CustomerDto;

public interface IAccountsService {
	
	void createAccount(CustomerDto customerDto);
	CustomerDto fetchAccount(String mobile);
	boolean updateAccount(CustomerDto customerDto);
	boolean deleteAccount(String mobile);
}
