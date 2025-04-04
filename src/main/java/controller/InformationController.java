package controller;

import dao.InformationDAO;
import dto.InformationDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Information;
import utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InformationController implements IController {
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = DateUtil.getTimestamp();
    private final InformationDAO dao;

    public InformationController(EntityManagerFactory emf) {
        this.dao = InformationDAO.getInstance(emf);
    }

    public InformationDTO converter(Information information) {
        return InformationDTO.builder()
                .id(information.getId())
                .description(information.getDescription())
                .account(information.getAccount())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<Information> informations = dao.getAll();
                List<InformationDTO> informationDTOS = new ArrayList<>();
                for (Information i : informations) {
                    InformationDTO informationDTO = converter(i);
                    informationDTOS.add(informationDTO);
                }
                ctx.json(informationDTOS);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Information information = dao.getById(id);
            if (information != null) {
                InformationDTO informationDTO = converter(information);
                ctx.json(informationDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Information informationCreated = ctx.bodyAsClass(Information.class);
            if (informationCreated != null) {
                Information information = dao.create(informationCreated);
                InformationDTO informationDTO = converter(information);
                ctx.json(informationDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Information informationToUpdate = ctx.bodyAsClass(Information.class);
            informationToUpdate.setId(id);
            Information informationUpdated = dao.update(informationToUpdate);
            if (informationUpdated != null) {
                InformationDTO informationDTO = converter(informationUpdated);
                ctx.json(informationDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Information informationDeleted = dao.delete(id);
            if (informationDeleted != null) {
                InformationDTO informationDTO = converter(informationDeleted);
                ctx.json(informationDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
