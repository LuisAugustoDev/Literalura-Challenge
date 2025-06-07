package com.augusto.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ConverterDados implements IConverteDados {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        if (json == null || json.trim().isEmpty()) {
            throw new RuntimeException("Resposta da API está vazia ou nula.");
        }

        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar o JSON: " + e.getMessage(), e);
        }
    }

    public <T> T converterResultadoBusca(String json, Class<T> classe){

        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode resultsNode = rootNode.get("results");
            System.out.println(resultsNode.toPrettyString());
            return mapper.readValue(json, classe);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
