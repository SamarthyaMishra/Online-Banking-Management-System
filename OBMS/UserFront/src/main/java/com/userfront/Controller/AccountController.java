package com.userfront.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.Domain.CurrentAccount;
import com.userfront.Domain.CurrentTranscation;
import com.userfront.Domain.SavingAccount;
import com.userfront.Domain.SavingTranscation;
import com.userfront.Domain.User;
import com.userfront.Service.AccountService;
import com.userfront.Service.TranscationService;
import com.userfront.Service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TranscationService transcationService;

	@RequestMapping("/currentAccount")
	public String currentAccount(Model model, Principal principal) {
		List<CurrentTranscation> currentTranscationList = transcationService
				.findCurrentTranscationList(principal.getName());
		User user = userService.findByUserName(principal.getName());
		CurrentAccount currentAccount = user.getCurrentAccount();
		model.addAttribute("currentAccount", currentAccount);
		model.addAttribute("currentTranscationList", currentTranscationList);
		return "currentAccount";
	}

	@RequestMapping("/savingAccount")
	public String savingAccount(Model model, Principal principal) {
		List<SavingTranscation> savingTranscationList = transcationService
				.findSavingTranscationList(principal.getName());
		User user = userService.findByUserName(principal.getName());
		SavingAccount savingAccount = user.getSavingAccount();
		model.addAttribute("savingAccount", savingAccount);
		model.addAttribute("savingTranscationList", savingTranscationList);
		return "savingAccount";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit(Model model) {
		model.addAttribute("accountType", "");
		model.addAttribute("amount", "");

		return "deposit";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public String depositPOST(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal) {
		accountService.deposit(accountType, Double.parseDouble(amount), principal);
		return "redirect:/userFront";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw(Model model) {
		model.addAttribute("accountType", "");
		model.addAttribute("amount", "");

		return "withdraw";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdrawPOST(@ModelAttribute("amount") String amount,
			@ModelAttribute("accountType") String accountType, Principal principal) {
		accountService.withdraw(accountType, Double.parseDouble(amount), principal);

		return "redirect:/userFront";
	}
}
