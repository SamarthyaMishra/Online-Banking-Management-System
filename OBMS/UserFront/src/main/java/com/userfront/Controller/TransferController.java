package com.userfront.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.userfront.Domain.CurrentAccount;
import com.userfront.Domain.Recipient;
import com.userfront.Domain.SavingAccount;
import com.userfront.Domain.User;
import com.userfront.Service.TranscationService;
import com.userfront.Service.UserService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private TranscationService transcationService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.GET)
	public String betweenAccounts(Model model) {
		model.addAttribute("transferFrom", "");
		model.addAttribute("transferTo", "");
		model.addAttribute("amount", "");

		return "betweenAccounts";
	}

	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
	public String betweenAccountsPost(@ModelAttribute("transferFrom") String transferFrom,
			@ModelAttribute("transferTo") String transferTo, @ModelAttribute("amount") String amount,
			Principal principal) throws Exception {
		User user = userService.findByUserName(principal.getName());
		CurrentAccount currentAccount = user.getCurrentAccount();
		SavingAccount savingAccount = user.getSavingAccount();
		transcationService.betweenAccountsTransfer(transferFrom, transferTo, amount, currentAccount, savingAccount);

		return "redirect:/userFront";
	}

	@RequestMapping(value = "/recipient", method = RequestMethod.GET)
	public String recipient(Model model, Principal principal) {
		List<Recipient> recipientList = transcationService.findRecipientList(principal);

		Recipient recipient = new Recipient();

		model.addAttribute("recipientList", recipientList);
		model.addAttribute("recipient", recipient);

		return "recipient";
	}

	@RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
	public String recipientPost(@ModelAttribute("recipient") Recipient recipient, Principal principal) {

		User user = userService.findByUserName(principal.getName());
		recipient.setUser(user);
		transcationService.saveRecipient(recipient);

		return "redirect:/transfer/recipient";
	}

	@RequestMapping(value = "/recipient/edit", method = RequestMethod.GET)
	public String recipientEdit(@RequestParam(value = "recipientName") String recipientName, Model model,
			Principal principal) {

		Recipient recipient = transcationService.findRecipientByName(recipientName);
		List<Recipient> recipientList = transcationService.findRecipientList(principal);

		model.addAttribute("recipientList", recipientList);
		model.addAttribute("recipient", recipient);

		return "recipient";
	}

	@RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
	@Transactional
	public String recipientDelete(@RequestParam(value = "recipientName") String recipientName, Model model,
			Principal principal) {

		transcationService.deleteRecipientByName(recipientName);

		List<Recipient> recipientList = transcationService.findRecipientList(principal);

		Recipient recipient = new Recipient();
		model.addAttribute("recipient", recipient);
		model.addAttribute("recipientList", recipientList);

		return "recipient";
	}

	@RequestMapping(value = "/toSomeoneElse", method = RequestMethod.GET)
	public String toSomeoneElse(Model model, Principal principal) {
		List<Recipient> recipientList = transcationService.findRecipientList(principal);

		model.addAttribute("recipientList", recipientList);
		model.addAttribute("accountType", "");

		return "toSomeoneElse";
	}

	@RequestMapping(value = "/toSomeoneElse", method = RequestMethod.POST)
	public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName,
			@ModelAttribute("accountType") String accountType, @ModelAttribute("amount") String amount,
			Principal principal) {
		User user = userService.findByUserName(principal.getName());
		Recipient recipient = transcationService.findRecipientByName(recipientName);
		transcationService.toSomeoneElseTransfer(recipient, accountType, amount, user.getCurrentAccount(),
				user.getSavingAccount());

		return "redirect:/userFront";
	}
}
