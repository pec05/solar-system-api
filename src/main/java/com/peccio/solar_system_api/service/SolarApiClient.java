package com.peccio.solar_system_api.service;

import com.peccio.solar_system_api.model.MassVolume;
import com.peccio.solar_system_api.model.Planet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SolarApiClient {

    private final RestTemplate restTemplate;
    private String baseUrl;

    //Distance moyenne en UA (Unité astronomique)
    private static final Map<String, Double> DISTANCES_UA = Map.of(
            "mercure", 0.387,
            "venus",   0.723,
            "terre",   1.0,
            "mars",    1.524,
            "jupiter", 5.203,
            "saturne", 9.537,
            "uranus",  19.19,
            "neptune", 30.07
    );

    // Textures NASA (domaine public)
    private static final Map<String, String> TEXTURES = Map.of(
            "mercure", "assets/textures/mercury.jpg",
            "venus",   "assets/textures/venus.jpg",
            "terre",   "assets/textures/earth.jpg",
            "mars",    "assets/textures/mars.jpg",
            "jupiter", "assets/textures/jupiter.jpg",
            "saturne", "assets/textures/saturn.jpg",
            "uranus",  "assets/textures/uranus.jpg",
            "neptune", "assets/textures/neptune.jpg"
    );

    public SolarApiClient(@Value("${solar.api.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Cacheable("planets")
    public List<Planet> getAllPlanets() {
        log.info("Fetching planets from external API...");

        String url = baseUrl + "/bodies?filter[]=isPlanet,eq,true&filter[]=bodyType,eq,Planet";
        ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

        if (response == null || response.getBodies() == null) {
            log.error("No data received from Solar System API");
            return List.of();
        }

        return Arrays.stream(response.getBodies())
                .map(this::mapToPlanet)
                .toList();
    }

    @Cacheable("planet")
    public Planet getPlanetById(String id) {
        log.info("Fetching planet: {}", id);
        String url = baseUrl + "/bodies/" + id;
        ApiBody body = restTemplate.getForObject(url, ApiBody.class);
        return body != null ? mapToPlanet(body) : null;
    }

    private Planet mapToPlanet(ApiBody body) {
        String nameKey = body.getName() != null
                ? body.getName().toLowerCase()
                : "";

        MassVolume mass = null;
        MassVolume vol = null;

        if (body.getMass() != null) {
            mass = new MassVolume(
                    body.getMass().getMassValue(),
                    body.getMass().getMassExponent()
            );
        }
        if (body.getVol() != null) {
            vol = new MassVolume(
                    body.getVol().getMassValue(),
                    body.getVol().getMassExponent()
            );
        }

        return Planet.builder()
                .id(body.getId())
                .name(body.getName())
                .englishName(body.getEnglishName())
                .mass(mass)
                .vol(vol)
                .meanRadius(body.getMeanRadius())
                .sideralOrbit(body.getSideralOrbit())
                .sideralRotation(body.getSideralRotation())
                .distanceFromSun(DISTANCES_UA.getOrDefault(nameKey, 0.0))
                .isPlanet(body.isPlanet())
                .gravity(body.getGravity())
                .avgTemp(body.getAvgTemp())
                .axialTilt(body.getAxialTilt())
                .moonCount(body.getMoons() != null ? body.getMoons().length : 0)
                .textureUrl(TEXTURES.getOrDefault(nameKey, "assets/textures/default.jpg"))
                .build();
    }

    // Classes internes pour désérialiser la réponse de l'API
    @lombok.Data
    static class ApiResponse {
        private ApiBody[] bodies;
    }

    @lombok.Data
    static class ApiBody {
        private String id;
        private String name;
        private String englishName;
        private MassVol mass;
        private MassVol vol;
        private double meanRadius;
        private double sideralOrbit;
        private double sideralRotation;
        private boolean isPlanet;
        private double gravity;
        private double avgTemp;
        private double axialTilt;
        private Object[] moons;

        @lombok.Data
        static class MassVol {
            private double massValue;
            private int massExponent;
        }
    }
}