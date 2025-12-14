package brasilburgerjava.example.views;


import brasilburgerjava.example.entity.Complement;
import brasilburgerjava.example.entity.TypeComplement;
import brasilburgerjava.example.services.ComplementService;
import brasilburgerjava.example.services.Impl.ComplementServiceImpl;
import java.util.List;
import java.util.Scanner;


public class ComplementVue {
    private final ComplementService complementService;
    private final Scanner scanner;
   
    public ComplementVue(Scanner scanner) {
        this.complementService = new ComplementServiceImpl();
        this.scanner = scanner;
    }
   
    public void creerComplement() {
        System.out.println("\n--- CRÉATION D'UN COMPLÉMENT ---");
       
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
       
        System.out.println("Type:");
        System.out.println("1. Boisson");
        System.out.println("2. Frites");
        System.out.print("Votre choix (1-2): ");
        int typeChoice = Integer.parseInt(scanner.nextLine());
       
        TypeComplement type;
        if (typeChoice == 1) {
            type = TypeComplement.Boisson;
        } else if (typeChoice == 2) {
            type = TypeComplement.Frites;
        } else {
            System.out.println("❌ Type invalide !");
            return;
        }
       
        System.out.print("Prix (FCFA): ");
        double prix = Double.parseDouble(scanner.nextLine());
       
        System.out.print("Quantité (ex: 500ml, 200g): ");
        String quantite = scanner.nextLine();
       
        System.out.print("Image (URL): ");
        String image = scanner.nextLine();
       
        System.out.print("Description: ");
        String description = scanner.nextLine();
       
        System.out.print("ID du gestionnaire: ");
        int gestionnaireId = Integer.parseInt(scanner.nextLine());
       
        try {
            Complement complement = new Complement(nom, image, description, quantite, prix, type, gestionnaireId);
            complementService.createComplement(complement);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
   
    public void listerComplements() {
        System.out.println("\n--- LISTE DES COMPLÉMENTS ---");
        List<Complement> complements = complementService.getAllComplements();
       
        if (complements.isEmpty()) {
            System.out.println("Aucun complément trouvé.");
        } else {
            complements.forEach(System.out::println);
        }
    }
   
    public void listerComplementsParType() {
        System.out.println("\nType à afficher:");
        System.out.println("1. Boissons");
        System.out.println("2. Frites");
        System.out.print("Votre choix (1-2): ");
        int typeChoice = Integer.parseInt(scanner.nextLine());
       
        TypeComplement type;
        if (typeChoice == 1) {
            type = TypeComplement.Boisson;
        } else if (typeChoice == 2) {
            type = TypeComplement.Frites;
        } else {
            System.out.println("Type invalide !");
            return;
        }
       
        System.out.println("\n--- " + type + " DISPONIBLES ---");
        List<Complement> complements = complementService.getActiveComplementsByType(type);
       
        if (complements.isEmpty()) {
            System.out.println("Aucun " + type.toString().toLowerCase() + " trouvé.");
        } else {
            complements.forEach(System.out::println);
        }
    }
}

