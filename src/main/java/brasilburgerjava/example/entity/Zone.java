package brasilburgerjava.example.entity;

import java.time.LocalDateTime;

public class Zone {
    private Integer id;
    private String nom;
    private String quartiers;
    private Double prix;
    private LocalDateTime createdAt;

    public Zone() {}

    public Zone(String nom, String quartiers, Double prix) {
        this.nom = nom;
        this.quartiers = quartiers;
        this.prix = prix;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getQuartiers() { return quartiers; }
    public void setQuartiers(String quartiers) { this.quartiers = quartiers; }
    
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("ZONE - ID: %d | Nom: %s | Prix livraison: %.2f FCFA", 
            id, nom, prix);
    }
}