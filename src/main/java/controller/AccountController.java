package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountController implements IController {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());
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
            if (!dao.getAll().isEmpty()) {
                List<Account> accounts = dao.getAll();
                List<AccountDTO> accountDTOS = new ArrayList<>();
                for (Account a : accounts) {
                    AccountDTO accountDTO = converter(a);
                    accountDTOS.add(accountDTO);
                }
                ctx.json(accountDTOS);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Account account = dao.getById(id);
            AccountDTO accountDTO = converter(account);
            if (accountDTO != null) {
                ctx.json(accountDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Account accountCreated = ctx.bodyAsClass(Account.class);
            if (accountCreated != null) {
                Account createdAccount = dao.create(accountCreated);
                AccountDTO accountDTO = converter(createdAccount);
                ctx.json(accountDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Account accountToUpdate = ctx.bodyAsClass(Account.class);
            accountToUpdate.setId(id);
            Account accountUpdated = dao.update(accountToUpdate);
            AccountDTO accountDTO = converter(accountUpdated);
            if (accountDTO != null) {
                ctx.json(accountDTO);
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
            AccountDTO accountDTO = converter(accountDeleted);
            if (accountDTO != null) {
                ctx.json(accountDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}