using Microsoft.AspNetCore.Mvc;
using BrasilburgerC.Services;
using BrasilburgerC.Data;

namespace BrasilburgerC.Controllers
{
    public class CommandeController : Controller
    {
        private readonly ICommandeService _commandeService;
        private readonly IPaiementService _paiementService;
        private readonly IPanierService _panierService;
        private readonly ICatalogueService _catalogueService;
        private readonly BrasilburgerCDbContext _context;

        public CommandeController(
            ICommandeService commandeService,
            IPaiementService paiementService,
            IPanierService panierService,
            ICatalogueService catalogueService,
            BrasilburgerCDbContext context)
        {
            _commandeService = commandeService;
            _paiementService = paiementService;
            _panierService = panierService;
            _catalogueService = catalogueService;
            _context = context;
        }

        private string GetSessionId()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            return userId?.ToString() ?? HttpContext.Session.Id;
        }

        // GET: Commande/Create (Page de finalisation)
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
                TempData["ErrorMessage"] = "Votre panier est vide !";
                return RedirectToAction("Index", "Panier");
            }

            var total = _panierService.CalculerTotal(sessionId);
            var complements = _catalogueService.GetAllComplements();
            var zones = _context.Zones.OrderBy(z => z.Nom).ToList();

            ViewBag.Total = total;
            ViewBag.Panier = panier;
            ViewBag.Complements = complements;
            ViewBag.Zones = zones;
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View();
        }

        // POST: Commande/Valider
        [HttpPost]
        public IActionResult Valider(string mode, string? adresse, int? zoneId, string typePaiement)
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId == null)
            {
                return RedirectToAction("Login", "Auth");
            }

            // Validation
            if (string.IsNullOrEmpty(mode))
            {
                TempData["ErrorMessage"] = "Veuillez choisir un mode de réception !";
                return RedirectToAction("Create");
            }

            if (mode == "Livraison" && (zoneId == null || string.IsNullOrEmpty(adresse)))
            {
                TempData["ErrorMessage"] = "Veuillez choisir une zone et entrer votre adresse pour la livraison !";
                return RedirectToAction("Create");
            }

            if (string.IsNullOrEmpty(typePaiement))
            {
                TempData["ErrorMessage"] = "Veuillez choisir un mode de paiement !";
                return RedirectToAction("Create");
            }

            var sessionId = GetSessionId();
            var panier = _panierService.GetPanier(sessionId);

            if (panier.Count == 0)
            {
                TempData["ErrorMessage"] = "Votre panier est vide !";
                return RedirectToAction("Index", "Panier");
            }

            var total = _panierService.CalculerTotal(sessionId);

            // Ajouter frais de livraison si applicable
            if (mode == "Livraison" && zoneId != null)
            {
                var zone = _context.Zones.Find(zoneId.Value);
                if (zone != null)
                {
                    total += zone.Prix;
                }
            }

            // Créer la commande
            var commande = _commandeService.CreerCommande(
                userId.Value,
                panier,
                mode,
                mode == "Livraison" ? adresse : null,
                mode == "Livraison" ? zoneId : null,
                typePaiement,
                total
            );

            if (commande == null)
            {
                TempData["ErrorMessage"] = "Erreur lors de la création de la commande !";
                return RedirectToAction("Create");
            }

            // Créer le paiement
            var paiement = _paiementService.CreerPaiement(commande.Id, total, typePaiement);

            if (paiement == null)
            {
                TempData["ErrorMessage"] = "Erreur lors de la création du paiement !";
                return RedirectToAction("Create");
            }

            // Vider le panier
            _panierService.ViderPanier(sessionId);

            TempData["SuccessMessage"] = "Commande validée avec succès !";
            return RedirectToAction("Index");
        }

        // GET: Commande/Index (Liste des commandes)
        public IActionResult Index()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId == null)
            {
                return RedirectToAction("Login", "Auth");
            }

            var commandesEnCours = _commandeService.GetCommandesEnCoursClient(userId.Value);
            var commandesTerminees = _commandeService.GetCommandesTermineesClient(userId.Value);

            ViewBag.CommandesEnCours = commandesEnCours;
            ViewBag.CommandesTerminees = commandesTerminees;
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View();
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