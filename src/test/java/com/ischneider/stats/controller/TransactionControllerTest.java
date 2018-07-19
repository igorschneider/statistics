package com.ischneider.stats.controller;

import com.ischneider.stats.model.Transaction;
import com.ischneider.stats.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController controller;
    @Mock
    private TransactionService service;

    @Test
    public void create() {
        Transaction transaction = new Transaction( 100D, new Date() );
        Mockito.when( service.create( transaction ) ).thenReturn( HttpStatus.CREATED );

        ResponseEntity response = controller.create( transaction );

        assertEquals( HttpStatus.CREATED, response.getStatusCode() );
    }
}