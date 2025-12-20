using BrasilburgerC.Models;

namespace BrasilburgerC.Services
{
    public interface IAuthService
    {
        User? Login(string telephone, string password);
        User? Register(string nom, string prenom, string telephone, string password);
        Client? GetClientByUserId(int userId);
        bool TelephoneExists(string telephone);
    }
}