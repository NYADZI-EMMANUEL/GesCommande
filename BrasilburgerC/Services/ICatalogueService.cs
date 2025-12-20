using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface ICatalogueService
    {
        List<Burger> GetAllBurgers();
        List<Menu> GetAllMenus();
        List<Complement> GetAllComplements();
        List<object> GetAllProducts();
        List<object> FilterProducts(string? filter);
        Burger? GetBurgerById(int id);
        Menu? GetMenuById(int id);
        Complement? GetComplementById(int id);
        object? GetProductDetails(string type, int id);
    }
}