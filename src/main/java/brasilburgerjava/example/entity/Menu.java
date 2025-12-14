package brasilburgerjava.example.entity;

import java.time.LocalDateTime;

public class Menu {
    private Integer id;
    private String nom;
    private String image;
    private Double prix;
    private Integer burgerId;
    private Integer boissonId;
    private Integer friteId;
    private LocalDateTime createdAt;
    private Integer gestionnaireId;

    public Menu() {}

    public Menu(String nom, String image, Integer burgerId, Integer boissonId, 
                Integer friteId, Integer gestionnaireId) {
        this.nom = nom;
        this.image = image;
        this.burgerId = burgerId;
        this.boissonId = boissonId;
        this.friteId = friteId;
        this.gestionnaireId = gestionnaireId;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    
    public Integer getBurgerId() { return burgerId; }
    public void setBurgerId(Integer burgerId) { this.burgerId = burgerId; }
    
    public Integer getBoissonId() { return boissonId; }
    public void setBoissonId(Integer boissonId) { this.boissonId = boissonId; }
    
    public Integer getFriteId() { return friteId; }
    public void setFriteId(Integer friteId) { this.friteId = friteId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getGestionnaireId() { return gestionnaireId; }
    public void setGestionnaireId(Integer gestionnaireId) { this.gestionnaireId = gestionnaireId; }

    @Override
    public String toString() {
        return String.format("MENU - ID: %d | Nom: %s | Prix: %.2f FCFA", id, nom, prix);
    }
}