package brasilburgerjava.example.repository;

import brasilburgerjava.example.entity.Zone;
import java.util.List;
import java.util.Optional;

public interface ZoneRepository {
    Zone save(Zone zone);
    Optional<Zone> findById(Integer id);
    List<Zone> findAll();
    Zone update(Zone zone);
    void delete(Integer id);
    Optional<Zone> findByNom(String nom);
}