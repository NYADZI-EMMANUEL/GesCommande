package brasilburgerjava.example.repository.Impl;

import brasilburgerjava.example.config.database.Database;
import brasilburgerjava.example.config.database.DatabaseImpl;
import brasilburgerjava.example.entity.Menu;
import brasilburgerjava.example.repository.MenuRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryImpl implements MenuRepository {
    private final Database database;

    public MenuRepositoryImpl() {
        this.database = new DatabaseImpl();
    }

    @Override
    public Menu save(Menu menu) {
        String sql = "INSERT INTO menus (nom, image, prix, burger_id, boisson_id, frite_id, gestionnaire_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, menu.getNom());
            statement.setString(2, menu.getImage());
            statement.setDouble(3, menu.getPrix());
            statement.setInt(4, menu.getBurgerId());
            statement.setInt(5, menu.getBoissonId());
            statement.setInt(6, menu.getFriteId());
            statement.setInt(7, menu.getGestionnaireId());
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                menu.setId(resultSet.getInt("id"));
                menu.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            }
            return menu;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde du menu: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Menu> findById(Integer id) {
        String sql = "SELECT * FROM menus WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToMenu(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du menu: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menus ORDER BY nom";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                menus.add(mapResultSetToMenu(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des menus: " + e.getMessage());
        }
        return menus;
    }

    @Override
    public Menu update(Menu menu) {
        String sql = "UPDATE menus SET nom = ?, image = ?, prix = ?, burger_id = ?, " +
                    "boisson_id = ?, frite_id = ? WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, menu.getNom());
            statement.setString(2, menu.getImage());
            statement.setDouble(3, menu.getPrix());
            statement.setInt(4, menu.getBurgerId());
            statement.setInt(5, menu.getBoissonId());
            statement.setInt(6, menu.getFriteId());
            statement.setInt(7, menu.getId());
            
            statement.executeUpdate();
            return menu;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du menu: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM menus WHERE id = ?";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du menu: " + e.getMessage());
        }
    }

    @Override
    public Optional<Menu> findByNom(String nom) {
        String sql = "SELECT * FROM menus WHERE LOWER(nom) = LOWER(?)";
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToMenu(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Double calculatePrix(Integer burgerId, Integer boissonId, Integer friteId) {
        String sql = """
            SELECT 
                COALESCE((SELECT prix FROM burgers WHERE id = ?), 0) +
                COALESCE((SELECT prix FROM complements WHERE id = ?), 0) +
                COALESCE((SELECT prix FROM complements WHERE id = ?), 0) as total_prix
            """;
        
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, burgerId);
            statement.setInt(2, boissonId);
            statement.setInt(3, friteId);
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("total_prix");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du prix: " + e.getMessage());
        }
        return 0.0;
    }

    private Menu mapResultSetToMenu(ResultSet resultSet) throws SQLException {
        Menu menu = new Menu();
        menu.setId(resultSet.getInt("id"));
        menu.setNom(resultSet.getString("nom"));
        menu.setImage(resultSet.getString("image"));
        menu.setPrix(resultSet.getDouble("prix"));
        menu.setBurgerId(resultSet.getInt("burger_id"));
        menu.setBoissonId(resultSet.getInt("boisson_id"));
        menu.setFriteId(resultSet.getInt("frite_id"));
        menu.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        menu.setGestionnaireId(resultSet.getInt("gestionnaire_id"));
        return menu;
    }
}