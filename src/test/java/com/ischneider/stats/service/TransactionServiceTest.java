package com.ischneider.stats.service;

import com.ischneider.stats.model.Transaction;
import com.ischneider.stats.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService service;
    @Mock
    private TransactionRepository repository;

    @Test
    public void doNotCreateWhenIsOlderThan60Seconds() {
        Date timestamp = new Date( Instant.now().minusSeconds( 61L ).toEpochMilli() );
        Transaction transaction = new Transaction( 100D, timestamp );

        HttpStatus status = service.create( transaction );

        assertEquals( HttpStatus.NO_CONTENT, status );
        Mockito.verifyZeroInteractions( repository );
    }

    @Test
    public void createWhenIsNotOlderThan60Seconds() {
        Date timestamp = new Date( Instant.now().minusSeconds( 59L ).toEpochMilli() );
        Transaction transaction = new Transaction( 100D, timestamp );

        HttpStatus status = service.create( transaction );

        assertEquals( HttpStatus.CREATED, status );
        Mockito.verify( repository ).add( transaction );
    }
}