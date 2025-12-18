FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app
EXPOSE 80

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copie le .csproj depuis le sous-dossier
COPY ["src/BrasilaburgerC.csproj", "./"]

RUN dotnet restore "BrasilaburgerC.csproj"

# Copie tout le reste
COPY . .

# Build et publish
WORKDIR "/src"
RUN dotnet build "BrasilaburgerC.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "BrasilaburgerC.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "BrasilaburgerC.dll"]