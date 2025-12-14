package brasilburgerjava.example.entity;

import java.time.LocalDateTime;

public class Burger {
    private Integer id;
    private String nom;
    private Double prix;
    private String image;
    private Boolean isArchived;
    private String description;
    private LocalDateTime createdAt;
    private Integer gestionnaireId;

    public Burger() {}

    public Burger(String nom, Double prix, String image, String description, Integer gestionnaireId) {
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.description = description;
        this.gestionnaireId = gestionnaireId;
        this.isArchived = false;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getGestionnaireId() { return gestionnaireId; }
    public void setGestionnaireId(Integer gestionnaireId) { this.gestionnaireId = gestionnaireId; }

    @Override
    public String toString() {
        return String.format("BURGER - ID: %d | Nom: %s | Prix: %.2f FCFA | Archivé: %s", 
            id, nom, prix, isArchived ? "Oui" : "Non");
    }
}