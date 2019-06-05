package com.qa.repository;

import java.util.List;

import com.qa.model.Account;

public interface AccountRepository {

	public Account create(Account account);
	
	public Account read(int id);
	public List<Account> readAll();	
	
	public Account update(int id, Account account);
	
	public void delete(int id);
	
	
}
