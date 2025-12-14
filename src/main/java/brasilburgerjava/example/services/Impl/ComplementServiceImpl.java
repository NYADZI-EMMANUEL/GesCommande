package brasilburgerjava.example.services.Impl;


import brasilburgerjava.example.entity.Complement;
import brasilburgerjava.example.entity.TypeComplement;
import brasilburgerjava.example.repository.ComplementRepository;
import brasilburgerjava.example.repository.Impl.ComplementRepositoryImpl;
import brasilburgerjava.example.services.ComplementService;


import java.util.List;


public class ComplementServiceImpl implements ComplementService {
    private final ComplementRepository complementRepository;


    public ComplementServiceImpl() {
        this.complementRepository = new ComplementRepositoryImpl();
    }


    @Override
    public Complement createComplement(Complement complement) {
        if (complementRepository.findByNom(complement.getNom()).isPresent()) {
            throw new IllegalArgumentException(" Un complément avec le nom '" + complement.getNom() + "' existe déjà !");
        }
       
        Complement savedComplement = complementRepository.save(complement);
        if (savedComplement != null) {
            System.out.println("Complément créé avec succès !");
        }
        return savedComplement;
    }


    @Override
    public Complement getComplementById(Integer id) {
        return complementRepository.findById(id).orElse(null);
    }


    @Override
    public List<Complement> getAllComplements() {
        return complementRepository.findAll();
    }


    @Override
    public List<Complement> getActiveComplements() {
        return complementRepository.findAllActive();
    }


    @Override
    public List<Complement> getComplementsByType(TypeComplement type) {
        return complementRepository.findByType(type);
    }


    @Override
    public List<Complement> getActiveComplementsByType(TypeComplement type) {
        return complementRepository.findActiveByType(type);
    }


    @Override
    public Complement updateComplement(Complement complement) {
        complementRepository.findByNom(complement.getNom())
            .ifPresent(existingComplement -> {
                if (!existingComplement.getId().equals(complement.getId())) {
                    throw new IllegalArgumentException("❌ Un autre complément avec le nom '" + complement.getNom() + "' existe déjà !");
                }
            });
       
        return complementRepository.update(complement);
    }


    @Override
    public void archiveComplement(Integer id) {
        complementRepository.archive(id);
        System.out.println("✅ Complément archivé avec succès !");
    }


    @Override
    public boolean complementExistsByName(String nom) {
        return complementRepository.findByNom(nom).isPresent();
    }
}

