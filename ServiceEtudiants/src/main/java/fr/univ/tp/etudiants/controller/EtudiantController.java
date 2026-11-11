package fr.univ.tp.etudiants.controller;

import fr.univ.tp.etudiants.model.Etudiant;
import fr.univ.tp.etudiants.repository.EtudiantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {
    private final EtudiantRepository repo;

    public EtudiantController(EtudiantRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Etudiant> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Etudiant get(@PathVariable int id) {
        return repo.findById(id).orElseThrow(() -> notFound(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Etudiant create(@RequestBody Etudiant etudiant) {
        return repo.create(etudiant);
    }

    @PutMapping("/{id}")
    public Etudiant update(@PathVariable int id, @RequestBody Etudiant etudiant) {
        return repo.update(id, etudiant).orElseThrow(() -> notFound(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        if (!repo.delete(id)) {
            throw notFound(id);
        }
    }

    private ResponseStatusException notFound(int id) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Étudiant " + id + " introuvable");
    }
}