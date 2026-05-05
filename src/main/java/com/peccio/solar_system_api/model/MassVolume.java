package com.peccio.solar_system_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MassVolume {
    private double massValue;
    private int massExponent;
}
