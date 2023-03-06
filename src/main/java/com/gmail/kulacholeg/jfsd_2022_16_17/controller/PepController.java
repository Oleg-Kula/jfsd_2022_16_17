package com.gmail.kulacholeg.jfsd_2022_16_17.controller;

import com.gmail.kulacholeg.jfsd_2022_16_17.service.PepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pep")
public class PepController {

    private final PepService service;

    @Autowired
    public PepController(PepService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addPepArchive(@RequestBody MultipartFile file){
        try{
            return ResponseEntity.ok(service.savePep(file));
        } catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file");
        }
    }
}
