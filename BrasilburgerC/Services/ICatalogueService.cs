using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface ICatalogueService
    {
        Task<List<Burger>> GetAllBurgers();
        Task<List<Menu>> GetAllMenus();
        Task<List<Complement>> GetAllComplements();
        Task<Burger?> GetBurgerById(int id);
        Task<Menu?> GetMenuById(int id);
        Task<Complement?> GetComplementById(int id);
        Task<List<Burger>> GetBurgersNonArchives();
        Task<List<Menu>> GetMenusNonArchives();
        Task<List<Complement>> GetComplementsNonArchives();
        Task<List<Complement>> GetComplementsByType(string type);
    }
}