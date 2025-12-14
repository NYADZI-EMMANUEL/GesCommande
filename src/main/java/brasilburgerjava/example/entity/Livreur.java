package brasilburgerjava.example.entity;

public class Livreur extends User {
    
    public Livreur() {
        super();
    }
    
    public Livreur(String nom, String prenom, String telephone, String password) {
        super(nom, prenom, telephone, password);
    }
    
    @Override
    public String toString() {
        return String.format("LIVREUR - ID: %d | Nom: %s %s | Téléphone: %s", 
            id, prenom, nom, telephone);
    }
}