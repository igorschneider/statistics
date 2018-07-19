package com.ischneider.stats.controller;

import com.ischneider.stats.model.Transaction;
import com.ischneider.stats.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public ResponseEntity create(@RequestBody Transaction transaction) {
        return ResponseEntity
                .status( service.create( transaction ) )
                .build();
    }

}
