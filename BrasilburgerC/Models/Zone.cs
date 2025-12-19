using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    [Table("zones")]
    public class Zone
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("nom")]
        [MaxLength(100)]
        public string Nom { get; set; } = string.Empty;

        [Required]
        [Column("quartiers")]
        public string Quartiers { get; set; } = string.Empty;

        [Required]
        [Column("prix")]
        public decimal Prix { get; set; }

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        // Collections
        public ICollection<Commande>? Commandes { get; set; }
    }
}