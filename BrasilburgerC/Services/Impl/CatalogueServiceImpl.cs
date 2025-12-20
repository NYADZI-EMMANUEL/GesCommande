using BrasilburgerC.Data;
using BrasilburgerC.Models;
using Microsoft.EntityFrameworkCore;

namespace BrasilburgerC.Services.Impl
{
    public class CatalogueServiceImpl : ICatalogueService
    {
        private readonly BrasilburgerCDbContext _context;

        public CatalogueServiceImpl(BrasilburgerCDbContext context)
        {
            _context = context;
        }

        public List<Burger> GetAllBurgers()
        {
            return _context.Burgers
                .Where(b => !b.IsArchived)
                .OrderBy(b => b.Nom)
                .ToList();
        }

        public List<Menu> GetAllMenus()
        {
            return _context.Menus
                .Include(m => m.Burger)
                .Include(m => m.Boisson)
                .Include(m => m.Frite)
                .OrderBy(m => m.Nom)
                .ToList();
        }

        public List<Complement> GetAllComplements()
        {
            return _context.Complements
                .Where(c => !c.IsArchived)
                .OrderBy(c => c.Nom)
                .ToList();
        }

        public List<object> GetAllProducts()
        {
            var products = new List<object>();

            // Ajouter les burgers
            var burgers = GetAllBurgers().Select(b => new
            {
                Id = b.Id,
                Nom = b.Nom,
                Prix = b.Prix,
                Image = b.Image,
                Description = b.Description,
                Type = "Burger"
            });

            // Ajouter les menus
            var menus = GetAllMenus().Select(m => new
            {
                Id = m.Id,
                Nom = m.Nom,
                Prix = m.Prix,
                Image = m.Image,
                Description = $"Menu: {m.Burger?.Nom} + {m.Boisson?.Nom} + {m.Frite?.Nom}",
                Type = "Menu"
            });

            // Ajouter les compléments
            var complements = GetAllComplements().Select(c => new
            {
                Id = c.Id,
                Nom = c.Nom,
                Prix = c.Prix,
                Image = c.Image,
                Description = c.Description,
                Type = "Complement"
            });

            products.AddRange(burgers);
            products.AddRange(menus);
            products.AddRange(complements);

            return products;
        }

        public List<object> FilterProducts(string? filter)
        {
            if (string.IsNullOrEmpty(filter) || filter == "tous")
            {
                return GetAllProducts();
            }

            var products = new List<object>();

            if (filter.ToLower() == "burger")
            {
                var burgers = GetAllBurgers().Select(b => new
                {
                    Id = b.Id,
                    Nom = b.Nom,
                    Prix = b.Prix,
                    Image = b.Image,
                    Description = b.Description,
                    Type = "Burger"
                });
                products.AddRange(burgers);
            }
            else if (filter.ToLower() == "menu")
            {
                var menus = GetAllMenus().Select(m => new
                {
                    Id = m.Id,
                    Nom = m.Nom,
                    Prix = m.Prix,
                    Image = m.Image,
                    Description = $"Menu: {m.Burger?.Nom} + {m.Boisson?.Nom} + {m.Frite?.Nom}",
                    Type = "Menu"
                });
                products.AddRange(menus);
            }

            return products;
        }

        public Burger? GetBurgerById(int id)
        {
            return _context.Burgers
                .FirstOrDefault(b => b.Id == id && !b.IsArchived);
        }

        public Menu? GetMenuById(int id)
        {
            return _context.Menus
                .Include(m => m.Burger)
                .Include(m => m.Boisson)
                .Include(m => m.Frite)
                .FirstOrDefault(m => m.Id == id);
        }

        public Complement? GetComplementById(int id)
        {
            return _context.Complements
                .FirstOrDefault(c => c.Id == id && !c.IsArchived);
        }

        public object? GetProductDetails(string type, int id)
        {
            if (type.ToLower() == "burger")
            {
                return GetBurgerById(id);
            }
            else if (type.ToLower() == "menu")
            {
                return GetMenuById(id);
            }
            else if (type.ToLower() == "complement")
            {
                return GetComplementById(id);
            }

            return null;
        }
    }
}