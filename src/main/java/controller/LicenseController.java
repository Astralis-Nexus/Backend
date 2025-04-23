package controller;

import dao.GameDAO;
import dao.LicenseDAO;
import dto.LicenseDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Game;
import persistence.model.License;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class LicenseController implements IController {
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = DateUtil.getTimestamp();
    private final LicenseDAO dao;

    public LicenseController(EntityManagerFactory emf) {
        this.dao = LicenseDAO.getInstance(emf);
    }

   
    public LicenseDTO converter(License license) {
        return LicenseDTO.builder()
                .id(license.getId())
                .username(license.getUsername())
                .password(license.getPassword())
                .email(license.getEmail())
                .pcNumber(license.getPcNumber())
                .game(license.getGame())
                .status(license.getStatus()) 
                .build();
    }
    
    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<License> licenses = dao.getAll();
                List<LicenseDTO> licenseDTOS = new ArrayList<>();
                for (License l : licenses) {
                    LicenseDTO licenseDTO = converter(l);
                    licenseDTOS.add(licenseDTO);
                }
                ctx.json(licenseDTOS);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            License license = dao.getById(id);
            if (license != null) {
                LicenseDTO licenseDTO = converter(license);
                ctx.json(licenseDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            LicenseDTO incoming = ctx.bodyAsClass(LicenseDTO.class);

            if (incoming == null || incoming.getGame().getId() == null) {
                throw new ApiException(400, "Game ID is required.", timestamp);
            }

            int gameId = incoming.getGame().getId();
            Game game = dao.getGameById(gameId);

            if (game == null) {
                throw new ApiException(404, "Game not found with ID: " + gameId, timestamp);
            }

            License license = new License(
                    incoming.getUsername(),
                    incoming.getPassword(),
                    incoming.getEmail(),
                    incoming.getPcNumber(),
                    game,
                    incoming.getStatus()
            );

            License createdLicense = dao.create(license);

            LicenseDTO responseDTO = LicenseDTO.builder()
                    .id(createdLicense.getId())
                    .username(createdLicense.getUsername())
                    .password(createdLicense.getPassword())
                    .email(createdLicense.getEmail())
                    .pcNumber(createdLicense.getPcNumber())
                    .game(createdLicense.getGame())
                    .status(createdLicense.getStatus())
                    .build();

            ctx.json(responseDTO);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            LicenseDTO incoming = ctx.bodyAsClass(LicenseDTO.class);

            Game game = dao.getGameById(incoming.getGame().getId());
            if (game == null) {
                throw new ApiException(404, "Game not found with ID: " + incoming.getGame().getId(), timestamp);
            }

            License licenseToUpdate = dao.getById(id);
            if (licenseToUpdate == null) {
                throw new ApiException(404, "License not found with ID: " + id, timestamp);
            }

            licenseToUpdate.setUsername(incoming.getUsername());
            licenseToUpdate.setPassword(incoming.getPassword());
            licenseToUpdate.setEmail(incoming.getEmail());
            licenseToUpdate.setPcNumber(incoming.getPcNumber());
            licenseToUpdate.setGame(game);
            licenseToUpdate.setStatus(incoming.getStatus());

            License licenseUpdated = dao.update(licenseToUpdate);
            ctx.json(converter(licenseUpdated));
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            License licenseDeleted = dao.delete(id);
            if (licenseDeleted != null) {
                LicenseDTO licenseDTO = converter(licenseDeleted);
                ctx.json(licenseDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
