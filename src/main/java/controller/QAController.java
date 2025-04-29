package controller;

import dao.QADAO;
import dto.QADTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.QA;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class QAController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final QADAO dao;

    public QAController(EntityManagerFactory emf) {
        this.dao = QADAO.getInstance(emf);
    }

    public QADTO converter(QA qa) {
        return QADTO.builder()
                .id(qa.getId())
                .question(qa.getQuestion())
                .answer(qa.getAnswer())
                .accountId(qa.getAccount().getId())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<QA> qas = dao.getAll();
                List<QADTO> qaDTOS = new ArrayList<>();
                for (QA q : qas) {
                    QADTO qadto = converter(q);
                    qaDTOS.add(qadto);
                }
                ctx.json(qaDTOS);
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
            QA incoming = ctx.bodyAsClass(QA.class);

            if (incoming == null ||
                    incoming.getQuestion() == null ||
                    incoming.getAnswer() == null ||
                    incoming.getAccount() == null ||
                    incoming.getAccount().getId() == null) {
                throw new ApiException(400, "Missing required fields", timestamp);
            }

            Account account = dao.getAccountById(incoming.getAccount().getId());
            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + incoming.getAccount().getId(), timestamp);
            }

            incoming.setAccount(account);

            QA created = dao.create(incoming);

            QADTO dto = new QADTO(
                    created.getId(),
                    created.getQuestion(),
                    created.getAnswer(),
                    created.getAccount().getId());

            ctx.json(dto);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            QA qaToUpdate = ctx.bodyAsClass(QA.class);
            qaToUpdate.setId(id);

            Account account = dao.getAccountById(qaToUpdate.getAccount().getId());
            if (account == null)
                throw new ApiException(404, "Account not found", timestamp);
            qaToUpdate.setAccount(account);

            QA updated = dao.update(qaToUpdate);
            if (updated != null) {
                QADTO dto = converter(updated);
                ctx.json(dto);
            } else {
                throw new ApiException(404, "No data found.", timestamp);
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
