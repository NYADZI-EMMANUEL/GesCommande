package brasilburgerjava.example.repository.Impl;

import brasilburgerjava.example.config.database.Database;
import brasilburgerjava.example.config.database.DatabaseImpl;
import brasilburgerjava.example.entity.Complement;
import brasilburgerjava.example.entity.TypeComplement;
import brasilburgerjava.example.repository.ComplementRepository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ComplementRepositoryImpl implements ComplementRepository {
    private final Database database;

    public ComplementRepositoryImpl() {
        this.database = new DatabaseImpl();
    }

    @Override
    public Complement save(Complement complement) {
        String sql = "INSERT INTO complements (nom, image, description, quantite, prix, type, gestionnaire_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?::type_complement, ?) RETURNING id, created_at";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, complement.getNom());
            statement.setString(2, complement.getImage());
            statement.setString(3, complement.getDescription());
            statement.setString(4, complement.getQuantite());
            statement.setDouble(5, complement.getPrix());
            statement.setString(6, complement.getType().name());
            statement.setInt(7, complement.getGestionnaireId());
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                complement.setId(resultSet.getInt("id"));
                complement.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            }
            return complement;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du complément: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Complement> findById(Integer id) {
        String sql = "SELECT * FROM complements WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToComplement(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du complément: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Complement> findAll() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements ORDER BY type, nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                complements.add(mapResultSetToComplement(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public List<Complement> findAllActive() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements WHERE is_archived = false ORDER BY type, nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                complements.add(mapResultSetToComplement(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments actifs: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public List<Complement> findByType(TypeComplement type) {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements WHERE type = ?::type_complement ORDER BY nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, type.name());
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                complements.add(mapResultSetToComplement(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération par type: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public List<Complement> findActiveByType(TypeComplement type) {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complements WHERE type = ?::type_complement AND is_archived = false ORDER BY nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, type.name());
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                complements.add(mapResultSetToComplement(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération actifs par type: " + e.getMessage());
        }
        return complements;
    }

    @Override
    public Complement update(Complement complement) {
        String sql = "UPDATE complements SET nom = ?, image = ?, description = ?, quantite = ?, " +
                    "prix = ?, type = ?::type_complement, is_archived = ? WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, complement.getNom());
            statement.setString(2, complement.getImage());
            statement.setString(3, complement.getDescription());
            statement.setString(4, complement.getQuantite());
            statement.setDouble(5, complement.getPrix());
            statement.setString(6, complement.getType().name());
            statement.setBoolean(7, complement.getIsArchived());
            statement.setInt(8, complement.getId());
            
            statement.executeUpdate();
            return complement;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du complément: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void archive(Integer id) {
        String sql = "UPDATE complements SET is_archived = true WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du complément: " + e.getMessage());
        }
    }

    @Override
    public Optional<Complement> findByNom(String nom) {
        String sql = "SELECT * FROM complements WHERE LOWER(nom) = LOWER(?)";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToComplement(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Complement mapResultSetToComplement(ResultSet resultSet) throws SQLException {
        Complement complement = new Complement();
        complement.setId(resultSet.getInt("id"));
        complement.setNom(resultSet.getString("nom"));
        complement.setImage(resultSet.getString("image"));
        complement.setIsArchived(resultSet.getBoolean("is_archived"));
        complement.setDescription(resultSet.getString("description"));
        complement.setQuantite(resultSet.getString("quantite"));
        complement.setPrix(resultSet.getDouble("prix"));
        complement.setType(TypeComplement.valueOf(resultSet.getString("type")));
        complement.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        complement.setGestionnaireId(resultSet.getInt("gestionnaire_id"));
        return complement;
    }
}