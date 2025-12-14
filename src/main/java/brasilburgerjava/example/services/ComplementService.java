package brasilburgerjava.example.services;

import brasilburgerjava.example.entity.Complement;
import brasilburgerjava.example.entity.TypeComplement;
import java.util.List;

public interface ComplementService {
    Complement createComplement(Complement complement);
    Complement getComplementById(Integer id);
    List<Complement> getAllComplements();
    List<Complement> getActiveComplements();
    List<Complement> getComplementsByType(TypeComplement type);
    List<Complement> getActiveComplementsByType(TypeComplement type);
    Complement updateComplement(Complement complement);
    void archiveComplement(Integer id);
    boolean complementExistsByName(String nom);
}