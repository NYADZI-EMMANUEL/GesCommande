package brasilburgerjava.example.views;


import brasilburgerjava.example.entity.Livreur;
import brasilburgerjava.example.services.LivreurService;
import brasilburgerjava.example.services.Impl.LivreurServiceImpl;
import java.util.List;
import java.util.Scanner;


public class LivreurVue {
    private final LivreurService livreurService;
    private final Scanner scanner;
   
    public LivreurVue(Scanner scanner) {
        this.livreurService = new LivreurServiceImpl();
        this.scanner = scanner;
    }
   
    public void creerLivreur() {
        System.out.println("\n--- CRÉATION D'UN LIVREUR ---");
       
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
       
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
       
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
       
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();
       
        try {
            Livreur livreur = new Livreur(nom, prenom, telephone, password);
            livreurService.createLivreur(livreur);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
   
    public void listerLivreurs() {
        System.out.println("\n--- LISTE DES LIVREURS ---");
        List<Livreur> livreurs = livreurService.getAllLivreurs();
       
        if (livreurs.isEmpty()) {
            System.out.println("📭 Aucun livreur trouvé.");
        } else {
            livreurs.forEach(System.out::println);
        }
    }
   
    public void voirLivreur() {
        System.out.print("\nID du livreur à afficher: ");
        int id = Integer.parseInt(scanner.nextLine());
        Livreur livreur = livreurService.getLivreurById(id);
       
        if (livreur != null) {
            System.out.println("\n--- DÉTAILS DU LIVREUR ---");
            System.out.println("ID: " + livreur.getId());
            System.out.println("Nom: " + livreur.getNom());
            System.out.println("Prénom: " + livreur.getPrenom());
            System.out.println("Téléphone: " + livreur.getTelephone());
            System.out.println("Créé le: " + livreur.getCreatedAt());
        } else {
            System.out.println("Livreur non trouvé !");
        }
    }
}



