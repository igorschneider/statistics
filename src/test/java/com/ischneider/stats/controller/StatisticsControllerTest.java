package com.ischneider.stats.controller;

import com.ischneider.stats.model.Statistics;
import com.ischneider.stats.service.StatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {

    @InjectMocks
    private StatisticsController controller;
    @Mock
    private StatisticsService service;

    @Test
    public void get() {
        Statistics statistics = new Statistics( 0D, 0D, 0D, 0D, 0L );
        ResponseEntity expected = ResponseEntity.ok( statistics );
        Mockito.when( service.get() ).thenReturn( statistics );

        ResponseEntity response = controller.get();

        assertEquals( expected, response );
    }
}