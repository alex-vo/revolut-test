package com.abc.config;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JSONResponseTransformer implements ResponseTransformer {
    @Override
    public String render(Object model) {
        return new Gson().toJson(model);
    }
}
