package com.etherblood.chess.server.user.account;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.etherblood.chess.api.player.PlayerTo;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.authentication.model.UserAuthority;

/**
 *
 * @author Philipp
 */
@RestController
public class AccountRemoteService {

	private final AccountService accountService;
	private final UserContextService userContextService;

	@Autowired
	public AccountRemoteService(AccountService userService, UserContextService userContextService) {
		this.accountService = userService;
		this.userContextService = userContextService;
	}

	@PreAuthorize(value = "permitAll()")
	@RequestMapping(path = "/account/register", method = RequestMethod.POST)
	public ModelAndView register(@RequestParam String loginHandle) {
		Account account = accountService.register(loginHandle);
		userContextService.forceLogin(account, EnumSet.of(UserAuthority.PLAYER));
		return new ModelAndView("redirect:/private/sandbox.html");
	}

	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	@RequestMapping(path = "/account/password", method = RequestMethod.POST)
	public void setPassword(@RequestParam String plaintextPassword) {
		accountService.setPassword(plaintextPassword);
	}

	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	@RequestMapping(path = "/account/players", method = RequestMethod.GET)
	public List<PlayerTo> getPlayersByIds(List<UUID> ids) {
		return accountService.getAccountsByIds(ids).stream().map(this::makeTo).collect(Collectors.toList());
	}
	
	private PlayerTo makeTo(Account account) {
		PlayerTo to = new PlayerTo();
		to.id = account.getId();
		to.name = account.getLoginHandle();
		return to;
	}

}
