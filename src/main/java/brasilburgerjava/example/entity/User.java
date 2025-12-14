package brasilburgerjava.example.entity;

import java.time.LocalDateTime;

public class User {
    protected Integer id;
    protected String nom;
    protected String prenom;
    protected String telephone;
    protected String password;
    protected LocalDateTime createdAt;

    public User() {}

    public User(String nom, String prenom, String telephone, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.password = password;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("ID: %d | Nom: %s %s | Téléphone: %s", 
            id, prenom, nom, telephone);
    }
}