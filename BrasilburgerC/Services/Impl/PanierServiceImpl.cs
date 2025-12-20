using BrasilburgerC.Models;
using System.Text.Json;

namespace BrasilburgerC.Services.Impl
{
    public class PanierServiceImpl : IPanierService
    {
        private readonly IHttpContextAccessor _httpContextAccessor;
        private const string PANIER_KEY = "Panier_";

        public PanierServiceImpl(IHttpContextAccessor httpContextAccessor)
        {
            _httpContextAccessor = httpContextAccessor;
        }

        private string GetPanierKey(string sessionId)
        {
            return $"{PANIER_KEY}{sessionId}";
        }

        public List<PanierItem> GetPanier(string sessionId)
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            var panierJson = session?.GetString(GetPanierKey(sessionId));

            if (string.IsNullOrEmpty(panierJson))
            {
                return new List<PanierItem>();
            }

            return JsonSerializer.Deserialize<List<PanierItem>>(panierJson) ?? new List<PanierItem>();
        }

        private void SavePanier(string sessionId, List<PanierItem> panier)
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            var panierJson = JsonSerializer.Serialize(panier);
            session?.SetString(GetPanierKey(sessionId), panierJson);
        }

        public void AjouterAuPanier(string sessionId, PanierItem item)
        {
            var panier = GetPanier(sessionId);
            
            // Vérifier si le même produit existe déjà (même type, même ID, sans compléments)
            var existingItem = panier.FirstOrDefault(p => 
                p.ProduitId == item.ProduitId && 
                p.Type == item.Type && 
                p.Complements.Count == 0);

            if (existingItem != null)
            {
                // Augmenter la quantité
                existingItem.Quantite += item.Quantite;
            }
            else
            {
                // Ajouter un nouvel item
                panier.Add(item);
            }

            SavePanier(sessionId, panier);
        }

        public void AjouterComplement(string sessionId, int panierItemId, ComplementItem complement)
        {
            var panier = GetPanier(sessionId);
            var item = panier.FirstOrDefault(p => p.ProduitId == panierItemId);

            if (item != null)
            {
                var existingComplement = item.Complements.FirstOrDefault(c => c.Id == complement.Id);
                
                if (existingComplement != null)
                {
                    existingComplement.Quantite += complement.Quantite;
                }
                else
                {
                    item.Complements.Add(complement);
                }

                SavePanier(sessionId, panier);
            }
        }

        public void SupprimerDuPanier(string sessionId, int panierItemId)
        {
            var panier = GetPanier(sessionId);
            var item = panier.FirstOrDefault(p => p.ProduitId == panierItemId);

            if (item != null)
            {
                panier.Remove(item);
                SavePanier(sessionId, panier);
            }
        }

        public void ViderPanier(string sessionId)
        {
            var session = _httpContextAccessor.HttpContext?.Session;
            session?.Remove(GetPanierKey(sessionId));
        }

        public void UpdateQuantite(string sessionId, int panierItemId, int quantite)
        {
            var panier = GetPanier(sessionId);
            var item = panier.FirstOrDefault(p => p.ProduitId == panierItemId);

            if (item != null)
            {
                if (quantite <= 0)
                {
                    panier.Remove(item);
                }
                else
                {
                    item.Quantite = quantite;
                }

                SavePanier(sessionId, panier);
            }
        }

        public decimal CalculerTotal(string sessionId)
        {
            var panier = GetPanier(sessionId);
            return panier.Sum(item => item.SousTotal);
        }

        public int GetNombreArticles(string sessionId)
        {
            var panier = GetPanier(sessionId);
            return panier.Sum(item => item.Quantite);
        }
    }
}