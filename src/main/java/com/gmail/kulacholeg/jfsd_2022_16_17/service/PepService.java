package com.gmail.kulacholeg.jfsd_2022_16_17.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class PepService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PepService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public String savePep(MultipartFile file) throws IOException {
        try (ZipInputStream zipStream = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry = zipStream.getNextEntry();
            while (entry != null) {
                if (entry.getName().endsWith(".json")) {
                    String json = new String(zipStream.readAllBytes(), StandardCharsets.UTF_8);
                    saveJsonToMongo(json);
                    break;
                }
                entry = zipStream.getNextEntry();
            }
        }
        return "File processed successfully.";

    }

    private void saveJsonToMongo(String json) throws JsonProcessingException {
        mongoTemplate.dropCollection("pep");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Document> documents = objectMapper.readValue(json, new TypeReference<List<Document>>(){});
        mongoTemplate.insert(documents, "pep");
    }
}
