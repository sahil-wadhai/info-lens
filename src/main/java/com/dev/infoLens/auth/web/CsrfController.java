package com.dev.infoLens.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/csrf-token")
public class CsrfController {
    @GetMapping
    public ResponseEntity<String> generateCsrf(){
        return new ResponseEntity<>("someToken", HttpStatus.OK);
    }
}
