using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    public enum TypePaiement
    {
        OM,
        Wave
    }

    [Table("paiements")]
    public class Paiement
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Column("date")]
        public DateTime Date { get; set; } = DateTime.UtcNow;

        [Required]
        [Column("montant")]
        public decimal Montant { get; set; }

        [Required]
        [Column("type_paiement")]
        public string TypePaiement { get; set; } = string.Empty;

        [Required]
        [Column("commande_id")]
        public int CommandeId { get; set; }

        // Navigation property
        [ForeignKey("CommandeId")]
        public Commande? Commande { get; set; }
    }
}