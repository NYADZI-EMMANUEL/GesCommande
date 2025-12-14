package brasilburgerjava.example.services.Impl;


import brasilburgerjava.example.entity.Menu;
import brasilburgerjava.example.repository.MenuRepository;
import brasilburgerjava.example.repository.Impl.MenuRepositoryImpl;
import brasilburgerjava.example.services.MenuService;


import java.util.List;


public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;


    public MenuServiceImpl() {
        this.menuRepository = new MenuRepositoryImpl();
    }


    @Override
    public Menu createMenu(Menu menu) {
        if (menuRepository.findByNom(menu.getNom()).isPresent()) {
            throw new IllegalArgumentException("Un menu avec le nom '" + menu.getNom() + "' existe déjà !");
        }
       
        Double prix = menuRepository.calculatePrix(menu.getBurgerId(), menu.getBoissonId(), menu.getFriteId());
        menu.setPrix(prix);
       
        Menu savedMenu = menuRepository.save(menu);
        if (savedMenu != null) {
            System.out.println("Menu créé avec succès ! Prix total: " + prix + " FCFA");
        }
        return savedMenu;
    }


    @Override
    public Menu getMenuById(Integer id) {
        return menuRepository.findById(id).orElse(null);
    }


    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }


    @Override
    public Menu updateMenu(Menu menu) {
        menuRepository.findByNom(menu.getNom())
            .ifPresent(existingMenu -> {
                if (!existingMenu.getId().equals(menu.getId())) {
                    throw new IllegalArgumentException("Un autre menu avec le nom '" + menu.getNom() + "' existe déjà !");
                }
            });
       
        Double prix = menuRepository.calculatePrix(menu.getBurgerId(), menu.getBoissonId(), menu.getFriteId());
        menu.setPrix(prix);
       
        return menuRepository.update(menu);
    }


    @Override
    public void deleteMenu(Integer id) {
        menuRepository.delete(id);
        System.out.println("Menu supprimé avec succès !");
    }


    @Override
    public boolean menuExistsByName(String nom) {
        return menuRepository.findByNom(nom).isPresent();
    }


    @Override
    public Double calculateMenuPrice(Integer burgerId, Integer boissonId, Integer friteId) {
        return menuRepository.calculatePrix(burgerId, boissonId, friteId);
    }
}

