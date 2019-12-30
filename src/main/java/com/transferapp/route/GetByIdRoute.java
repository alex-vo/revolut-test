package com.transferapp.route;

import com.transferapp.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import spark.HaltException;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Generic HTTP request handler that is supposed to return an object by id
 *
 * @param <T> type of the object to be returned in the response body
 */
@Slf4j
public abstract class GetByIdRoute<T> implements Route {

    /**
     * @see Route#handle(Request, Response)
     */
    @Override
    public Object handle(Request request, Response response) {
        Long id = Long.valueOf(request.params(":id"));
        response.type("application/json");
        try {
            return this.processGetRequest(id);
        } catch (HaltException he) {
            return new ErrorResponseDTO(he.body());
        } catch (Throwable e) {
            log.error(String.format("Unexpected error during processing GET request %s", request.url()), e);
            return new ErrorResponseDTO("error occurred");
        }
    }

    /**
     * Placeholder for getting object by id
     *
     * @param id requested object identifier
     * @return dto representation of the requested object
     */
    protected abstract T processGetRequest(Long id);
}
