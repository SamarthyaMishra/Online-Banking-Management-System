package com.userfront.Dao;

import org.springframework.data.repository.CrudRepository;

import com.userfront.Domain.SavingAccount;

public interface SavingAccountDao extends CrudRepository<SavingAccount, Long> {

	SavingAccount findByAccountNumber(int accountNumber);


}
