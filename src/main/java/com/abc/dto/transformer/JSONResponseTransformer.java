package com.abc.dto.transformer;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import spark.ResponseTransformer;

@Singleton
public class JSONResponseTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        return new Gson().toJson(model);
    }
}
