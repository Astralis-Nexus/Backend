package controller;

import dao.LicenseDAO;
import dto.LicenseDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.License;
import utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            License licenseCreated = ctx.bodyAsClass(License.class);
            if (licenseCreated != null) {
                License license = dao.create(licenseCreated);
                LicenseDTO licenseDTO = converter(license);
                ctx.json(licenseDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            License licenseToUpdate = ctx.bodyAsClass(License.class);
            licenseToUpdate.setId(id);
            License licenseUpdated = dao.update(licenseToUpdate);
            if (licenseUpdated != null) {
                LicenseDTO licenseDTO = converter(licenseUpdated);
                ctx.json(licenseDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
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
