using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilburgerC.Models
{
    [Table("clients")]
    public class Client
    {
        [Key]
        [Column("id")]
        [ForeignKey("User")]
        public int Id { get; set; }

        // Navigation property
        public User? User { get; set; }

        // Collection pour les commandes
        public ICollection<Commande>? Commandes { get; set; }
    }
}