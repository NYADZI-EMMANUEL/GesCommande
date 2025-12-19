using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    [Table("users")]
    public class User
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("nom")]
        [MaxLength(100)]
        public string Nom { get; set; } = string.Empty;

        [Required]
        [Column("prenom")]
        [MaxLength(100)]
        public string Prenom { get; set; } = string.Empty;

        [Required]
        [Column("telephone")]
        [MaxLength(20)]
        public string Telephone { get; set; } = string.Empty;

        [Required]
        [Column("password")]
        [MaxLength(255)]
        public string Password { get; set; } = string.Empty;

        [Required]
        [Column("type_user")]
        [MaxLength(20)]
        public string TypeUser { get; set; } = string.Empty;

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}