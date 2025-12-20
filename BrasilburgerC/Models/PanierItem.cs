namespace BrasilburgerC.Models
{
    public class PanierItem
    {
        public string PanierItemId { get; set; } = Guid.NewGuid().ToString(); // ID unique pour le panier
        public int ProduitId { get; set; } // ID du produit (burger, menu, complement)
        public string Type { get; set; } = string.Empty; // "Burger", "Menu", "Complement"
        public string Nom { get; set; } = string.Empty;
        public decimal Prix { get; set; }
        public string? Image { get; set; }
        public int Quantite { get; set; } = 1;
        public List<ComplementItem> Complements { get; set; } = new List<ComplementItem>();
        
        public decimal SousTotal
        {
            get
            {
                decimal totalComplements = Complements.Sum(c => c.Prix * c.Quantite);
                return (Prix + totalComplements) * Quantite;
            }
        }
    }

    public class ComplementItem
    {
        public int Id { get; set; }
        public string Nom { get; set; } = string.Empty;
        public decimal Prix { get; set; }
        public int Quantite { get; set; } = 1;
    }
}