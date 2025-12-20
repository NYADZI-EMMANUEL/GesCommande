using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface IPaiementService
    {
        Paiement? CreerPaiement(int commandeId, decimal montant, string typePaiement);
        Paiement? GetPaiementByCommandeId(int commandeId);
    }
}