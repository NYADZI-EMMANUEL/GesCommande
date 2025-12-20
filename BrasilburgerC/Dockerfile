FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src
COPY ["BrasilburgerC.csproj", "."]
RUN dotnet restore "BrasilburgerC.csproj"
COPY . .
RUN dotnet build "BrasilburgerC.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "BrasilburgerC.csproj" -c Release -o /app/publish

FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS final
WORKDIR /app
EXPOSE 8080
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "BrasilburgerC.dll"]