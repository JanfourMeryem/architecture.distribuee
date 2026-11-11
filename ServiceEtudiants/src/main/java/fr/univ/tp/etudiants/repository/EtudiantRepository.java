package fr.univ.tp.etudiants.repository;

import fr.univ.tp.etudiants.model.Etudiant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class EtudiantRepository {
    private final Map<Integer, Etudiant> store = new ConcurrentHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(1);

    public List<Etudiant> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Etudiant> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public Etudiant create(Etudiant etudiant) {
        etudiant.setId(seq.getAndIncrement());
        store.put(etudiant.getId(), etudiant);
        return etudiant;
    }

    public Optional<Etudiant> update(int id, Etudiant etudiant) {
        etudiant.setId(id);
        if (store.replace(id, etudiant) == null) {
            return Optional.empty();
        }
        return Optional.of(etudiant);
    }

    public boolean delete(int id) {
        return store.remove(id) != null;
    }
}