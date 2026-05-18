package controller;

import dao.GameDAO;
import dto.GameDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.Role;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class GameController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final GameDAO dao;

    public GameController(EntityManagerFactory emf) {
        this.dao = GameDAO.getInstance(emf);
    }

    public GameDTO converter(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .name(game.getName())
                .licenses(game.getLicenses())
                .accountId(game.getAccount().getId())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            List<Game> games = dao.getAll();
            List<GameDTO> gameDTOS = new ArrayList<>();
            for (Game g : games) {
                GameDTO gameDTO = converter(g);
                gameDTOS.add(gameDTO);
            }
            ctx.status(200).json(gameDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Game game = dao.getById(id);
            GameDTO gameDTO = converter(game);
            if (gameDTO != null) {
                ctx.status(200).json(gameDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Game incoming = ctx.bodyAsClass(Game.class);

            if (incoming == null) {
                throw new ApiException(400, "Missing game data.", timestamp);
            }
            if (incoming.getName() == null) {
                throw new ApiException(400, "Missing game name.", timestamp);
            }
            if (incoming.getAccount() == null || incoming.getAccount().getId() == null) {
                throw new ApiException(400, "Missing account ID.", timestamp);
            }

            Account account = dao.getAccountById(incoming.getAccount().getId());
            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + incoming.getAccount().getId(), timestamp);
            }

            Role role = dao.getRoleByName(account.getRole().getName());
            account.setRole(role);

            Game game = new Game(incoming.getName(), account);
            game.setAccount(account);
            Game createdGame = dao.create(game);
            GameDTO gameDTO = converter(createdGame);

            ctx.status(201).json(gameDTO);
        };
    }

    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.getById(id);
            Game gameToUpdate = ctx.bodyAsClass(Game.class);

            if (gameToUpdate.getAccount() == null || gameToUpdate.getAccount().getId() == null) {
                throw new ApiException(400, "Missing account information", timestamp);
            }

            Account account = dao.getAccountById(gameToUpdate.getAccount().getId());
            if (account == null) {
                throw new ApiException(404, "Account not found", timestamp);
            }

            gameToUpdate.setId(id);
            gameToUpdate.setAccount(account);

            Game gameUpdated = dao.update(gameToUpdate);

            GameDTO gameDTO = converter(gameUpdated);
            if (gameDTO != null) {
                ctx.status(200).json(gameDTO);
            } else {
                throw new ApiException(404, "No data found.", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Game gameDeleted = dao.delete(id);
            if (gameDeleted != null) {
                GameDTO gameDTO = converter(gameDeleted);
                ctx.status(200).json(gameDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
