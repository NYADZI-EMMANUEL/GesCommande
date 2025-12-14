package brasilburgerjava.example.services.Impl;


import brasilburgerjava.example.entity.Livreur;
import brasilburgerjava.example.repository.LivreurRepository;
import brasilburgerjava.example.repository.Impl.LivreurRepositoryImpl;
import brasilburgerjava.example.services.LivreurService;


import java.util.List;


public class LivreurServiceImpl implements LivreurService {
    private final LivreurRepository livreurRepository;


    public LivreurServiceImpl() {
        this.livreurRepository = new LivreurRepositoryImpl();
    }


    @Override
    public Livreur createLivreur(Livreur livreur) {
        if (livreurRepository.findByTelephone(livreur.getTelephone()).isPresent()) {
            throw new IllegalArgumentException("Un livreur avec le téléphone '" + livreur.getTelephone() + "' existe déjà !");
        }
       
        Livreur savedLivreur = livreurRepository.save(livreur);
        if (savedLivreur != null) {
            System.out.println("✅ Livreur créé avec succès !");
        }
        return savedLivreur;
    }


    @Override
    public Livreur getLivreurById(Integer id) {
        return livreurRepository.findById(id).orElse(null);
    }


    @Override
    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }


    @Override
    public Livreur updateLivreur(Livreur livreur) {
        livreurRepository.findByTelephone(livreur.getTelephone())
            .ifPresent(existingLivreur -> {
                if (!existingLivreur.getId().equals(livreur.getId())) {
                    throw new IllegalArgumentException("❌ Un autre livreur avec le téléphone '" + livreur.getTelephone() + "' existe déjà !");
                }
            });
       
        return livreurRepository.update(livreur);
    }


    @Override
    public void deleteLivreur(Integer id) {
        livreurRepository.delete(id);
        System.out.println("✅ Livreur supprimé avec succès !");
    }


    @Override
    public boolean livreurExistsByTelephone(String telephone) {
        return livreurRepository.findByTelephone(telephone).isPresent();
    }
}

