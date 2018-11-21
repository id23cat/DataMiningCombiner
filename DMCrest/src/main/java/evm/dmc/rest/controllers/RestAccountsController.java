package evm.dmc.rest.controllers;

import evm.dmc.api.model.account.Account;
import evm.dmc.model.service.AccountService;
import evm.dmc.rest.annotation.HateoasRelation;
import evm.dmc.rest.annotation.HateoasRelationChildren;
import evm.dmc.webApi.dto.AccountDto;
import evm.dmc.webApi.exceptions.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(RestAccountsController.BASE_URL)
@HateoasRelationChildren({RestProjectController.class})
@Slf4j
public class RestAccountsController extends AbstractRestCrudController<AccountDto> {

    final static String BASE_URL = "/rest/user";

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Autowired
    public RestAccountsController(AccountService accountService, ModelMapper modelMapper) {

        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @Override
    @GetMapping("/{accountId}")
    @HateoasRelation("getAccount")
    public AccountDto getInstance(@PathVariable Long accountId, Long projectId, Long entityId) {

        return convertToDto(accountService.get(accountId)
                .orElseThrow(AccountNotFoundException.supplier(accountId)));
    }

    @Override
    @GetMapping("/all")
    @HateoasRelation("getAccountList")
    public List<AccountDto> getInstanceList(Long accountId, Long projectId) {

        List<Account> accountList = accountService.getAllAsList();
        List<AccountDto> dtoList = new ArrayList<>(accountList.size());
        for (Account account : accountList) {
            dtoList.add(convertToDto(account));
        }
        return dtoList;
    }

    @Override
    @PostMapping
    @HateoasRelation("addAccount")
    public AccountDto addInstance(AccountDto dto, Long accountId, Long projectId) {

        Account account = accountService.save(convertToEntity(dto))
                .orElseThrow(RuntimeException::new);
        return convertToDto(account);
    }

    @Override
    @PutMapping
    @HateoasRelation("updateAccount")
    public AccountDto updateInstance(AccountDto dto, Long accountId, Long projectId) {

        Account account = accountService.save(convertToEntity(dto))
                .orElseThrow(RuntimeException::new);
        return convertToDto(account);
    }

    @Override
    @DeleteMapping("/{accountId}")
    @HateoasRelation("deleteAccount")
    public AccountDto deleteInstance(@PathVariable Long accountId, Long projectId, Long entityId) {

        Account account = accountService.get(accountId)
                .orElseThrow(RuntimeException::new);
        accountService.delete(account);
        return convertToDto(account);
    }

    private AccountDto convertToDto(Account account) {

        return modelMapper.map(account, AccountDto.class);
    }

    private Account convertToEntity(AccountDto dto) {

        return modelMapper.map(dto, Account.class);
    }
}
