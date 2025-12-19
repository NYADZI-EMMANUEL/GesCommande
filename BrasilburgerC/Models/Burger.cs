using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    [Table("burgers")]
    public class Burger
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("nom")]
        [MaxLength(100)]
        public string Nom { get; set; } = string.Empty;

        [Required]
        [Column("prix")]
        public decimal Prix { get; set; }

        [Column("image")]
        [MaxLength(500)]
        public string? Image { get; set; }

        [Column("is_archived")]
        public bool IsArchived { get; set; } = false;

        [Column("description")]
        public string? Description { get; set; }

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Column("gestionnaire_id")]
        public int? GestionnaireId { get; set; }

        // Collections
        public ICollection<Menu>? Menus { get; set; }
        public ICollection<CommandeProduit>? CommandeProduits { get; set; }
    }
}