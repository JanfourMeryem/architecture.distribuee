package fr.univ.tp.cours.service;

import fr.univ.tp.cours.model.EtudiantDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class EtudiantClient {
    private final RestTemplate rest;
    private final String baseUrl;

    public EtudiantClient(RestTemplateBuilder builder, @Value("${service.etudiants.url}") String baseUrl) {
        this.rest = builder
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(2))
                .build();
        this.baseUrl = baseUrl;
    }

    public EtudiantDto getEtudiantById(int id) {
        return rest.getForObject(baseUrl + "/{id}", EtudiantDto.class, id);
    }
}
