package com.ischneider.stats.config;

import com.ischneider.stats.model.Transaction;
import com.ischneider.stats.repository.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public TransactionRepository createRepository() {
        return new TransactionRepository(
                new PriorityQueue<>( (t1, t2) -> t2.getAmount().compareTo( t1.getAmount() ) ),
                new PriorityQueue<>( Comparator.comparing( Transaction::getAmount ) ),
                0D,
                0D,
                0L,
                Executors.newSingleThreadScheduledExecutor()
        );
    }

}
