using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    [Table("menus")]
    public class Menu
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("nom")]
        [MaxLength(100)]
        public string Nom { get; set; } = string.Empty;

        [Column("image")]
        [MaxLength(500)]
        public string? Image { get; set; }

        [Required]
        [Column("prix")]
        public decimal Prix { get; set; }

        [Required]
        [Column("burger_id")]
        public int BurgerId { get; set; }

        [Required]
        [Column("boisson_id")]
        public int BoissonId { get; set; }

        [Required]
        [Column("frite_id")]
        public int FriteId { get; set; }

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Column("gestionnaire_id")]
        public int? GestionnaireId { get; set; }

        // Navigation properties
        [ForeignKey("BurgerId")]
        public Burger? Burger { get; set; }

        [ForeignKey("BoissonId")]
        public Complement? Boisson { get; set; }

        [ForeignKey("FriteId")]
        public Complement? Frite { get; set; }

        // Collections
        public ICollection<CommandeProduit>? CommandeProduits { get; set; }
    }
}