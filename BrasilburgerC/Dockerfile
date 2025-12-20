# Étape 1: Build
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# Copie du fichier .csproj (chemin correct)
COPY BrasilburgerC.csproj ./
RUN dotnet restore "BrasilburgerC.csproj"

# Copie du reste
COPY . .

# Build
RUN dotnet build "BrasilburgerC.csproj" -c Release -o /app/build

# Étape 2: Publication
FROM build AS publish
RUN dotnet publish "BrasilburgerC.csproj" -c Release -o /app/publish /p:UseAppHost=false

# Étape 3: Runtime
FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS final
WORKDIR /app
EXPOSE 8080
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "BrasilburgerC.dll"]