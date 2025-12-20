using Microsoft.EntityFrameworkCore;
using BrasilburgerC.Data;
using BrasilburgerC.Services;
using BrasilburgerC.Services.Impl;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();

// Récupérer la ConnectionString
var connectionString = builder.Configuration.GetConnectionString("NeonConnection");

if (string.IsNullOrEmpty(connectionString))
{
    throw new InvalidOperationException("Connection string 'NeonConnection' not found.");
}

// Configuration PostgreSQL avec Npgsql (Neon)
builder.Services.AddDbContext<BrasilburgerCDbContext>(options =>
    options.UseNpgsql(connectionString));

// Configuration Session
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(60);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

builder.Services.AddHttpContextAccessor();

// Enregistrement des services avec leurs interfaces
builder.Services.AddScoped<IAuthService, AuthServiceImpl>();
builder.Services.AddScoped<ICatalogueService, CatalogueServiceImpl>();
builder.Services.AddScoped<ICatalogueService, CatalogueServiceImpl>();
builder.Services.AddScoped<IPanierService, PanierServiceImpl>();
builder.Services.AddScoped<ICommandeService, CommandeServiceImpl>();
builder.Services.AddScoped<IPaiementService, PaiementServiceImpl>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseSession();

app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Auth}/{action=Login}/{id?}");

var port = Environment.GetEnvironmentVariable("PORT") ?? "8080";
app.Run($"http://0.0.0.0:{port}");