using Microsoft.AspNetCore.Mvc;
using BrasilburgerC.Services;
using BrasilburgerC.Models;

namespace BrasilburgerC.Controllers
{
    public class PanierController : Controller
    {
        private readonly IPanierService _panierService;
        private readonly ICatalogueService _catalogueService;

        public PanierController(IPanierService panierService, ICatalogueService catalogueService)
        {
            _panierService = panierService;
            _catalogueService = catalogueService;
        }

        private string GetSessionId()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            return userId?.ToString() ?? HttpContext.Session.Id;
        }

        // GET: Panier/Index
        public IActionResult Index()
        {
            var sessionId = GetSessionId();
            var panier = _panierService.GetPanier(sessionId);
            var total = _panierService.CalculerTotal(sessionId);

            ViewBag.Total = total;
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            // Récupérer tous les compléments disponibles
            var complements = _catalogueService.GetAllComplements();
            ViewBag.Complements = complements;

            return View(panier);
        }

        // POST: Panier/AjouterProduit
        [HttpPost]
        public IActionResult AjouterProduit(string type, int id, int quantite = 1)
        {
            var sessionId = GetSessionId();
            PanierItem? item = null;

            if (type.ToLower() == "burger")
            {
                var burger = _catalogueService.GetBurgerById(id);
                if (burger != null)
                {
                    item = new PanierItem
                    {
                        ProduitId = burger.Id,
                        Type = "Burger",
                        Nom = burger.Nom,
                        Prix = burger.Prix,
                        Image = burger.Image,
                        Quantite = quantite
                    };
                }
            }
            else if (type.ToLower() == "menu")
            {
                var menu = _catalogueService.GetMenuById(id);
                if (menu != null)
                {
                    item = new PanierItem
                    {
                        ProduitId = menu.Id,
                        Type = "Menu",
                        Nom = menu.Nom,
                        Prix = menu.Prix,
                        Image = menu.Image,
                        Quantite = quantite
                    };
                }
            }
            else if (type.ToLower() == "complement")
            {
                var complement = _catalogueService.GetComplementById(id);
                if (complement != null)
                {
                    item = new PanierItem
                    {
                        ProduitId = complement.Id,
                        Type = "Complement",
                        Nom = complement.Nom,
                        Prix = complement.Prix,
                        Image = complement.Image,
                        Quantite = quantite
                    };
                }
            }

            if (item != null)
            {
                _panierService.AjouterAuPanier(sessionId, item);
                TempData["SuccessMessage"] = $"{item.Nom} a été ajouté au panier !";
            }

            // Rediriger vers le catalogue ou rester sur la page
            return RedirectToAction("Index", "Catalogue");
        }

        // POST: Panier/AjouterComplement
        [HttpPost]
        public IActionResult AjouterComplement(int panierItemId, int complementId, int quantite = 1)
        {
            var sessionId = GetSessionId();
            var complement = _catalogueService.GetComplementById(complementId);

            if (complement != null)
            {
                var complementItem = new ComplementItem
                {
                    Id = complement.Id,
                    Nom = complement.Nom,
                    Prix = complement.Prix,
                    Quantite = quantite
                };

                _panierService.AjouterComplement(sessionId, panierItemId, complementItem);
                TempData["SuccessMessage"] = $"{complement.Nom} a été ajouté comme complément !";
            }

            return RedirectToAction("Index");
        }

        // POST: Panier/UpdateQuantite
        [HttpPost]
        public IActionResult UpdateQuantite(int panierItemId, int quantite)
        {
            var sessionId = GetSessionId();
            _panierService.UpdateQuantite(sessionId, panierItemId, quantite);

            return RedirectToAction("Index");
        }

        // POST: Panier/Supprimer
        [HttpPost]
        public IActionResult Supprimer(int panierItemId)
        {
            var sessionId = GetSessionId();
            _panierService.SupprimerDuPanier(sessionId, panierItemId);
            TempData["SuccessMessage"] = "Article supprimé du panier !";

            return RedirectToAction("Index");
        }

        // POST: Panier/Vider
        [HttpPost]
        public IActionResult Vider()
        {
            var sessionId = GetSessionId();
            _panierService.ViderPanier(sessionId);
            TempData["SuccessMessage"] = "Panier vidé !";

            return RedirectToAction("Index");
        }
    }
}