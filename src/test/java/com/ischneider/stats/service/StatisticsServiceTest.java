package com.ischneider.stats.service;

import com.ischneider.stats.model.Statistics;
import com.ischneider.stats.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService service;
    @Mock
    private TransactionRepository repository;

    @Test
    public void get() {
        Statistics expected = new Statistics( 0D, 0D, 0D, 0D, 0L );
        Mockito.when( repository.get() ).thenReturn( expected );

        Statistics response = service.get();

        assertEquals(expected, response);
    }
}