# Backend

## Docker setup

Projektet har en Docker Compose opsaetning med:

- PostgreSQL
- pgAdmin

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

## Opret forbindelse til databasen i pgAdmin

1. Åbn pgAdmin: `http://localhost:5050`
2. Log ind med:

```text
Email: admin@admin.com
Password: admin
```

## Kør tests og generer JaCoCo rapport

```powershell
mvn "-Dtest=unit.**.*Test" test
mvn "-Dtest=integration.**.*Test" test
mvn "-Dtest=api.**.*Test" test
mvn jacoco:report
```

Rapporten findes herefter i:

```text
target/site/jacoco/index.html
```