using Microsoft.AspNetCore.Mvc;
using BrasilburgerC.Services;
using BrasilburgerC.Models;

namespace BrasilburgerC.Controllers
{
    public class PanierController : Controller
    {
        private readonly IPanierService _panierService;
        private readonly ICatalogueService _catalogueService;
        private readonly ICommandeService _commandeService;

        public PanierController(IPanierService panierService, ICatalogueService catalogueService, ICommandeService commandeService)
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

        // GET: Panier/Index avec zoneId pour calcul
        public IActionResult Index(int? zoneId)
        {
            var sessionId = GetSessionId();
            var panier = _panierService.GetPanier(sessionId);
            var total = _panierService.CalculerTotal(sessionId);

            // Calculer les frais de livraison si une zone est sélectionnée
            decimal fraisLivraison = 0;
            if (zoneId != null && zoneId > 0)
            {
                var zone = _commandeService.GetZoneById(zoneId.Value);
                if (zone != null)
                {
                    fraisLivraison = zone.Prix;
                    ViewBag.SelectedZoneId = zoneId.Value;
                }
            }

            ViewBag.Total = total;
            ViewBag.FraisLivraison = fraisLivraison;
            ViewBag.TotalFinal = total + fraisLivraison;
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            // Récupérer tous les compléments disponibles
            var complements = _catalogueService.GetAllComplements();
            ViewBag.Complements = complements;

            // Récupérer toutes les zones
            var zones = _commandeService.GetAllZones();
            ViewBag.Zones = zones;

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

            // Rediriger vers le panier
            return RedirectToAction("Index");
        }

        // POST: Panier/AjouterComplements (Ajouter plusieurs compléments)
        [HttpPost]
        public IActionResult AjouterComplements(List<int>? complementIds, int? zoneId)
        {
            var sessionId = GetSessionId();

            if (complementIds != null && complementIds.Count > 0)
            {
                foreach (var complementId in complementIds)
                {
                    if (complementId > 0)
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
                }
                TempData["SuccessMessage"] = "Compléments ajoutés au panier !";
            }

            // Rediriger vers Index avec la zone sélectionnée
            return RedirectToAction("Index", new { zoneId = zoneId });
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

        // POST: Panier/Valider (Créer la commande)
        [HttpPost]
        public IActionResult Valider(string modeReception, int? zoneId, string? adresseLivraison, string modePaiement)
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
                return RedirectToAction("Index");
            }

            // Validation du mode de réception
            if (string.IsNullOrEmpty(modeReception))
            {
                TempData["ErrorMessage"] = "Veuillez choisir un mode de réception";
                return RedirectToAction("Index", new { zoneId = zoneId });
            }

            // Validation selon le mode
            if (modeReception == "Livraison")
            {
                // Livraison : zone ET adresse obligatoires
                if (zoneId == null)
                {
                    TempData["ErrorMessage"] = "Veuillez choisir une zone pour la livraison";
                    return RedirectToAction("Index", new { zoneId = zoneId });
                }
                if (string.IsNullOrWhiteSpace(adresseLivraison))
                {
                    TempData["ErrorMessage"] = "Veuillez entrer votre adresse de livraison";
                    return RedirectToAction("Index", new { zoneId = zoneId });
                }
            }
            else if (modeReception == "Sur_place")
            {
                // Sur place : numéro de table obligatoire
                if (string.IsNullOrWhiteSpace(adresseLivraison))
                {
                    TempData["ErrorMessage"] = "Veuillez entrer votre numéro de table";
                    return RedirectToAction("Index", new { zoneId = zoneId });
                }
            }
            // Emporter : rien n'est obligatoire

            // Validation du paiement
            if (string.IsNullOrEmpty(modePaiement))
            {
                TempData["ErrorMessage"] = "Veuillez choisir un mode de paiement";
                return RedirectToAction("Index", new { zoneId = zoneId });
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
            var commande = _commandeService.CreerCommande(
                userId.Value,
                panier,
                modeReception,
                adresseLivraison,
                zoneId,
                modePaiement,
                montantTotal
            );

            if (commande != null)
            {
                // Vider le panier
                _panierService.ViderPanier(sessionId);
                TempData["SuccessMessage"] = $"Commande #{commande.Id} validée avec succès ! Montant total : {montantTotal} fcfa";
                return RedirectToAction("Index", "Commande");
            }

            TempData["ErrorMessage"] = "Erreur lors de la création de la commande";
            return RedirectToAction("Index", new { zoneId = zoneId });
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