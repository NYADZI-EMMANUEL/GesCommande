# Étape 1 : Build
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# Copier le .csproj et restaurer les dépendances
COPY *.csproj ./
RUN dotnet restore

# Copier tout et build
COPY . .
RUN dotnet publish -c Release -o /app/publish

# Étape 2 : Runtime
FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS runtime
WORKDIR /app

# Variables pour Render
ENV ASPNETCORE_ENVIRONMENT=Production
ENV ASPNETCORE_URLS=http://+:8080

# Copier les fichiers publiés
COPY --from=build /app/publish .

# Port exposé (Render utilisera $PORT)
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["dotnet", "BrasilburgerC.dll"]