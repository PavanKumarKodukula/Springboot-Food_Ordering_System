package com.food.ordering.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {

    private String type = "Point";
    private double[] coordinates;
}
