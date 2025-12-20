using BrasilburgerC.Data;
using BrasilburgerC.Models;

namespace BrasilburgerC.Services.Impl
{
    public class PaiementServiceImpl : IPaiementService
    {
        private readonly BrasilburgerCDbContext _context;

        public PaiementServiceImpl(BrasilburgerCDbContext context)
        {
            _context = context;
        }

        public Paiement? CreerPaiement(int commandeId, decimal montant, string typePaiement)
        {
            var paiement = new Paiement
            {
                CommandeId = commandeId,
                Montant = montant,
                TypePaiement = typePaiement,
                Date = DateTime.UtcNow
            };

            _context.Paiements.Add(paiement);
            _context.SaveChanges();

            return paiement;
        }

        public Paiement? GetPaiementByCommandeId(int commandeId)
        {
            return _context.Paiements
                .FirstOrDefault(p => p.CommandeId == commandeId);
        }
    }
}