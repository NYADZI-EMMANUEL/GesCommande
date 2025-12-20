using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface IPanierService
    {
        List<PanierItem> GetPanier(string sessionId);
        void AjouterAuPanier(string sessionId, PanierItem item);
        void AjouterComplement(string sessionId, int panierItemId, ComplementItem complement);
        void SupprimerDuPanier(string sessionId, int panierItemId);
        void ViderPanier(string sessionId);
        void UpdateQuantite(string sessionId, int panierItemId, int quantite);
        decimal CalculerTotal(string sessionId);
        int GetNombreArticles(string sessionId);
    }
}