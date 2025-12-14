package brasilburgerjava.example.repository.Impl;

import brasilburgerjava.example.config.database.Database;
import brasilburgerjava.example.config.database.DatabaseImpl;
import brasilburgerjava.example.entity.Burger;
import brasilburgerjava.example.repository.BurgerRepository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurgerRepositoryImpl implements BurgerRepository {
    private final Database database;

    public BurgerRepositoryImpl() {
        this.database = new DatabaseImpl();
    }

    @Override
    public Burger save(Burger burger) {
        String sql = "INSERT INTO burgers (nom, prix, image, description, gestionnaire_id) VALUES (?, ?, ?, ?, ?) RETURNING id, created_at";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, burger.getNom());
            statement.setDouble(2, burger.getPrix());
            statement.setString(3, burger.getImage());
            statement.setString(4, burger.getDescription());
            statement.setInt(5, burger.getGestionnaireId());
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                burger.setId(resultSet.getInt("id"));
                burger.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            }
            return burger;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du burger: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Burger> findById(Integer id) {
        String sql = "SELECT * FROM burgers WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBurger(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du burger: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Burger> findAll() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burgers ORDER BY nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                burgers.add(mapResultSetToBurger(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des burgers: " + e.getMessage());
        }
        return burgers;
    }

    @Override
    public List<Burger> findAllActive() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burgers WHERE is_archived = false ORDER BY nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                burgers.add(mapResultSetToBurger(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des burgers actifs: " + e.getMessage());
        }
        return burgers;
    }

    @Override
    public Burger update(Burger burger) {
        String sql = "UPDATE burgers SET nom = ?, prix = ?, image = ?, description = ?, is_archived = ? WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, burger.getNom());
            statement.setDouble(2, burger.getPrix());
            statement.setString(3, burger.getImage());
            statement.setString(4, burger.getDescription());
            statement.setBoolean(5, burger.getIsArchived());
            statement.setInt(6, burger.getId());
            
            statement.executeUpdate();
            return burger;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du burger: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void archive(Integer id) {
        String sql = "UPDATE burgers SET is_archived = true WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du burger: " + e.getMessage());
        }
    }

    @Override
    public Optional<Burger> findByNom(String nom) {
        String sql = "SELECT * FROM burgers WHERE LOWER(nom) = LOWER(?)";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBurger(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Burger mapResultSetToBurger(ResultSet resultSet) throws SQLException {
        Burger burger = new Burger();
        burger.setId(resultSet.getInt("id"));
        burger.setNom(resultSet.getString("nom"));
        burger.setPrix(resultSet.getDouble("prix"));
        burger.setImage(resultSet.getString("image"));
        burger.setIsArchived(resultSet.getBoolean("is_archived"));
        burger.setDescription(resultSet.getString("description"));
        burger.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        burger.setGestionnaireId(resultSet.getInt("gestionnaire_id"));
        return burger;
    }
}