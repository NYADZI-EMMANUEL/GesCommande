package brasilburgerjava.example.views;

import brasilburgerjava.example.entity.Burger;
import brasilburgerjava.example.services.BurgerService;
import brasilburgerjava.example.services.Impl.BurgerServiceImpl;
import java.util.List;
import java.util.Scanner;

public class BurgerVue {
    private final BurgerService burgerService;
    private final Scanner scanner;
    
    public BurgerVue(Scanner scanner) {
        this.burgerService = new BurgerServiceImpl();
        this.scanner = scanner;
    }
    
    public void creerBurger() {
        System.out.println("\n--- CRÉATION D'UN BURGER ---");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prix (FCFA): ");
        double prix = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Image (URL): ");
        String image = scanner.nextLine();
        
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        System.out.print("ID du gestionnaire: ");
        int gestionnaireId = Integer.parseInt(scanner.nextLine());
        
        try {
            Burger burger = new Burger(nom, prix, image, description, gestionnaireId);
            burgerService.createBurger(burger);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void listerBurgers() {
        System.out.println("\n--- LISTE DES BURGERS ---");
        List<Burger> burgers = burgerService.getAllBurgers();
        
        if (burgers.isEmpty()) {
            System.out.println("Aucun burger trouvé.");
        } else {
            burgers.forEach(System.out::println);
        }
    }
    
    public void voirBurger() {
        System.out.print("\nID du burger à afficher: ");
        int id = Integer.parseInt(scanner.nextLine());
        Burger burger = burgerService.getBurgerById(id);
        
        if (burger != null) {
            System.out.println("\n--- DÉTAILS DU BURGER ---");
            System.out.println("ID: " + burger.getId());
            System.out.println("Nom: " + burger.getNom());
            System.out.println("Prix: " + burger.getPrix() + " FCFA");
            System.out.println("Image: " + burger.getImage());
            System.out.println("Description: " + burger.getDescription());
            System.out.println("Archivé: " + (burger.getIsArchived() ? "Oui" : "Non"));
            System.out.println("Créé le: " + burger.getCreatedAt());
        } else {
            System.out.println("Burger non trouvé !");
        }
    }
}