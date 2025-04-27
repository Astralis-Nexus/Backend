package controller;

import dao.InformationDAO;
import dto.InformationDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Information;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class InformationController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final InformationDAO dao;

    public InformationController(EntityManagerFactory emf) {
        this.dao = InformationDAO.getInstance(emf);
    }

    public InformationDTO converter(Information information) {
        return InformationDTO.builder()
                .id(information.getId())
                .description(information.getDescription())
                .accountId(information.getAccount().getId())
                .importanceLevel(information.getImportanceLevel().name()) 
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
            Information incoming = ctx.bodyAsClass(Information.class);
    
            if (incoming == null ||
                incoming.getDescription() == null ||
                incoming.getImportanceLevel() == null ||
                incoming.getAccount() == null ||
                incoming.getAccount().getId() == null) {
                throw new ApiException(400, "Description, importanceLevel, and accountId are required.", timestamp);
            }
    
            int accountId = incoming.getAccount().getId();
    
            Account account = dao.getAccountById(accountId);
            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + accountId, timestamp);
            }
    
            Information info = new Information(
                incoming.getDescription(),
                account,
                incoming.getImportanceLevel() 
            );
    
            Information createdInfo = dao.create(info);
            InformationDTO dto = converter(createdInfo);
    
            ctx.json(dto);
        };
    }
    


         

    @Override
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Information incoming = ctx.bodyAsClass(Information.class);
    
            if (incoming.getDescription() == null || incoming.getImportanceLevel() == null || incoming.getAccount() == null || incoming.getAccount().getId() == null) {
                throw new ApiException(400, "Missing fields", timestamp);
            }
    
            int accountId = incoming.getAccount().getId();
            Account account = dao.getAccountById(accountId);
            if (account == null) {
                throw new ApiException(404, "Account not found", timestamp);
            }
    
            incoming.setId(id);
            incoming.setAccount(account);
    
            Information updated = dao.update(incoming);
    
            if (updated != null) {
                InformationDTO dto = converter(updated);
                ctx.json(dto);
            } else {
                throw new ApiException(404, "No data found", timestamp);
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
