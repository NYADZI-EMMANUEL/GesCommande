package brasilburgerjava.example.repository.Impl;

import brasilburgerjava.example.config.database.Database;
import brasilburgerjava.example.config.database.DatabaseImpl;
import brasilburgerjava.example.entity.Livreur;
import brasilburgerjava.example.repository.LivreurRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivreurRepositoryImpl implements LivreurRepository {
    private final Database database;

    public LivreurRepositoryImpl() {
        this.database = new DatabaseImpl();
    }

    @Override
    public Livreur save(Livreur livreur) {
        String sql = "INSERT INTO users (nom, prenom, telephone, password, type_user) VALUES (?, ?, ?, ?, 'Livreur') RETURNING id, created_at";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, livreur.getNom());
            statement.setString(2, livreur.getPrenom());
            statement.setString(3, livreur.getTelephone());
            statement.setString(4, livreur.getPassword());
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                livreur.setId(resultSet.getInt("id"));
                livreur.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                
                // Insérer dans la table livreurs
                String sqlLivreur = "INSERT INTO livreurs (id) VALUES (?)";
                try (PreparedStatement stmtLivreur = connection.prepareStatement(sqlLivreur)) {
                    stmtLivreur.setInt(1, livreur.getId());
                    stmtLivreur.executeUpdate();
                }
            }
            return livreur;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du livreur: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Livreur> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ? AND type_user = 'Livreur'";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToLivreur(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du livreur: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Livreur> findAll() {
        List<Livreur> livreurs = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE type_user = 'Livreur' ORDER BY nom, prenom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                livreurs.add(mapResultSetToLivreur(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livreurs: " + e.getMessage());
        }
        return livreurs;
    }

    @Override
    public Livreur update(Livreur livreur) {
        String sql = "UPDATE users SET nom = ?, prenom = ?, telephone = ?, password = ? WHERE id = ? AND type_user = 'Livreur'";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, livreur.getNom());
            statement.setString(2, livreur.getPrenom());
            statement.setString(3, livreur.getTelephone());
            statement.setString(4, livreur.getPassword());
            statement.setInt(5, livreur.getId());
            
            statement.executeUpdate();
            return livreur;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livreur: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ? AND type_user = 'Livreur'";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livreur: " + e.getMessage());
        }
    }

    @Override
    public Optional<Livreur> findByTelephone(String telephone) {
        String sql = "SELECT * FROM users WHERE telephone = ? AND type_user = 'Livreur'";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, telephone);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToLivreur(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par téléphone: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Livreur mapResultSetToLivreur(ResultSet resultSet) throws SQLException {
        Livreur livreur = new Livreur();
        livreur.setId(resultSet.getInt("id"));
        livreur.setNom(resultSet.getString("nom"));
        livreur.setPrenom(resultSet.getString("prenom"));
        livreur.setTelephone(resultSet.getString("telephone"));
        livreur.setPassword(resultSet.getString("password"));
        livreur.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return livreur;
    }
}