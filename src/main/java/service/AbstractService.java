package service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

public abstract class AbstractService {
    private URI uri;
    private final ObjectMapper objectMapper = new ObjectMapper();
    abstract Object getClassFromJson(String json);
    abstract String getJsonAsString();
}
