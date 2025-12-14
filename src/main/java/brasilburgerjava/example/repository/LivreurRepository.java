package brasilburgerjava.example.repository;

import brasilburgerjava.example.entity.Livreur;
import java.util.List;
import java.util.Optional;

public interface LivreurRepository {
    Livreur save(Livreur livreur);
    Optional<Livreur> findById(Integer id);
    List<Livreur> findAll();
    Livreur update(Livreur livreur);
    void delete(Integer id);
    Optional<Livreur> findByTelephone(String telephone);
}