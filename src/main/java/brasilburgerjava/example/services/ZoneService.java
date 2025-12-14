package brasilburgerjava.example.services;

import brasilburgerjava.example.entity.Zone;
import java.util.List;

public interface ZoneService {
    Zone createZone(Zone zone);
    Zone getZoneById(Integer id);
    List<Zone> getAllZones();
    Zone updateZone(Zone zone);
    void deleteZone(Integer id);
    boolean zoneExistsByName(String nom);
}