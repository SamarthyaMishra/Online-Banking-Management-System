package com.userfront.Service;

import java.security.Principal;

import com.userfront.Domain.CurrentAccount;
import com.userfront.Domain.SavingAccount;

public interface AccountService {

	CurrentAccount createCurrentAccount();
	SavingAccount  createSavingAccount();
	void deposit(String accountType, double amount, Principal principal);
	void withdraw(String accountType, double amount, Principal principal);
}
	
