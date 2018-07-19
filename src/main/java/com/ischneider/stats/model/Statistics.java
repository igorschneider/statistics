package com.ischneider.stats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Statistics {

    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;

}
