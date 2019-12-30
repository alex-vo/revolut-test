package com.transferapp.dto.transformer;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.ResponseTransformer;

@Singleton
public class JSONResponseTransformer implements ResponseTransformer {

    private final Gson gson;

    @Inject
    public JSONResponseTransformer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
