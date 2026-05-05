package com.peccio.solar_system_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planet {
    private String id;
    private String name;
    private String englishName;
    private MassVolume mass;
    private MassVolume vol;
    private double meanRadius;
    private double sideralOrbit;
    private double sideralRotation;
    private double distanceFromSun;
    private boolean isPlanet;
    private double gravity;
    private double avgTemp;
    private double axialTilt;
    private int moonCount;
    private String textureUrl;
}
