package evm.dmc.rest.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evm.dmc.api.model.account.Account;
import evm.dmc.model.repositories.AccountRepository;
import evm.dmc.model.service.AccountService;
import evm.dmc.webApi.dto.AccountDto;
import evm.dmc.webApi.exceptions.AccountNotFoundException;

@RestController
@RequestMapping(RestAccountsControllerOld.BASE_URL)	// /rest/user
public class RestAccountsControllerOld {
	public final static String BASE_URL = "/rest/user";
	
	public final static String LINK_REL_accountsList = "accountsList";
	
	@Autowired
	private AccountService accountService;
	
	@Autowired AccountRepository accountRepository;
	
	@Autowired
    private ModelMapper modelMapper;
	
	public static AccountDto selfLink(AccountDto dto) {
		Link selfLink = linkTo(methodOn(RestAccountsControllerOld.class)
				.getAccount(dto.getAccountId()))
				.withSelfRel();
		dto.add(selfLink);
		return dto;
	}
	
	public static ResourceSupport accountsListLink(ResourceSupport resSupport) {
		Link listLink = linkTo(methodOn(RestAccountsControllerOld.class)
				.getAccountsList())
				.withRel(LINK_REL_accountsList);
		resSupport.add(listLink);
		return resSupport;
	}
	
	@GetMapping
	@Transactional
	public List<AccountDto> getAccountsList() {
		return accountService.getAll()
				.map(this :: convertToDto)
				.map((accDto) -> addLinks(accDto))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{accountId}")
	@Transactional
	public AccountDto getAccount(@PathVariable Long accountId) {
		return addLinks(convertToDto(accountRepository.findById(accountId)
				.orElseThrow(AccountNotFoundException.supplier(accountId))));
	}
	
	private AccountDto addLinks(AccountDto accDto) {
		selfLink(accDto);
		accountsListLink(accDto);
		RestProjectControllerOld.projectsListLink(accDto, accDto.getAccountId());
		return accDto;
	}
	
	private AccountDto convertToDto(Account acc) {
		return modelMapper.map(acc, AccountDto.class);
	}
	
//	private Account convertToEntity(AccountDto dto) {
//		return modelMapper.map(dto, Account.class);
//	}

}
