package brasilburgerjava.example.services;

import brasilburgerjava.example.entity.Livreur;
import java.util.List;

public interface LivreurService {
    Livreur createLivreur(Livreur livreur);
    Livreur getLivreurById(Integer id);
    List<Livreur> getAllLivreurs();
    Livreur updateLivreur(Livreur livreur);
    void deleteLivreur(Integer id);
    boolean livreurExistsByTelephone(String telephone);
}