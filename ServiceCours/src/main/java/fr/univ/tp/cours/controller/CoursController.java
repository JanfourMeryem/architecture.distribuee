package fr.univ.tp.cours.controller;

import fr.univ.tp.cours.model.Cours;
import fr.univ.tp.cours.model.EtudiantDto;
import fr.univ.tp.cours.repository.CoursRepository;
import fr.univ.tp.cours.service.EtudiantClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cours")
public class CoursController {
    private final CoursRepository repo;

    private final EtudiantClient etudiantClient;

    public CoursController(CoursRepository repo, EtudiantClient etudiantClient) {
        this.repo = repo;
        this.etudiantClient = etudiantClient;
    }

    @GetMapping
    public List<Cours> all() {
        return repo.findAll();
    }

    @GetMapping("/enrichis")
    public List<Map<String, Object>> getCoursEnrichis() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Cours cours : repo.findAll()) {
            Map<String, Object> ligne = new LinkedHashMap<>();
            ligne.put("id", cours.getId());
            ligne.put("titre", cours.getTitre());
            ligne.put("etudiantNom", getEtudiantNom(cours.getEtudiantId()));
            result.add(ligne);
        }
        return result;
    }

    private String getEtudiantNom(Integer etudiantId) {
        if (etudiantId == null) {
            return "INCONNU";
        }
        try {
            EtudiantDto etudiant = etudiantClient.getEtudiantById(etudiantId);
            return etudiant == null || etudiant.nom() == null ? "INCONNU" : etudiant.nom();
        } catch (RestClientException ex) {
            return "INCONNU";
        }
    }

    @GetMapping("/{id}")
    public Cours get(@PathVariable int id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Cours non trouvé"));
    }

    @PostMapping
    public Cours create(@RequestBody Cours cours) {
        return repo.save(cours);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        repo.delete(id);
    }
}
