package com.abc;

import com.abc.dto.TransferDTO;
import com.abc.entity.Account;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import spark.Spark;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static spark.Spark.halt;

public class MainClass {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        prepareData();

        Spark.post(
                "/transfer",
                (req, res) -> {
                    TransferDTO dto = gson.fromJson(req.body(), TransferDTO.class);
                    List<String> validationErrors = Pico.getTransferDTOValidator().validate(dto);
                    if (CollectionUtils.isNotEmpty(validationErrors)) {
                        throw halt(400, String.join(", ", validationErrors));
                    }

                    Pico.getAccountService().processTransfer(dto);

                    res.type("application/json");
                    return "transfer completed";
                },
                Pico.getJSONResponseTransformer()
        );

        Spark.get(
                "/account/:id",
                (req, res) -> {
                    Long id = Long.valueOf(req.params(":id"));
                    res.type("application/json");
                    return Pico.getAccountService().getAccountState(id);
                },
                Pico.getJSONResponseTransformer()
        );
    }

    private static void prepareData() {
        Pico.getTransactionService().executeInTransaction(() -> {
            EntityManager em = Pico.getEntityManager();
            Account a1 = new Account();
            a1.setBalance(BigDecimal.valueOf(4.5));
            em.persist(a1);

            Account a2 = new Account();
            a2.setBalance(BigDecimal.valueOf(3.5));
            em.persist(a2);
        });
    }
}
