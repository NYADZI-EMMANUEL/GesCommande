package brasilburgerjava.example.repository.Impl;

import brasilburgerjava.example.config.database.Database;
import brasilburgerjava.example.config.database.DatabaseImpl;
import brasilburgerjava.example.entity.Zone;
import brasilburgerjava.example.repository.ZoneRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZoneRepositoryImpl implements ZoneRepository {
    private final Database database;

    public ZoneRepositoryImpl() {
        this.database = new DatabaseImpl();
    }

    @Override
    public Zone save(Zone zone) {
        String sql = "INSERT INTO zones (nom, quartiers, prix) VALUES (?, ?, ?) RETURNING id, created_at";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, zone.getNom());
            statement.setString(2, zone.getQuartiers());
            statement.setDouble(3, zone.getPrix());
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                zone.setId(resultSet.getInt("id"));
                zone.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            }
            return zone;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde de la zone: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Zone> findById(Integer id) {
        String sql = "SELECT * FROM zones WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToZone(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la zone: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Zone> findAll() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM zones ORDER BY nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                zones.add(mapResultSetToZone(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des zones: " + e.getMessage());
        }
        return zones;
    }

    @Override
    public Zone update(Zone zone) {
        String sql = "UPDATE zones SET nom = ?, quartiers = ?, prix = ? WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, zone.getNom());
            statement.setString(2, zone.getQuartiers());
            statement.setDouble(3, zone.getPrix());
            statement.setInt(4, zone.getId());
            
            statement.executeUpdate();
            return zone;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la zone: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM zones WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la zone: " + e.getMessage());
        }
    }

    @Override
    public Optional<Zone> findByNom(String nom) {
        String sql = "SELECT * FROM zones WHERE LOWER(nom) = LOWER(?)";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToZone(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Zone mapResultSetToZone(ResultSet resultSet) throws SQLException {
        Zone zone = new Zone();
        zone.setId(resultSet.getInt("id"));
        zone.setNom(resultSet.getString("nom"));
        zone.setQuartiers(resultSet.getString("quartiers"));
        zone.setPrix(resultSet.getDouble("prix"));
        zone.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return zone;
    }
}