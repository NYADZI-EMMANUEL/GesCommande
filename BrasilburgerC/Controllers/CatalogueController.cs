using Microsoft.AspNetCore.Mvc;
using BrasilburgerC.Services;

namespace BrasilburgerC.Controllers
{
    public class CatalogueController : Controller
    {
        private readonly ICatalogueService _catalogueService;

        public CatalogueController(ICatalogueService catalogueService)
        {
            _catalogueService = catalogueService;
        }

        // GET: Catalogue/Index
        public IActionResult Index(string? filter)
        {
            List<object> products;

            if (string.IsNullOrEmpty(filter))
            {
                products = _catalogueService.GetAllProducts();
            }
            else
            {
                products = _catalogueService.FilterProducts(filter);
            }

            ViewBag.CurrentFilter = filter ?? "tous";
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View(products);
        }

        // GET: Catalogue/Details?type=burger&id=1
        public IActionResult Details(string type, int id)
        {
            if (string.IsNullOrEmpty(type))
            {
                return RedirectToAction("Index");
            }

            var product = _catalogueService.GetProductDetails(type, id);

            if (product == null)
            {
                return NotFound();
            }

            ViewBag.ProductType = type;
            ViewBag.UserNom = HttpContext.Session.GetString("UserNom");
            ViewBag.UserPrenom = HttpContext.Session.GetString("UserPrenom");

            return View(product);
        }
    }
}