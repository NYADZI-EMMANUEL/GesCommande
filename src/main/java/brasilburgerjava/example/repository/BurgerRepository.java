package brasilburgerjava.example.repository;

import brasilburgerjava.example.entity.Burger;
import java.util.List;
import java.util.Optional;

public interface BurgerRepository {
    Burger save(Burger burger);
    Optional<Burger> findById(Integer id);
    List<Burger> findAll();
    List<Burger> findAllActive();
    Burger update(Burger burger);
    void archive(Integer id);
    Optional<Burger> findByNom(String nom);
}