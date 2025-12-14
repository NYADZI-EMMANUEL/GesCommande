package brasilburgerjava.example.services.Impl;


import brasilburgerjava.example.entity.Zone;
import brasilburgerjava.example.repository.ZoneRepository;
import brasilburgerjava.example.repository.Impl.ZoneRepositoryImpl;
import brasilburgerjava.example.services.ZoneService;


import java.util.List;


public class ZoneServiceImpl implements ZoneService {
    private final ZoneRepository zoneRepository;


    public ZoneServiceImpl() {
        this.zoneRepository = new ZoneRepositoryImpl();
    }


    @Override
    public Zone createZone(Zone zone) {
        if (zoneRepository.findByNom(zone.getNom()).isPresent()) {
            throw new IllegalArgumentException("Une zone avec le nom '" + zone.getNom() + "' existe déjà !");
        }
       
        Zone savedZone = zoneRepository.save(zone);
        if (savedZone != null) {
            System.out.println("Zone créée avec succès !");
        }
        return savedZone;
    }


    @Override
    public Zone getZoneById(Integer id) {
        return zoneRepository.findById(id).orElse(null);
    }


    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }


    @Override
    public Zone updateZone(Zone zone) {
        zoneRepository.findByNom(zone.getNom())
            .ifPresent(existingZone -> {
                if (!existingZone.getId().equals(zone.getId())) {
                    throw new IllegalArgumentException("Une autre zone avec le nom '" + zone.getNom() + "' existe déjà !");
                }
            });
       
        return zoneRepository.update(zone);
    }


    @Override
    public void deleteZone(Integer id) {
        zoneRepository.delete(id);
        System.out.println("Zone supprimée avec succès !");
    }


    @Override
    public boolean zoneExistsByName(String nom) {
        return zoneRepository.findByNom(nom).isPresent();
    }
}

