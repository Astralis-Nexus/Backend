package controller;

import dao.HeaderDAO;
import dto.HeaderDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HeaderController implements IController {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());
    private final HeaderDAO dao;

    public HeaderController(EntityManagerFactory emf) {
        this.dao = HeaderDAO.getInstance(emf);
    }

    public HeaderDTO converter(Header header) {
        return HeaderDTO.builder()
                .id(header.getId())
                .name(header.getName())
                .role(header.getRole())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<Header> headers = dao.getAll();
                List<HeaderDTO> headerDTOS = new ArrayList<>();
                for (Header h : headers) {
                    HeaderDTO headerDTO = converter(h);
                    headerDTOS.add(headerDTO);
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
            Header header = dao.getById(id);
            if (header != null) {
                HeaderDTO headerDTO = converter(header);
                ctx.json(headerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Header headerCreated = ctx.bodyAsClass(Header.class);
            if (headerCreated != null) {
                HeaderDTO headerDTO = converter(headerCreated);
                ctx.json(headerDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Header headerToUpdate = ctx.bodyAsClass(Header.class);
            headerToUpdate.setId(id);
            Header headerUpdated = dao.update(headerToUpdate);
            HeaderDTO headerDTO = converter(headerUpdated);
            if (headerDTO != null) {
                ctx.json(headerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Header headerDeleted = dao.delete(id);
            if (headerDeleted != null) {
                HeaderDTO headerDTO = converter(headerDeleted);
                ctx.json(headerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
