package com.transferapp.route;

import com.google.gson.Gson;
import com.transferapp.dto.validation.AbstractDTOValidator;
import org.apache.commons.collections4.CollectionUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static spark.Spark.halt;

/**
 * Generic HTTP request handler that is supposed to process a POST request
 *
 * @param <T> type of the object provided in the request body
 */
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

        List<String> validationErrors = this.getDTOValidator().validate(dto);
        if (CollectionUtils.isNotEmpty(validationErrors)) {
            throw halt(400, String.join(", ", validationErrors));
        }

        processBody(dto);
        response.type("application/json");

        return "success";
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
