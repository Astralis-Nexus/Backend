package controller;

import dao.GameDAO;
import dto.GameDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Game;
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
            if (!dao.getAll().isEmpty()) {
                List<Game> games = dao.getAll();
                List<GameDTO> gameDTOS = new ArrayList<>();
                for (Game g : games) {
                    GameDTO gameDTO = converter(g);
                    gameDTOS.add(gameDTO);
                }
                ctx.json(gameDTOS);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Game game = dao.getById(id);
            GameDTO gameDTO = converter(game);
            if (gameDTO != null) {
                ctx.json(gameDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Game incoming = ctx.bodyAsClass(Game.class);
    
            if (incoming == null || incoming.getName() == null || incoming.getAccount() == null || incoming.getAccount().getId() == null) {
                throw new ApiException(400, "Game name and accountId are required.", timestamp);
            }
    
            int accountId = incoming.getAccount().getId();
    
            var account = dao.getAccountById(accountId);
            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + accountId, timestamp);
            }
    
            Game game = new Game(incoming.getName(), account);
    
            Game createdGame = dao.create(game);
            GameDTO gameDTO = converter(createdGame);
    
            ctx.json(gameDTO);
        };
    }
    

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Game gameToUpdate = ctx.bodyAsClass(Game.class);
            gameToUpdate.setId(id);
            Game gameUpdated = dao.update(gameToUpdate);
            GameDTO gameDTO = converter(gameUpdated);
            if (gameDTO != null) {
                ctx.json(gameDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Game gameDeleted = dao.delete(id);
            GameDTO gameDTO = converter(gameDeleted);
            if (gameDTO != null) {
                ctx.json(gameDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
