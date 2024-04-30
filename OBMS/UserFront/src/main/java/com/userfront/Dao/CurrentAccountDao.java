package com.userfront.Dao;

import org.springframework.data.repository.CrudRepository;

import com.userfront.Domain.CurrentAccount;

public interface CurrentAccountDao extends CrudRepository<CurrentAccount, Long> {

	CurrentAccount findByAccountNumber(int accountNumber);



}
