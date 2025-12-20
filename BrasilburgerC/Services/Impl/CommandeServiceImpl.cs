using BrasilburgerC.Data;
using BrasilburgerC.Models;
using Microsoft.EntityFrameworkCore;


namespace BrasilburgerC.Services.Impl
{
    public class CommandeServiceImpl : ICommandeService
    {
        private readonly BrasilburgerCDbContext _context;

        public CommandeServiceImpl(BrasilburgerCDbContext context)
        {
            _context = context;
        }

        public Commande? CreerCommande(int clientId, List<PanierItem> panierItems, string mode, string? adresseLivraison, int? zoneId, string typePaiement, decimal montantTotal)
        {
            // Créer la commande
            var commande = new Commande
            {
                ClientId = clientId,
                DateCom = DateTime.UtcNow,
                TotalPrix = montantTotal,
                Mode = mode,
                Statut = "En_attente",
                AdresseLivraison = adresseLivraison,
                ZoneId = zoneId,
                CreatedAt = DateTime.UtcNow
            };

            _context.Commandes.Add(commande);
            _context.SaveChanges();

            // Créer les lignes commande_produits
            foreach (var item in panierItems)
            {
                // Ajouter le produit principal (burger ou menu)
                var commandeProduit = new CommandeProduit
                {
                    CommandeId = commande.Id,
                    Quantite = item.Quantite,
                    SousTotal = item.Prix * item.Quantite,
                    BurgerId = item.Type == "Burger" ? item.ProduitId : null,
                    MenuId = item.Type == "Menu" ? item.ProduitId : null,
                    ComplementId = item.Type == "Complement" ? item.ProduitId : null
                };

                _context.CommandeProduits.Add(commandeProduit);

                // Ajouter les compléments
                foreach (var complement in item.Complements)
                {
                    var commandeComplement = new CommandeProduit
                    {
                        CommandeId = commande.Id,
                        Quantite = complement.Quantite,
                        SousTotal = complement.Prix * complement.Quantite,
                        ComplementId = complement.Id
                    };

                    _context.CommandeProduits.Add(commandeComplement);
                }
            }

            _context.SaveChanges();

            return commande;
        }

        public List<Commande> GetCommandesClient(int clientId)
        {
            return _context.Commandes
                .Include(c => c.CommandeProduits)
                .Include(c => c.Zone)
                .Include(c => c.Paiement)
                .Where(c => c.ClientId == clientId)
                .OrderByDescending(c => c.DateCom)
                .ToList();
        }

        public List<Commande> GetCommandesEnCoursClient(int clientId)
        {
            return _context.Commandes
                .Include(c => c.CommandeProduits)
                .Include(c => c.Paiement)
                .Where(c => c.ClientId == clientId && 
                           (c.Statut == "En_attente" || c.Statut == "En_preparation" || c.Statut == "Pret"))
                .OrderByDescending(c => c.DateCom)
                .ToList();
        }

        public List<Commande> GetCommandesTermineesClient(int clientId)
        {
            return _context.Commandes
                .Include(c => c.CommandeProduits)
                .Include(c => c.Paiement)
                .Where(c => c.ClientId == clientId && c.Statut == "Terminer")
                .OrderByDescending(c => c.DateCom)
                .ToList();
        }

        public Commande? GetCommandeById(int id)
        {
            return _context.Commandes
                .Include(c => c.CommandeProduits!)
                    .ThenInclude(cp => cp.Burger)
                .Include(c => c.CommandeProduits!)
                    .ThenInclude(cp => cp.Menu)
                .Include(c => c.CommandeProduits!)
                    .ThenInclude(cp => cp.Complement)
                .Include(c => c.Zone)
                .Include(c => c.Paiement)
                .FirstOrDefault(c => c.Id == id);
        }
    }
}