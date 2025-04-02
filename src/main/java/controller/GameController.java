package controller;

import dao.GameDAO;
import dto.GameDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameController implements IController {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());
    private final GameDAO dao;

    public GameController(EntityManagerFactory emf) {
        this.dao = GameDAO.getInstance(emf);
    }

    public GameDTO converter(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .name(game.getName())
                .licenses(game.getLicenses())
                .account(game.getAccount())
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
                ctx.json(dao.getAll());
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
            Game gameCreated = ctx.bodyAsClass(Game.class);
            if (gameCreated != null) {
                Game createdGame = dao.create(gameCreated);
                GameDTO gameDTO = converter(createdGame);
                ctx.json(gameDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
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
