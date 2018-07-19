package com.ischneider.stats.service;

import com.ischneider.stats.model.Statistics;
import com.ischneider.stats.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private TransactionRepository repository;

    public Statistics get() {
        return repository.get();
    }

}
