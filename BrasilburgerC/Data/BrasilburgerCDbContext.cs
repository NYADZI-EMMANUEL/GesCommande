using Microsoft.EntityFrameworkCore;
using BrasilburgerC.Models;



namespace BrasilburgerC.Data
{
    public class BrasilburgerCDbContext : DbContext
    {
        public BrasilburgerCDbContext(DbContextOptions<BrasilburgerCDbContext> options)
            : base(options)
        {
        }

        // DbSets pour toutes les tables
        public DbSet<User> Users { get; set; }
        public DbSet<Client> Clients { get; set; }
        public DbSet<Burger> Burgers { get; set; }
        public DbSet<Complement> Complements { get; set; }
        public DbSet<Menu> Menus { get; set; }
        public DbSet<Zone> Zones { get; set; }
        public DbSet<Commande> Commandes { get; set; }
        public DbSet<CommandeProduit> CommandeProduits { get; set; }
        public DbSet<Paiement> Paiements { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Configuration pour PostgreSQL ENUM types
            modelBuilder.HasPostgresEnum<TypeComplement>();
            modelBuilder.HasPostgresEnum<ModeCommande>();
            modelBuilder.HasPostgresEnum<StatutCommande>();
            modelBuilder.HasPostgresEnum<TypePaiement>();

            // Configuration User
            modelBuilder.Entity<User>(entity =>
            {
                entity.HasIndex(e => e.Telephone).IsUnique();
                entity.HasIndex(e => e.TypeUser);
            });

            // Configuration Client (héritage de User)
            modelBuilder.Entity<Client>(entity =>
            {
                entity.HasOne(c => c.User)
                    .WithOne()
                    .HasForeignKey<Client>(c => c.Id)
                    .OnDelete(DeleteBehavior.Cascade);
            });

            // Configuration Burger
            modelBuilder.Entity<Burger>(entity =>
            {
                entity.HasIndex(e => e.IsArchived);
                entity.HasIndex(e => e.Nom);
            });

            // Configuration Complement
            modelBuilder.Entity<Complement>(entity =>
            {
                entity.HasIndex(e => e.Type);
                entity.HasIndex(e => e.IsArchived);
            });

            // Configuration Menu
            modelBuilder.Entity<Menu>(entity =>
            {
                entity.HasIndex(e => e.Nom);
                entity.HasIndex(e => e.BurgerId);
                entity.HasIndex(e => e.BoissonId);
                entity.HasIndex(e => e.FriteId);

                entity.HasOne(m => m.Burger)
                    .WithMany(b => b.Menus)
                    .HasForeignKey(m => m.BurgerId)
                    .OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(m => m.Boisson)
                    .WithMany()
                    .HasForeignKey(m => m.BoissonId)
                    .OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(m => m.Frite)
                    .WithMany()
                    .HasForeignKey(m => m.FriteId)
                    .OnDelete(DeleteBehavior.Cascade);
            });

            // Configuration Zone
            modelBuilder.Entity<Zone>(entity =>
            {
                entity.HasIndex(e => e.Nom);
            });

            // Configuration Commande
            modelBuilder.Entity<Commande>(entity =>
            {
                entity.HasIndex(e => e.ClientId);
                entity.HasIndex(e => e.Statut);
                entity.HasIndex(e => e.DateCom);
                entity.HasIndex(e => e.LivreurId);
                entity.HasIndex(e => e.Mode);
                entity.HasIndex(e => e.ZoneId);

                entity.HasOne(c => c.Client)
                    .WithMany(cl => cl.Commandes)
                    .HasForeignKey(c => c.ClientId)
                    .OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(c => c.Zone)
                    .WithMany(z => z.Commandes)
                    .HasForeignKey(c => c.ZoneId)
                    .OnDelete(DeleteBehavior.SetNull);
            });

            // Configuration CommandeProduit
            modelBuilder.Entity<CommandeProduit>(entity =>
            {
                entity.HasIndex(e => e.CommandeId);
                entity.HasIndex(e => e.BurgerId);
                entity.HasIndex(e => e.MenuId);
                entity.HasIndex(e => e.ComplementId);

                entity.HasOne(cp => cp.Commande)
                    .WithMany(c => c.CommandeProduits)
                    .HasForeignKey(cp => cp.CommandeId)
                    .OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(cp => cp.Burger)
                    .WithMany(b => b.CommandeProduits)
                    .HasForeignKey(cp => cp.BurgerId)
                    .OnDelete(DeleteBehavior.Restrict);

                entity.HasOne(cp => cp.Menu)
                    .WithMany(m => m.CommandeProduits)
                    .HasForeignKey(cp => cp.MenuId)
                    .OnDelete(DeleteBehavior.Restrict);

                entity.HasOne(cp => cp.Complement)
                    .WithMany(c => c.CommandeProduits)
                    .HasForeignKey(cp => cp.ComplementId)
                    .OnDelete(DeleteBehavior.Restrict);
            });

            // Configuration Paiement
            modelBuilder.Entity<Paiement>(entity =>
            {
                entity.HasIndex(e => e.CommandeId).IsUnique();
                entity.HasIndex(e => e.Date);

                entity.HasOne(p => p.Commande)
                    .WithOne(c => c.Paiement)
                    .HasForeignKey<Paiement>(p => p.CommandeId)
                    .OnDelete(DeleteBehavior.Cascade);
            });
        }
    }
}