using BrasilburgerC.Data;
using BrasilburgerC.Models;
using Microsoft.EntityFrameworkCore;

namespace BrasilburgerC.Services.Impl
{
    public class AuthServiceImpl : IAuthService
    {
        private readonly BrasilburgerCDbContext _context;

        public AuthServiceImpl(BrasilburgerCDbContext context)
        {
            _context = context;
        }

        public User? Login(string telephone, string password)
        {
            var user = _context.Users
                .FirstOrDefault(u => u.Telephone == telephone && u.Password == password);

            return user;
        }

        public User? Register(string nom, string prenom, string telephone, string password)
        {
            if (TelephoneExists(telephone))
            {
                return null;
            }

            var user = new User
            {
                Nom = nom,
                Prenom = prenom,
                Telephone = telephone,
                Password = password,
                TypeUser = "Client",
                CreatedAt = DateTime.UtcNow
            };

            _context.Users.Add(user);
            _context.SaveChanges();
            var client = new Client
            {
                Id = user.Id
            };

            _context.Clients.Add(client);
            _context.SaveChanges();

            return user;
        }

        public Client? GetClientByUserId(int userId)
        {
            return _context.Clients
                .Include(c => c.User)
                .FirstOrDefault(c => c.Id == userId);
        }

        public bool TelephoneExists(string telephone)
        {
            return _context.Users.Any(u => u.Telephone == telephone);
        }
    }
}