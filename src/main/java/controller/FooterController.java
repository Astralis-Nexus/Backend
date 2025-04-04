package controller;

import dao.FooterDAO;
import dto.FooterDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Footer;
import utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FooterController implements IController {
    private static String timestamp = DateUtil.getTimestamp();
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
            if (!dao.getAll().isEmpty()) {
                List<Footer> footers = dao.getAll();
                List<FooterDTO> footerDTOS = new ArrayList<>();
                for (Footer f : footers) {
                    FooterDTO footerDTO = converter(f);
                    footerDTOS.add(footerDTO);
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
            Footer footer = dao.getById(id);
            if (footer != null) {
                FooterDTO footerDTO = converter(footer);
                ctx.json(footerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Footer footerCreated = ctx.bodyAsClass(Footer.class);
            if (footerCreated != null) {
                Footer createdFooter = dao.create(footerCreated);
                FooterDTO footerDTO = converter(createdFooter);
                ctx.json(footerDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Footer footerToUpdate = ctx.bodyAsClass(Footer.class);
            footerToUpdate.setId(id);
            Footer footerUpdated = dao.update(footerToUpdate);
            FooterDTO footerDTO = converter(footerUpdated);
            if (footerDTO != null) {
                ctx.json(footerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Footer footerDeleted = dao.delete(id);
            FooterDTO footerDTO = converter(footerDeleted);
            if (footerDTO != null) {
                ctx.json(footerDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
