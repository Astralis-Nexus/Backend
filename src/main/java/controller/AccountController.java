package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class AccountController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final AccountDAO dao;

    public AccountController(EntityManagerFactory emf) {
        this.dao = AccountDAO.getInstance(emf);
    }

    public AccountDTO converter(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .role(account.getRole())
                .todos(account.getTodos())
                .information(account.getInformations())
                .games(account.getGames())
                .qas(account.getQas())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            List<Account> accounts = dao.getAll();
            List<AccountDTO> accountDTOS = new ArrayList<>();
            for (Account a : accounts) {
                AccountDTO accountDTO = converter(a);
                accountDTOS.add(accountDTO);
            }
            ctx.status(200).json(accountDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Account account = dao.getById(id);
            AccountDTO accountDTO = converter(account);
            if (accountDTO != null) {
                ctx.status(200).json(accountDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            AccountDTO incoming = ctx.bodyAsClass(AccountDTO.class);
    
            if (incoming.getPassword() == null || incoming.getPassword().isBlank()) {
                throw new ApiException(400, "Password is required", timestamp);
            }
    
            String rawPassword = incoming.getPassword();
    
            Account account = new Account();
            account.setUsername(incoming.getUsername());
            account.setPassword(rawPassword);
            account.setRole(incoming.getRole());
    
            Account createdAccount = dao.create(account);
            ctx.status(201).json(converter(createdAccount));
        };
    }
    

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.getById(id);
            Account accountToUpdate = ctx.bodyAsClass(Account.class);
            accountToUpdate.setId(id);
            Account accountUpdated = dao.update(accountToUpdate);
            AccountDTO accountDTO = converter(accountUpdated);
            if (accountDTO != null) {
                ctx.status(200).json(accountDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Account accountDeleted = dao.delete(id);
            if (accountDeleted != null) {
                AccountDTO accountDTO = converter(accountDeleted);
                ctx.status(200).json(accountDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
