package brasilburgerjava.example.services;

import brasilburgerjava.example.entity.Menu;
import java.util.List;

public interface MenuService {
    Menu createMenu(Menu menu);
    Menu getMenuById(Integer id);
    List<Menu> getAllMenus();
    Menu updateMenu(Menu menu);
    void deleteMenu(Integer id);
    boolean menuExistsByName(String nom);
    Double calculateMenuPrice(Integer burgerId, Integer boissonId, Integer friteId);
}