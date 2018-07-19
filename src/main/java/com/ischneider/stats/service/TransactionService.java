package com.ischneider.stats.service;

import com.ischneider.stats.model.Transaction;
import com.ischneider.stats.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    public HttpStatus create(Transaction transaction) {
        if (transaction.getTimestamp().toInstant().plusSeconds( 60L ).isBefore( Instant.now() ))
            return HttpStatus.NO_CONTENT;
        repository.add( transaction );
        return HttpStatus.CREATED;
    }

}
