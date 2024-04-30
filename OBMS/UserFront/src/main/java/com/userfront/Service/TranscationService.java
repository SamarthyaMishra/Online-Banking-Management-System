package com.userfront.Service;

import java.security.Principal;
import java.util.List;

import com.userfront.Domain.CurrentAccount;
import com.userfront.Domain.CurrentTranscation;
import com.userfront.Domain.Recipient;
import com.userfront.Domain.SavingAccount;
import com.userfront.Domain.SavingTranscation;

public interface TranscationService {

	List<CurrentTranscation> findCurrentTranscationList(String username);

	List<SavingTranscation> findSavingTranscationList(String username);

	void saveCurrentDepositTranscation(CurrentTranscation currentTranscation);

	void saveSavingDepositTranscation(SavingTranscation savingTranscation);

	void saveCurrentWithdrawTranscation(CurrentTranscation currentTranscation);

	void saveSavingWithdrawTranscation(SavingTranscation savingTranscation);

	void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, CurrentAccount currentAccount,
			SavingAccount savingAccount) throws Exception;

	List<Recipient> findRecipientList(Principal principal);

    Recipient saveRecipient(Recipient recipient);

	Recipient findRecipientByName(String recipientName);

	void deleteRecipientByName(String recipientName);

	void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, CurrentAccount currentAccount,
			SavingAccount savingAccount);

}