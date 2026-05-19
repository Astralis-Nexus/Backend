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

---

# End-to-End Tests (Playwright)


## Installation

Installer Playwright og dens afhængigheder fra projektets rodmappe:

```powershell
npm install
npx playwright install chromium
```

## Konfiguration

Playwright er konfigureret i `playwright.config.js` i projektets rod. Konfigurationen ser sådan ud:

```js
const { defineConfig, devices } = require("@playwright/test");

module.exports = defineConfig({
  testDir: "./src/test/java/e2e",
  fullyParallel: true,
  retries: process.env.CI ? 2 : 0,
  use: {
    baseURL: "http://127.0.0.1:5501/frontend/",
    trace: "on-first-retry"
  },
  projects: [
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] }
    }
  ],
  webServer: {
    command: "npx http-server . -p 5501 -c-1 --silent",
    url: "http://127.0.0.1:5501/frontend/",
    reuseExistingServer: !process.env.CI
  }
});
```

Playwright starter automatisk en lokal HTTP-server på port `5501` og serverer frontend-filerne derfra. Backenden skal køre separat (se Docker-opsætningen ovenfor).

## Forudsætninger før testen køres

Før testen køres, skal følgende være opfyldt:

1. **Docker-backend kører** – start PostgreSQL og backend som beskrevet i Backend-afsnittet ovenfor.
2. **Testdata eksisterer i databasen** – testen forventer at der allerede er en todo med titlen *"Afslut inaktive Rocket League"* og et Q&A-spørgsmål *"Hvordan lukker jeg en gammel licens?"* i databasen. Sørg for at disse poster er til stede, f.eks. via pgAdmin eller en seed-script.

## Kør E2E-testen

```powershell
npx playwright test
```

For at se testen køre i en synlig browser:

```powershell
npx playwright test --headed
```

For at åbne Playwright Test UI:

```powershell
npx playwright test --ui
```

## Hvad testen dækker (happy path)

1. Registrerer en ny bruger og verificerer login
2. Opretter et spil og verificerer at det vises
3. Sletter spillet og verificerer at det forsvinder
4. Navigerer til Todo-siden og ændrer status på en eksisterende todo
5. Sletter en todo og opretter en ny
6. Navigerer til Q&A-siden og tilføjer et nyt spørgsmål/svar
7. Logger ud