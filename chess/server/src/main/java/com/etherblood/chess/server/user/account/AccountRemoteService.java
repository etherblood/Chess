package com.etherblood.chess.server.user.account;

import java.util.EnumSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.etherblood.chess.api.player.PlayerTo;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.authentication.model.Login;
import com.etherblood.chess.server.user.authentication.model.UserAuthority;

/**
 *
 * @author Philipp
 */
@RestController
@RequestMapping("/account")
public class AccountRemoteService {

	private final AccountService accountService;
	private final UserContextService userContextService;

	@Autowired
	public AccountRemoteService(AccountService userService, UserContextService userContextService) {
		this.accountService = userService;
		this.userContextService = userContextService;
	}

	@PreAuthorize(value = "permitAll()")
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public ModelAndView register(@RequestParam String loginHandle) {
		Login login = accountService.register(loginHandle);
		userContextService.forceLogin(login, EnumSet.of(UserAuthority.PLAYER));
		return new ModelAndView("redirect:/private/sandbox.html");
	}

	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	@RequestMapping(path = "/password", method = RequestMethod.POST)
	public void setPassword(@RequestParam String plaintextPassword) {
		accountService.setPassword(plaintextPassword);
	}

	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	@RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
	public PlayerTo getPlayerById(@PathVariable UUID accountId) {
		return makeTo(accountService.getAccountById(accountId));
	}

	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	@RequestMapping(path = "/self", method = RequestMethod.GET)
	public PlayerTo getCurrentPlayer() {
		return makeTo(accountService.getAccountById(userContextService.currentUserId()));
	}
	
	private PlayerTo makeTo(Account account) {
		PlayerTo to = new PlayerTo();
		to.id = account.getId();
		to.name = account.getUsername();
		return to;
	}

}
