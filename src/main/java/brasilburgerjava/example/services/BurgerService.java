package brasilburgerjava.example.services;

import brasilburgerjava.example.entity.Burger;
import java.util.List;

public interface BurgerService {
    Burger createBurger(Burger burger);
    Burger getBurgerById(Integer id);
    List<Burger> getAllBurgers();
    List<Burger> getActiveBurgers();
    Burger updateBurger(Burger burger);
    void archiveBurger(Integer id);
    boolean burgerExistsByName(String nom);
}