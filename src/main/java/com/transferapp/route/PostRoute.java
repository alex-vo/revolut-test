package com.transferapp.route;

import com.google.gson.Gson;
import com.transferapp.dto.ResponseMessageDTO;
import com.transferapp.dto.validation.AbstractDTOValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import spark.HaltException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

/**
 * Generic HTTP request handler that is supposed to process a POST request
 *
 * @param <T> type of the object provided in the request body
 */
@Slf4j
public abstract class PostRoute<T> implements Route {

    private Class<T> clazz;
    private Gson gson = new Gson();

    public PostRoute(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * @see Route#handle(Request, Response)
     */
    @Override
    public Object handle(Request request, Response response) {
        T dto = gson.fromJson(request.body(), clazz);

        response.type("application/json");

        List<String> validationErrors = this.getDTOValidator().validate(dto);
        if (CollectionUtils.isNotEmpty(validationErrors)) {
            response.status(400);
            return new ResponseMessageDTO(String.join(", ", validationErrors));
        }


        try {
            processBody(dto);
        } catch (HaltException he) {
            response.status(he.statusCode());
            return new ResponseMessageDTO(he.body());
        } catch (Throwable e) {
            log.error(String.format("Unexpected error during processing POST request %s %s", request.url(), request.body()), e);
            return new ResponseMessageDTO("error occurred");
        }

        return new ResponseMessageDTO("success");
    }

    /**
     * Placeholder for dto validator
     *
     * @return validator for objects of the parametrised type
     */
    protected abstract AbstractDTOValidator<T> getDTOValidator();

    /**
     * Placeholder for object processing logic
     *
     * @param dto object to be processed
     */
    protected abstract void processBody(T dto);
}
