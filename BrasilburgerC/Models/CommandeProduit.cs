using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    [Table("commande_produits")]
    public class CommandeProduit
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("quantite")]
        public int Quantite { get; set; }

        [Required]
        [Column("sous_total")]
        public decimal SousTotal { get; set; }

        [Required]
        [Column("commande_id")]
        public int CommandeId { get; set; }

        [Column("burger_id")]
        public int? BurgerId { get; set; }

        [Column("menu_id")]
        public int? MenuId { get; set; }

        [Column("complement_id")]
        public int? ComplementId { get; set; }

        // Navigation properties
        [ForeignKey("CommandeId")]
        public Commande? Commande { get; set; }

        [ForeignKey("BurgerId")]
        public Burger? Burger { get; set; }

        [ForeignKey("MenuId")]
        public Menu? Menu { get; set; }

        [ForeignKey("ComplementId")]
        public Complement? Complement { get; set; }
    }
}