package com.ischneider.stats.controller;

import com.ischneider.stats.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping
    public ResponseEntity get() {
        return ResponseEntity
                .ok( service.get() );
    }

}
