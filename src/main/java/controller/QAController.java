package controller;

import dao.QADAO;
import dto.QADTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.QA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QAController implements IController {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());
    private final QADAO dao;

    public QAController(EntityManagerFactory emf) {
        this.dao = QADAO.getInstance(emf);
    }

    public QADTO converter(QA qa) {
        return QADTO.builder()
                .id(qa.getId())
                .question(qa.getQuestion())
                .answer(qa.getAnswer())
                .account(qa.getAccount())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<QA> qas = dao.getAll();
                List<QADTO> qadtos = new ArrayList<>();
                for (QA q : qas) {
                    QADTO qadto = converter(q);
                    qadtos.add(qadto);
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
            QA qa = dao.getById(id);
            if (qa != null) {
                QADTO qadto = converter(qa);
                ctx.json(qadto);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            QA qaCreated = ctx.bodyAsClass(QA.class);
            if (qaCreated != null) {
                QADTO qadto = converter(qaCreated);
                ctx.json(qadto);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            QA qaToUpdate = ctx.bodyAsClass(QA.class);
            qaToUpdate.setId(id);
            QA qaUpdated = dao.update(qaToUpdate);
            if (qaUpdated != null) {
                QADTO qadto = converter(qaUpdated);
                ctx.json(qadto);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            QA qaDeleted = dao.delete(id);
            if (qaDeleted != null) {
                QADTO qadto = converter(qaDeleted);
                ctx.json(qadto);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
