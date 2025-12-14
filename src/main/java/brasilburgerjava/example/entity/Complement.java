package brasilburgerjava.example.entity;

import java.time.LocalDateTime;

public class Complement {
    private Integer id;
    private String nom;
    private String image;
    private Boolean isArchived;
    private String description;
    private String quantite;
    private Double prix;
    private TypeComplement type;
    private LocalDateTime createdAt;
    private Integer gestionnaireId;

    public Complement() {}

    public Complement(String nom, String image, String description, String quantite, 
                     Double prix, TypeComplement type, Integer gestionnaireId) {
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.quantite = quantite;
        this.prix = prix;
        this.type = type;
        this.gestionnaireId = gestionnaireId;
        this.isArchived = false;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getQuantite() { return quantite; }
    public void setQuantite(String quantite) { this.quantite = quantite; }
    
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    
    public TypeComplement getType() { return type; }
    public void setType(TypeComplement type) { this.type = type; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getGestionnaireId() { return gestionnaireId; }
    public void setGestionnaireId(Integer gestionnaireId) { this.gestionnaireId = gestionnaireId; }

    @Override
    public String toString() {
        return String.format("COMPLÉMENT - ID: %d | Nom: %s | Type: %s | Prix: %.2f FCFA", 
            id, nom, type, prix);
    }
}