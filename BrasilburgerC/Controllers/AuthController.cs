using Microsoft.AspNetCore.Mvc;
using BrasilburgerC.Services;

namespace BrasilburgerC.Controllers
{
    public class AuthController : Controller
    {
        private readonly IAuthService _authService;

        public AuthController(IAuthService authService)
        {
            _authService = authService;
        }

        // GET: Auth/Login
        [HttpGet]
        public IActionResult Login()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId != null)
            {
                return RedirectToAction("Index", "Catalogue");
            }

            return View();
        }

        // POST: Auth/Login
        [HttpPost]
        public IActionResult Login(string telephone, string password)
        {
            if (string.IsNullOrWhiteSpace(telephone) || string.IsNullOrWhiteSpace(password))
            {
                ViewBag.ErrorMessage = "Veuillez remplir tous les champs";
                return View();
            }

            var user = _authService.Login(telephone, password);

            if (user == null)
            {
                ViewBag.ErrorMessage = "Téléphone ou mot de passe incorrect";
                return View();
            }

            if (user.TypeUser != "Client")
            {
                ViewBag.ErrorMessage = "Accès réservé aux clients uniquement";
                return View();
            }
            HttpContext.Session.SetInt32("UserId", user.Id);
            HttpContext.Session.SetString("UserNom", user.Nom);
            HttpContext.Session.SetString("UserPrenom", user.Prenom);
            HttpContext.Session.SetString("UserTelephone", user.Telephone);

            return RedirectToAction("Index", "Catalogue");
        }

        // GET: Auth/Register
        [HttpGet]
        public IActionResult Register()
        {
            var userId = HttpContext.Session.GetInt32("UserId");
            if (userId != null)
            {
                return RedirectToAction("Index", "Catalogue");
            }

            return View();
        }

        // POST: Auth/Register
        [HttpPost]
        public IActionResult Register(string nom, string prenom, string telephone, string password, string confirmPassword)
        {
            if (string.IsNullOrWhiteSpace(nom) || string.IsNullOrWhiteSpace(prenom) || 
                string.IsNullOrWhiteSpace(telephone) || string.IsNullOrWhiteSpace(password))
            {
                ViewBag.ErrorMessage = "Veuillez remplir tous les champs";
                return View();
            }

            if (password != confirmPassword)
            {
                ViewBag.ErrorMessage = "Les mots de passe ne correspondent pas";
                return View();
            }

            if (_authService.TelephoneExists(telephone))
            {
                ViewBag.ErrorMessage = "Ce numéro de téléphone est déjà utilisé";
                return View();
            }

            var user = _authService.Register(nom, prenom, telephone, password);

            if (user == null)
            {
                ViewBag.ErrorMessage = "Erreur lors de la création du compte";
                return View();
            }

            HttpContext.Session.SetInt32("UserId", user.Id);
            HttpContext.Session.SetString("UserNom", user.Nom);
            HttpContext.Session.SetString("UserPrenom", user.Prenom);
            HttpContext.Session.SetString("UserTelephone", user.Telephone);

            return RedirectToAction("Index", "Catalogue");
        }

        // POST: Auth/Logout
        [HttpPost]
        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Login", "Auth");
        }
    }
}