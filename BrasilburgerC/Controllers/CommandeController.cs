using Microsoft.AspNetCore.Mvc;
using BrasilburgerC.Services;
using BrasilburgerC.Models;

namespace BrasilburgerC.Controllers
{
    public class CommandeController : Controller
    {
        private readonly IPanierService _panierService;
        private readonly ICatalogueService _catalogueService;
        private readonly ICommandeService _commandeService;

        public CommandeController(IPanierService panierService, ICatalogueService catalogueService, ICommandeService commandeService)
        {
            _panierService = panierService;
            _catalogueService = catalogueService;
            _commandeService = commandeService;
        }

        private string GetSessionId()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            return userId?.ToString() ?? HttpContext.Session.Id;
        }

        // GET: Commande/Create (Page de validation)
        public IActionResult Create()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId == null)
            {
                return RedirectToAction("Login", "Auth");
            }

            var sessionId = GetSessionId();
            var panier = _panierService.GetPanier(sessionId);

            if (panier.Count == 0)
            {
                TempData["ErrorMessage"] = "Votre panier est vide";
                return RedirectToAction("Index", "Panier");
            }

            var total = _panierService.CalculerTotal(sessionId);
            var complements = _catalogueService.GetAllComplements();
            var zones = _commandeService.GetAllZones();

            ViewBag.Total = total;
            ViewBag.Panier = panier;
            ViewBag.Complements = complements;
            ViewBag.Zones = zones;
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View();
        }

        // POST: Commande/Create (Validation de la commande)
        [HttpPost]
        public IActionResult Create(string modeReception, int? zoneId, string? adresseLivraison, string modePaiement, List<int>? complementIds)
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId == null)
            {
                return RedirectToAction("Login", "Auth");
            }

            var sessionId = GetSessionId();
            var panier = _panierService.GetPanier(sessionId);

            if (panier.Count == 0)
            {
                TempData["ErrorMessage"] = "Votre panier est vide";
                return RedirectToAction("Index", "Panier");
            }

            // Validation
            if (string.IsNullOrEmpty(modeReception))
            {
                TempData["ErrorMessage"] = "Veuillez choisir un mode de réception";
                return RedirectToAction("Create");
            }

            if (modeReception == "Livraison" && (zoneId == null || string.IsNullOrEmpty(adresseLivraison)))
            {
                TempData["ErrorMessage"] = "Veuillez choisir une zone et entrer votre adresse pour la livraison";
                return RedirectToAction("Create");
            }

            if (string.IsNullOrEmpty(modePaiement))
            {
                TempData["ErrorMessage"] = "Veuillez choisir un mode de paiement";
                return RedirectToAction("Create");
            }

            // Ajouter les compléments sélectionnés au panier
            if (complementIds != null && complementIds.Count > 0)
            {
                foreach (var complementId in complementIds)
                {
                    var complement = _catalogueService.GetComplementById(complementId);
                    if (complement != null)
                    {
                        var complementItem = new PanierItem
                        {
                            ProduitId = complement.Id,
                            Type = "Complement",
                            Nom = complement.Nom,
                            Prix = complement.Prix,
                            Image = complement.Image,
                            Quantite = 1
                        };
                        _panierService.AjouterAuPanier(sessionId, complementItem);
                    }
                }

                // Récupérer le panier mis à jour
                panier = _panierService.GetPanier(sessionId);
            }

            // Calculer le total avec frais de livraison
            decimal totalPanier = _panierService.CalculerTotal(sessionId);
            decimal fraisLivraison = 0;

            if (modeReception == "Livraison" && zoneId != null)
            {
                var zone = _commandeService.GetZoneById(zoneId.Value);
                if (zone != null)
                {
                    fraisLivraison = zone.Prix;
                }
            }

            decimal montantTotal = totalPanier + fraisLivraison;

            // Créer la commande en base de données
            var commandeId = _commandeService.CreerCommande(
                userId.Value,
                panier,
                modeReception,
                adresseLivraison,
                zoneId,
                modePaiement,
                montantTotal
            );

            // Vider le panier
            _panierService.ViderPanier(sessionId);
            
            TempData["SuccessMessage"] = $"Commande #{commandeId} validée avec succès ! Montant total : {montantTotal} fcfa";
            
            return RedirectToAction("Index", "Commande");
        }

        // GET: Commande/Index (Liste des commandes)
        public IActionResult Index()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId == null)
            {
                return RedirectToAction("Login", "Auth");
            }

            var commandes = _commandeService.GetCommandesByClient(userId.Value);

            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View(commandes);
        }

        // GET: Commande/Details/5
        public IActionResult Details(int id)
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId == null)
            {
                return RedirectToAction("Login", "Auth");
            }

            var commande = _commandeService.GetCommandeById(id);

            if (commande == null || commande.ClientId != userId.Value)
            {
                return NotFound();
            }

            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View(commande);
        }
    }
}