package brasilburgerjava.example.repository;

import brasilburgerjava.example.entity.Complement;
import brasilburgerjava.example.entity.TypeComplement;
import java.util.List;
import java.util.Optional;

public interface ComplementRepository {
    Complement save(Complement complement);
    Optional<Complement> findById(Integer id);
    List<Complement> findAll();
    List<Complement> findAllActive();
    List<Complement> findByType(TypeComplement type);
    List<Complement> findActiveByType(TypeComplement type);
    Complement update(Complement complement);
    void archive(Integer id);
    Optional<Complement> findByNom(String nom);
}