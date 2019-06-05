package com.qa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.qa.model.Account;

@Transactional(value = TxType.SUPPORTS)
public class AccountDB implements AccountRepository{

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;
	
	@Transactional(value = TxType.REQUIRED)
	public Account create(Account account) {
		em.persist(account);
		return account;
	}

	public Account read(int id) {
		Account account = em.find(Account.class, id);
		return account;
	}
	
	public List<Account> readAll() {

		TypedQuery<Account> q = em.createQuery("Select acc from Account acc", Account.class);
		List<Account> list = q.getResultList();
		return list;
	}
	
	@Transactional(value = TxType.REQUIRED)
	public Account update(int id, Account newInfo) {
		Account account = read(id);
		account.setName(newInfo.getName());
		System.out.println(read(account.getId()).getName());
		return account;
	}

	@Transactional(value = TxType.REQUIRED)
	public void delete(int id) {
		em.remove(read(id));
	}

}
