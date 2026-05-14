package controller;

import dao.FooterDAO;
import dto.FooterDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Footer;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class FooterController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final FooterDAO dao;

    public FooterController(EntityManagerFactory emf) {
        this.dao = FooterDAO.getInstance(emf);
    }

    public FooterDTO converter(Footer footer) {
        return FooterDTO.builder()
                .id(footer.getId())
                .header(footer.getHeader())
                .description(footer.getDescription())
                .role(footer.getRole())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            List<Footer> footers = dao.getAll();
            List<FooterDTO> footerDTOS = new ArrayList<>();
            for (Footer f : footers) {
                FooterDTO footerDTO = converter(f);
                footerDTOS.add(footerDTO);
            }
            ctx.status(200).json(footerDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Footer footer = dao.getById(id);
            FooterDTO footerDTO = converter(footer);
            ctx.status(200).json(footerDTO);
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Footer footerCreated = ctx.bodyAsClass(Footer.class);
            if (footerCreated != null &&
                    footerCreated.getHeader() != null &&
                    footerCreated.getDescription() != null &&
                    footerCreated.getRole() != null &&
                    footerCreated.getRole().getName() != null) {
                Footer createdFooter = dao.create(footerCreated);
                FooterDTO footerDTO = converter(createdFooter);
                ctx.status(201).json(footerDTO);
            } else {
                throw new ApiException(400, "Header, description, and role are required.", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.getById(id);
            Footer footerToUpdate = ctx.bodyAsClass(Footer.class);
            footerToUpdate.setId(id);
            Footer footerUpdated = dao.update(footerToUpdate);
            FooterDTO footerDTO = converter(footerUpdated);
            ctx.status(200).json(footerDTO);
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Footer footerDeleted = dao.delete(id);
            if (footerDeleted != null) {
                FooterDTO footerDTO = converter(footerDeleted);
                ctx.status(200).json(footerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
