package brasilburgerjava.example.services.Impl;


import brasilburgerjava.example.entity.Burger;
import brasilburgerjava.example.repository.BurgerRepository;
import brasilburgerjava.example.repository.Impl.BurgerRepositoryImpl;
import brasilburgerjava.example.services.BurgerService;

import java.util.List;

public class BurgerServiceImpl implements BurgerService {
    private final BurgerRepository burgerRepository;

    public BurgerServiceImpl() {
        this.burgerRepository = new BurgerRepositoryImpl();
    }

    @Override
    public Burger createBurger(Burger burger) {
        if (burgerRepository.findByNom(burger.getNom()).isPresent()) {
            throw new IllegalArgumentException("Un burger avec le nom '" + burger.getNom() + "' existe déjà !");
        }
        
        Burger savedBurger = burgerRepository.save(burger);
        if (savedBurger != null) {
            System.out.println("Burger créé avec succès !");
        }
        return savedBurger;
    }

    @Override
    public Burger getBurgerById(Integer id) {
        return burgerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Burger> getAllBurgers() {
        return burgerRepository.findAll();
    }

    @Override
    public List<Burger> getActiveBurgers() {
        return burgerRepository.findAllActive();
    }

    @Override
    public Burger updateBurger(Burger burger) {
        burgerRepository.findByNom(burger.getNom())
            .ifPresent(existingBurger -> {
                if (!existingBurger.getId().equals(burger.getId())) {
                    throw new IllegalArgumentException("Un autre burger avec le nom '" + burger.getNom() + "' existe déjà !");
                }
            });
        
        return burgerRepository.update(burger);
    }

    @Override
    public void archiveBurger(Integer id) {
        burgerRepository.archive(id);
        System.out.println("Burger archivé avec succès !");
    }

    @Override
    public boolean burgerExistsByName(String nom) {
        return burgerRepository.findByNom(nom).isPresent();
    }
}