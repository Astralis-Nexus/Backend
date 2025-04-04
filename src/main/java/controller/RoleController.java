package controller;

import dao.RoleDAO;
import dto.RoleDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Role;
import utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoleController implements IController {
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = DateUtil.getTimestamp();
    private final RoleDAO dao;

    public RoleController(EntityManagerFactory emf) {
        this.dao = RoleDAO.getInstance(emf);
    }

    public RoleDTO converter(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName().name())
                .headers(role.getHeaders())
                .footers(role.getFooters())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<Role> roles = dao.getAll();
                List<RoleDTO> roleDTOS = new ArrayList<>();
                for (Role r : roles) {
                    RoleDTO roleDTO = converter(r);
                    roleDTOS.add(roleDTO);
                }
                ctx.json(roleDTOS);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Role role = dao.getById(id);
            if (role != null) {
                RoleDTO roleDTO = converter(role);
                ctx.json(roleDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Role roleCreated = ctx.bodyAsClass(Role.class);
            if (roleCreated != null) {
                Role role = dao.create(roleCreated);
                RoleDTO roleDTO = converter(role);
                ctx.json(roleDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            Role roleToUpdate = ctx.bodyAsClass(Role.class);
            roleToUpdate.setId(id);

            Role roleUpdated = dao.update(roleToUpdate);
            if (roleUpdated != null) {
                RoleDTO roleDTO = converter(roleUpdated);
                ctx.json(roleDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Role roleDeleted = dao.delete(id);
            if (roleDeleted != null) {
                RoleDTO roleDTO = converter(roleDeleted);
                ctx.json(roleDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
