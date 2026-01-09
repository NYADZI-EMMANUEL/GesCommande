using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface ICommandeService
    {
        Commande? CreerCommande(int clientId, List<PanierItem> panierItems, string mode, string? adresseLivraison, int? zoneId, string typePaiement, decimal montantTotal);
        List<Commande> GetCommandesByClient(int clientId);
        Commande? GetCommandeById(int commandeId);
        List<Zone> GetAllZones();
        Zone? GetZoneById(int zoneId);
    }
}