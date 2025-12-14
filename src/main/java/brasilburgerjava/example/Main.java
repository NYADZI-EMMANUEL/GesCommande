package brasilburgerjava.example;


import brasilburgerjava.example.views.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        

        BurgerVue burgerVue = new BurgerVue(scanner);
       
        
        System.out.println("=========================================");
        System.out.println("    BRASIL BURGER - GESTIONNAIRE");
        System.out.println("=========================================\n");
        
        boolean quitter = false;
        
        while (!quitter) {
            afficherMenuPrincipal();
            System.out.print("Votre choix: ");
            String choixStr = scanner.nextLine();
            
            try {
                int choix = Integer.parseInt(choixStr);
                
                switch (choix) {
                    case 1: 
                        gererBurgers(scanner, burgerVue);
                        break;
                        
                    case 2: 
                        //gererComplements(scanner, complementVue);
                        break;
                        
                    case 3: 
                        //gererMenus(scanner, menuVue);
                        break;
                        
                    case 4: 
                        //gererZones(scanner, zoneVue);
                        break;
                        
                    case 5: 
                        //gererLivreurs(scanner, livreurVue);
                        break;
                        
                    case 6: 
                        System.out.println("\nAu revoir !");
                        quitter = true;
                        break;
                        
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Veuillez entrer un nombre valide !");
            }
        }
        
        scanner.close();
    }
    
    private static void afficherMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1.Gestion des Burgers");
        System.out.println("2.Gestion des Compléments");
        System.out.println("3.Gestion des Menus");
        System.out.println("4.Gestion des Zones");
        System.out.println("5.Gestion des Livreurs");
        System.out.println("6.Quitter");
    }
    
    private static void gererBurgers(Scanner scanner, BurgerVue burgerVue) {
        boolean retour = false;
        
        while (!retour) {
            System.out.println("\n=== GESTION DES BURGERS ===");
            System.out.println("1.Créer un burger");
            System.out.println("2.Lister tous les burgers");
            System.out.println("3.Voir un burger");
            System.out.println("4.Retour");
            
            System.out.print("Votre choix: ");
            String choixStr = scanner.nextLine();
            
            try {
                int choix = Integer.parseInt(choixStr);
                
                switch (choix) {
                    case 1:
                        burgerVue.creerBurger();
                        break;
                    case 2:
                        burgerVue.listerBurgers();
                        break;
                    case 3:
                        burgerVue.voirBurger();
                        break;
                    case 4:
                        retour = true;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide !");
            }
        }
    }
   
}