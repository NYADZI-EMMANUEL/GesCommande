package brasilburgerjava.example.views;


import brasilburgerjava.example.entity.Zone;
import brasilburgerjava.example.services.ZoneService;
import brasilburgerjava.example.services.Impl.ZoneServiceImpl;
import java.util.List;
import java.util.Scanner;


public class ZoneVue {
    private final ZoneService zoneService;
    private final Scanner scanner;
   
    public ZoneVue(Scanner scanner) {
        this.zoneService = new ZoneServiceImpl();
        this.scanner = scanner;
    }
   
    public void creerZone() {
        System.out.println("\n--- CRÉATION D'UNE ZONE ---");
       
        System.out.print("Nom de la zone: ");
        String nom = scanner.nextLine();
       
        System.out.print("Quartiers (séparés par des virgules): ");
        String quartiers = scanner.nextLine();
       
        System.out.print("Prix de livraison (FCFA): ");
        double prix = Double.parseDouble(scanner.nextLine());
       
        try {
            Zone zone = new Zone(nom, quartiers, prix);
            zoneService.createZone(zone);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
   
    public void listerZones() {
        System.out.println("\n--- LISTE DES ZONES ---");
        List<Zone> zones = zoneService.getAllZones();
       
        if (zones.isEmpty()) {
            System.out.println("Aucune zone trouvée.");
        } else {
            zones.forEach(System.out::println);
        }
    }
   
    public void voirZone() {
        System.out.print("\nID de la zone à afficher: ");
        int id = Integer.parseInt(scanner.nextLine());
        Zone zone = zoneService.getZoneById(id);
       
        if (zone != null) {
            System.out.println("\n--- DÉTAILS DE LA ZONE ---");
            System.out.println("ID: " + zone.getId());
            System.out.println("Nom: " + zone.getNom());
            System.out.println("Quartiers: " + zone.getQuartiers());
            System.out.println("Prix de livraison: " + zone.getPrix() + " FCFA");
            System.out.println("Créée le: " + zone.getCreatedAt());
        } else {
            System.out.println(" Zone non trouvée !");
        }
    }
}

