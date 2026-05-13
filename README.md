# Backend

## Docker setup

Projektet har en Docker Compose opsaetning med:

- PostgreSQL
- pgAdmin

Alle brugernavne og passwords er kun til lokal udvikling.

Backend koeres lokalt fra IntelliJ og forbinder til PostgreSQL-containeren.

## Krav

Du skal have Docker Desktop installeret og startet.

Tjek at Docker virker:

```powershell
docker --version
docker compose version
```

## Start PostgreSQL og pgAdmin

Start databasen og pgAdmin:

```powershell
docker compose up -d
```

PostgreSQL kører paa:

```text
Host fra din computer: localhost
Host inde i Docker: postgres
Port: 5432
Database: astralis
Username: postgres
Password: postgres
```

pgAdmin kører paa:

```text
URL: http://localhost:5050
Email: admin@admin.com
Password: admin
```

## Opret forbindelse til databasen i pgAdmin

1. Aabn pgAdmin: `http://localhost:5050`
2. Log ind med:

```text
Email: admin@admin.com
Password: admin
```

3. Hoejreklik paa `Servers`
4. Vaelg `Register` -> `Server`
5. Under `General`:

```text
Name: Backend Postgres
```

6. Under `Connection`:

```text
Host name/address: postgres
Port: 5432
Maintenance database: astralis
Username: postgres
Password: postgres
```

7. Klik `Save`

## Stop Docker containers

Stop containers:

```powershell
docker compose down
```

Stop containers og slet database-data:

```powershell
docker compose down -v
```

Brug kun `-v`, hvis du vil slette databasen helt og starte forfra.

## Kør tests og generer JaCoCo rapport

Kør unit tests, integration tests og generer én samlet rapport:

```powershell
mvn "-Dtest=unit.**.*Test" test
mvn "-Dtest=integration.**.*Test" test
mvn jacoco:report
```

Rapporten findes herefter i:

```text
target/site/jacoco/index.html
```

## Start backend fra IntelliJ

Start `application.Application` fra IntelliJ.

Backend forbinder som default til:

```text
jdbc:postgresql://localhost:5432/astralis?currentSchema=public
Username: postgres
Password: postgres
```

Det passer med PostgreSQL-containeren, fordi port `5432` er exposed til din computer.

Backend API koerer derefter paa:

```text
http://localhost:7007
```
