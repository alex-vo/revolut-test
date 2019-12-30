package com.abc.route;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class GetByIdRoute<T> implements Route {

    @Override
    public Object handle(Request request, Response response) {
        Long id = Long.valueOf(request.params(":id"));
        response.type("application/json");
        return this.processGetRequest(id);
    }

    protected abstract T processGetRequest(Long id);
}
