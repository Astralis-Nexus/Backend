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
            List<QA> qas = dao.getAll();
            List<QADTO> qaDTOS = new ArrayList<>();
            for (QA q : qas) {
                QADTO qadto = converter(q);
                qaDTOS.add(qadto);
            }
            ctx.status(200).json(qaDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            QA qa = dao.getById(id);
            if (qa != null) {
                QADTO qadto = converter(qa);
                ctx.status(200).json(qadto);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            QADTO incoming = ctx.bodyAsClass(QADTO.class);

            if (incoming == null ||
                    incoming.getQuestion() == null ||
                    incoming.getAnswer() == null ||
                    incoming.getAccountId() == null) {
                throw new ApiException(400, "Missing required fields", timestamp);
            }

            Account account = dao.getAccountById(incoming.getAccountId());
            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + incoming.getAccountId(), timestamp);
            }

            QA qa = new QA(incoming.getQuestion(), incoming.getAnswer(), account);
            QA created = dao.create(qa);

            QADTO dto = new QADTO(
                    created.getId(),
                    created.getQuestion(),
                    created.getAnswer(),
                    created.getAccount().getId());

            ctx.status(201).json(dto);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.getById(id);
            QADTO incoming = ctx.bodyAsClass(QADTO.class);

            if (incoming.getAccountId() == null) {
                throw new ApiException(400, "Missing account information", timestamp);
            }

            Account account = dao.getAccountById(incoming.getAccountId());
            if (account == null)
                throw new ApiException(404, "Account not found", timestamp);

            QA qaToUpdate = new QA(incoming.getQuestion(), incoming.getAnswer(), account);
            qaToUpdate.setId(id);
            qaToUpdate.setAccount(account);

            QA updated = dao.update(qaToUpdate);
            if (updated != null) {
                QADTO dto = converter(updated);
                ctx.status(200).json(dto);
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
                ctx.status(200).json(qadto);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
