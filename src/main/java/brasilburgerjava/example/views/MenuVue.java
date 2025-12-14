package brasilburgerjava.example.views;


import brasilburgerjava.example.entity.*;
import brasilburgerjava.example.services.*;
import brasilburgerjava.example.services.Impl.*;
import java.util.List;
import java.util.Scanner;


public class MenuVue {
    private final MenuService menuService;
    private final BurgerService burgerService;
    private final ComplementService complementService;
    private final Scanner scanner;
   
    public MenuVue(Scanner scanner) {
        this.menuService = new MenuServiceImpl();
        this.burgerService = new BurgerServiceImpl();
        this.complementService = new ComplementServiceImpl();
        this.scanner = scanner;
    }
   
    public void creerMenu() {
        System.out.println("\n--- CRÉATION D'UN MENU ---");
       
       
        System.out.println("\nBURGERS DISPONIBLES:");
        List<Burger> burgers = burgerService.getActiveBurgers();
        if (burgers.isEmpty()) {
            System.out.println("Aucun burger disponible ! Créez d'abord un burger.");
            return;
        }
        burgers.forEach(b -> System.out.println("  ID: " + b.getId() + " | " + b.getNom() + " | " + b.getPrix() + " FCFA"));
       
        System.out.print("\nID du burger: ");
        int burgerId = Integer.parseInt(scanner.nextLine());
       
       
        System.out.println("\nBOISSONS DISPONIBLES:");
        List<Complement> boissons = complementService.getActiveComplementsByType(TypeComplement.Boisson);
        if (boissons.isEmpty()) {
            System.out.println("Aucune boisson disponible ! Créez d'abord une boisson.");
            return;
        }
        boissons.forEach(b -> System.out.println("  ID: " + b.getId() + " | " + b.getNom() + " | " + b.getPrix() + " FCFA | " + b.getQuantite()));
       
        System.out.print("\nID de la boisson: ");
        int boissonId = Integer.parseInt(scanner.nextLine());
       
       
        System.out.println("\nFRITES DISPONIBLES:");
        List<Complement> frites = complementService.getActiveComplementsByType(TypeComplement.Frites);
        if (frites.isEmpty()) {
            System.out.println("Aucune frite disponible ! Créez d'abord des frites.");
            return;
        }
        frites.forEach(f -> System.out.println("  ID: " + f.getId() + " | " + f.getNom() + " | " + f.getPrix() + " FCFA | " + f.getQuantite()));
       
        System.out.print("\nID des frites: ");
        int friteId = Integer.parseInt(scanner.nextLine());
       
        System.out.print("\nNom du menu: ");
        String nom = scanner.nextLine();
       
        System.out.print("Image (URL): ");
        String image = scanner.nextLine();
       
        System.out.print("ID du gestionnaire: ");
        int gestionnaireId = Integer.parseInt(scanner.nextLine());
       
        try {
            Double prix = menuService.calculateMenuPrice(burgerId, boissonId, friteId);
            System.out.println("Prix calculé automatiquement: " + prix + " FCFA");
           
            Menu menu = new Menu(nom, image, burgerId, boissonId, friteId, gestionnaireId);
            menu.setPrix(prix);
            menuService.createMenu(menu);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
   
    public void listerMenus() {
        System.out.println("\n--- LISTE DES MENUS ---");
        List<Menu> menus = menuService.getAllMenus();
       
        if (menus.isEmpty()) {
            System.out.println("📭 Aucun menu trouvé.");
        } else {
            menus.forEach(System.out::println);
        }
    }
}

