package com.userfront.Service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.Dao.CurrentAccountDao;
import com.userfront.Dao.CurrentTranscationDao;
import com.userfront.Dao.RecipientDao;
import com.userfront.Dao.SavingAccountDao;
import com.userfront.Dao.SavingTranscationDao;
import com.userfront.Domain.CurrentAccount;
import com.userfront.Domain.CurrentTranscation;
import com.userfront.Domain.Recipient;
import com.userfront.Domain.SavingAccount;
import com.userfront.Domain.SavingTranscation;
import com.userfront.Domain.User;
import com.userfront.Service.TranscationService;
import com.userfront.Service.UserService;

@Service
public class TranscationServiceImpl implements TranscationService {

	@Autowired
	private UserService userService;

	@Autowired
	private CurrentAccountDao currentAccountDao;

	@Autowired
	private SavingAccountDao savingAccountDao;

	@Autowired
	private CurrentTranscationDao currentTranscationDao;

	@Autowired
	private SavingTranscationDao savingTranscationDao;
	
	@Autowired
	private RecipientDao recipientDao;

	public List<CurrentTranscation> findCurrentTranscationList(String username) {
		// TODO Auto-generated method stub
		User user = userService.findByUserName(username);
		List<CurrentTranscation> currentTranscationList = user.getCurrentAccount().getCurrentTranscationList();
		return currentTranscationList;
	}

	public List<SavingTranscation> findSavingTranscationList(String username) {
		// TODO Auto-generated method stub
		User user = userService.findByUserName(username);
		List<SavingTranscation> savingTranscationList = user.getSavingAccount().getSavingTranscationList();

		return savingTranscationList;
	}

	public void saveCurrentDepositTranscation(CurrentTranscation currentTranscation) {
		// TODO Auto-generated method stub

		currentTranscationDao.save(currentTranscation);
	}

	public void saveSavingDepositTranscation(SavingTranscation savingTranscation) {
		// TODO Auto-generated method stub

		savingTranscationDao.save(savingTranscation);

	}

	public void saveCurrentWithdrawTranscation(CurrentTranscation currentTranscation) {
		// TODO Auto-generated method stub
		currentTranscationDao.save(currentTranscation);
	}

	public void saveSavingWithdrawTranscation(SavingTranscation savingTranscation) {
		// TODO Auto-generated method stub
		savingTranscationDao.save(savingTranscation);

	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			CurrentAccount currentAccount, SavingAccount savingAccount) throws Exception {
		// TODO Auto-generated method stub

		if (transferFrom.equalsIgnoreCase("Current") && transferTo.equalsIgnoreCase("Saving")) {
			currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingAccount.setAccountBalance(savingAccount.getAccountBalance().add(new BigDecimal(amount)));
			currentAccountDao.save(currentAccount);
			savingAccountDao.save(savingAccount);

			Date date = new Date();

			CurrentTranscation currentTranscation = new CurrentTranscation(date,
					"Between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished",
					Double.parseDouble(amount), currentAccount.getAccountBalance(), currentAccount);
			currentTranscationDao.save(currentTranscation);
		} else if (transferFrom.equalsIgnoreCase("Saving") && transferTo.equalsIgnoreCase("Current")) {
			currentAccount.setAccountBalance(currentAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingAccount.setAccountBalance(savingAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			currentAccountDao.save(currentAccount);
			savingAccountDao.save(savingAccount);

			Date date = new Date();

			SavingTranscation savingTranscation = new SavingTranscation(date,
					"Between account transfer from " + transferFrom + " to " + transferTo, "Transfer", "Finished",
					Double.parseDouble(amount), savingAccount.getAccountBalance(), savingAccount);
			savingTranscationDao.save(savingTranscation);
		} else {
			throw new Exception("Invalid Transfer");
		}

	}

	@Override
	public List<Recipient> findRecipientList(Principal principal) {
		 String username = principal.getName();
	        List<Recipient> recipientList = recipientDao.findAll().stream() 			//convert list to stream
	                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	//filters the line, equals to username
	                .collect(Collectors.toList());

	        return recipientList;
	}

	@Override
	public Recipient saveRecipient(Recipient recipient) {
		// TODO Auto-generated method stub
		return recipientDao.save(recipient);
	}

	@Override
	public Recipient findRecipientByName(String recipientName) {
		// TODO Auto-generated method stub
		return recipientDao.findByName(recipientName);
	}

	@Override
	public void deleteRecipientByName(String recipientName) {
		// TODO Auto-generated method stub
		recipientDao.deleteByName(recipientName);
	}

	@Override
	public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
			CurrentAccount currentAccount, SavingAccount savingAccount) {
		 if (accountType.equalsIgnoreCase("Current")) {
	            currentAccount.setAccountBalance(currentAccount.getAccountBalance().subtract(new BigDecimal(amount)));
	            currentAccountDao.save(currentAccount);

	            Date date = new Date();

	            CurrentTranscation currentTranscation = new CurrentTranscation(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), currentAccount.getAccountBalance(), currentAccount);
	            currentTranscationDao.save(currentTranscation);
	        } else if (accountType.equalsIgnoreCase("Saving")) {
	            savingAccount.setAccountBalance(savingAccount.getAccountBalance().subtract(new BigDecimal(amount)));
	            savingAccountDao.save(savingAccount);

	            Date date = new Date();

	            SavingTranscation savingTranscation  = new SavingTranscation(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingAccount.getAccountBalance(), savingAccount);
	            savingTranscationDao.save(savingTranscation);
	        }
	    }

}
