package com.transferapp.route;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Generic HTTP request handler that is supposed to return an object by id
 *
 * @param <T> type of the object to be returned in the response body
 */
public abstract class GetByIdRoute<T> implements Route {

    /**
     * @see Route#handle(Request, Response)
     */
    @Override
    public Object handle(Request request, Response response) {
        Long id = Long.valueOf(request.params(":id"));
        response.type("application/json");
        return this.processGetRequest(id);
    }

    /**
     * Placeholder for getting object by id
     *
     * @param id requested object identifier
     * @return dto representation of the requested object
     */
    protected abstract T processGetRequest(Long id);
}
