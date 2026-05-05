package com.peccio.solar_system_api.controller;

import com.peccio.solar_system_api.model.Planet;
import com.peccio.solar_system_api.service.SolarApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/planets")
@RequiredArgsConstructor
public class PlanetController {

    private final SolarApiClient solarApiClient;

    @GetMapping
    public ResponseEntity<List<Planet>> getAllPlanets() {
        log.info("GET /api/planets");
        List<Planet> planets = solarApiClient.getAllPlanets();
        return ResponseEntity.ok(planets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getPlanetById(@PathVariable String id) {
        log.info("GET /api/planets/{}", id);
        Planet planet = solarApiClient.getPlanetById(id);
        if (planet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(planet);
    }

}
