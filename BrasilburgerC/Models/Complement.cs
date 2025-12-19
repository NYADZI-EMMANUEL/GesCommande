using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    public enum TypeComplement
    {
        Boisson,
        Frites
    }

    [Table("complements")]
    public class Complement
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

        [Column("is_archived")]
        public bool IsArchived { get; set; } = false;

        [Column("description")]
        public string? Description { get; set; }

        [Column("quantite")]
        [MaxLength(50)]
        public string? Quantite { get; set; }

        [Required]
        [Column("prix")]
        public decimal Prix { get; set; }

        [Required]
        [Column("type")]
        public string Type { get; set; } = string.Empty;

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Column("gestionnaire_id")]
        public int? GestionnaireId { get; set; }

        // Collections
        public ICollection<CommandeProduit>? CommandeProduits { get; set; }
    }
}