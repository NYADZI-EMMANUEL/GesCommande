using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface ICommandeService
    {
        Commande? CreerCommande(int clientId, List<PanierItem> panierItems, string mode, string? adresseLivraison, int? zoneId, string typePaiement, decimal montantTotal);
        List<Commande> GetCommandesClient(int clientId);
        List<Commande> GetCommandesEnCoursClient(int clientId);
        List<Commande> GetCommandesTermineesClient(int clientId);
        Commande? GetCommandeById(int id);
    }
}