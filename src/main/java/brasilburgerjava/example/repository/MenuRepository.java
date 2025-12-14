package brasilburgerjava.example.repository;

import brasilburgerjava.example.entity.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findById(Integer id);
    List<Menu> findAll();
    Menu update(Menu menu);
    void delete(Integer id);
    Optional<Menu> findByNom(String nom);
    Double calculatePrix(Integer burgerId, Integer boissonId, Integer friteId);
}