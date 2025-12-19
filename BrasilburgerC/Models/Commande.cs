using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    public enum ModeCommande
    {
        Sur_place,
        Emporter,
        Livraison
    }

    public enum StatutCommande
    {
        En_attente,
        En_preparation,
        Pret,
        Annuler,
        Terminer
    }

    [Table("commandes")]
    public class Commande
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Column("date_com")]
        public DateTime DateCom { get; set; } = DateTime.UtcNow;

        [Required]
        [Column("total_prix")]
        public decimal TotalPrix { get; set; }

        [Required]
        [Column("mode")]
        public string Mode { get; set; } = string.Empty;

        [Column("statut")]
        public string Statut { get; set; } = "En_attente";

        [Column("adresse_livraison")]
        public string? AdresseLivraison { get; set; }

        [Required]
        [Column("client_id")]
        public int ClientId { get; set; }

        [Column("zone_id")]
        public int? ZoneId { get; set; }

        [Column("livreur_id")]
        public int? LivreurId { get; set; }

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        // Navigation properties
        [ForeignKey("ClientId")]
        public Client? Client { get; set; }

        [ForeignKey("ZoneId")]
        public Zone? Zone { get; set; }

        // Collections
        public ICollection<CommandeProduit>? CommandeProduits { get; set; }
        public Paiement? Paiement { get; set; }
    }
}