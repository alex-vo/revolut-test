package com.abc.route;

import com.abc.dto.validation.AbstractDTOValidator;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static spark.Spark.halt;

public abstract class PostRoute<T> implements Route {

    private Class<T> clazz;
    private Gson gson = new Gson();

    public PostRoute(Class<T> clazz) {
        this.clazz = clazz;
    }

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

    protected abstract AbstractDTOValidator<T> getDTOValidator();

    protected abstract void processBody(T dto);
}
