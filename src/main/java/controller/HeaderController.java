package controller;

import dao.HeaderDAO;
import dto.HeaderDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Header;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class HeaderController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final HeaderDAO dao;

    public HeaderController(EntityManagerFactory emf) {
        this.dao = HeaderDAO.getInstance(emf);
    }

    public HeaderDTO converter(Header header) {
        return HeaderDTO.builder()
                .id(header.getId())
                .text(header.getText())
                .role(header.getRole())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            List<Header> headers = dao.getAll();
            List<HeaderDTO> headerDTOS = new ArrayList<>();
            for (Header h : headers) {
                HeaderDTO headerDTO = converter(h);
                headerDTOS.add(headerDTO);
            }
            ctx.status(200).json(headerDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Header header = dao.getById(id);
            HeaderDTO headerDTO = converter(header);
            ctx.status(200).json(headerDTO);
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Header inputHeader = ctx.bodyAsClass(Header.class);
            if (inputHeader != null &&
                    inputHeader.getText() != null &&
                    inputHeader.getRole() != null &&
                    inputHeader.getRole().getName() != null) {
                Header createdHeader = dao.create(inputHeader);
                HeaderDTO headerDTO = converter(createdHeader);
                ctx.status(201).json(headerDTO);
            } else {
                throw new ApiException(400, "Text and role are required.", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.getById(id);
            Header headerToUpdate = ctx.bodyAsClass(Header.class);
            headerToUpdate.setId(id);
            Header headerUpdated = dao.update(headerToUpdate);
            HeaderDTO headerDTO = converter(headerUpdated);
            ctx.status(200).json(headerDTO);
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Header headerDeleted = dao.delete(id);
            if (headerDeleted != null) {
                HeaderDTO headerDTO = converter(headerDeleted);
                ctx.status(200).json(headerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
