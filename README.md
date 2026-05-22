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

## Kør JMeter performance tests

JMeter testplanerne ligger i:

```text
src/test/java/jmeter
```

Alle tre testplaner tester samme flow mod den lokale backend:

1. Opretter en unik testbruger med `POST /api/accounts/`
2. Logger ind med brugeren via `POST /api/security/login`

Testene forventer at backend kører på:

```text
http://localhost:7008
```

### Testtyper

Projektet har tre performance tests:

| Testtype | Fil | Formål | Opsætning |
| --- | --- | --- | --- |
| Load test | `src/test/java/jmeter/load/register-login-load.jmx` | Tester normal/forventet belastning. Bruges til at se om systemet kan håndtere en realistisk mængde brugere. | 10 users, 30 sek. ramp-up, 120 sek. duration |
| Stress test | `src/test/java/jmeter/stress/register-login-stress.jmx` | Presser systemet hårdere end normal belastning. Bruges til at finde grænser, fejl og langsomme svartider under høj belastning. | 50 users, 60 sek. ramp-up, 180 sek. duration |
| Spike test | `src/test/java/jmeter/spike/register-login-spike.jmx` | Sender mange brugere ind meget hurtigt. Bruges til at teste hvordan systemet reagerer på pludselige trafikspidser. | 100 users, 5 sek. ramp-up, 60 sek. duration |

### Vigtige JMeter-indstillinger

Disse felter findes i JMeters `Thread Group`, som styrer hvor mange brugere testen simulerer:

| Felt | Eksempel | Betydning |
| --- | --- | --- |
| Number of Threads (Users) | `10` | Antal samtidige virtuelle brugere JMeter simulerer. 10 betyder at op til 10 brugere kører flowet samtidigt. |
| Ramp-up Period (seconds) | `30` | Hvor lang tid JMeter bruger på at starte alle brugere. 10 users og 30 sek. ramp-up betyder cirka en ny bruger hvert 3. sekund. |
| Loop Count | `Forever` / `-1` | Hvor mange gange hver bruger gentager flowet. I vores tests kører loopet kontinuerligt, mens testens duration bestemmer hvornår testen stopper. |
| Duration (seconds) | `120` | Hvor længe testen kører i alt. Bruges sammen med scheduler i Thread Group. |
| Same user on next iteration | `true` | Den samme virtuelle JMeter-tråd fortsætter i næste iteration. I vores plan genereres der dog stadig unikke testdata via scriptet. |
| On sampler error | `continue` | Hvis et request fejler, fortsætter testen i stedet for at stoppe hele testkørslen. |

### JMeter-elementer i testplanerne

Testplanerne bruger disse JMeter-elementer:

| Element | Bruges til |
| --- | --- |
| Test Plan | Den overordnede container for hele testen. Her ligger variabler som `protocol`, `host` og `port`. |
| User Defined Variables | Definerer fælles værdier: `protocol=http`, `host=localhost`, `port=7008`. Det gør det nemt at ændre target for testen ét sted. |
| Thread Group | Styrer antal brugere, ramp-up, duration og loop. Det er her load/stress/spike adskiller sig mest. |
| Header Manager | Sender HTTP headers: `Content-Type: application/json` og `Accept: application/json`. |
| JSR223 Sampler | Kører et lille Groovy-script, som laver unikke testdata til hver bruger, fx unik `username` og fælles `password`. |
| HTTP Request: Register account | Sender `POST /api/accounts/` med JSON body: `username` og `password`. |
| Response Assertion: Register should return 201 | Tjekker at register-requestet svarer med HTTP status `201 Created`. |
| HTTP Request: Login account | Sender `POST /api/security/login` med samme `username` og `password`. |
| Response Assertion: Login should return 200 | Tjekker at login-requestet svarer med HTTP status `200 OK`. |
| Constant Timer | Bruges i stress testen som pacing. `250 ms` betyder en lille pause mellem iterationer, så testen ikke rammer API'et helt uden pause. |
| HTML Dashboard Report | Genereres af JMeter med `-e -o`. Rapporten viser bl.a. svartider, throughput, fejlprocent og grafer. |

### 1. Start PostgreSQL

```powershell
docker compose up -d
```

### 2. Start backend

Åbn en ny terminal i projektets rodmappe og kør:

```powershell
mvn package
java -jar target\Backend-1.0-SNAPSHOT-shaded.jar
```

Lad denne terminal blive stående åben, mens JMeter-testene kører.

### 3. Klargør rapportmappe

Åbn endnu en terminal i projektets rodmappe og kør:

```powershell
New-Item -ItemType Directory -Force -Path target\jmeter
```

### 4. Kør testene

På Windows kan `jmeter` være installeret uden at være på PATH. Hvis PowerShell siger `jmeter: The term 'jmeter' is not recognized`, skal du bruge den fulde sti til `jmeter.bat`.

På denne maskine ligger JMeter fx her:

```text
C:\Program Files\apache-jmeter-5.6.3\apache-jmeter-5.6.3\bin\jmeter.bat
```

Load test:

```powershell
& "C:\Program Files\apache-jmeter-5.6.3\apache-jmeter-5.6.3\bin\jmeter.bat" -n -t src\test\java\jmeter\load\register-login-load.jmx -l target\jmeter\register-login-load.jtl -e -o target\jmeter\register-login-load-report
```

Stress test:

```powershell
& "C:\Program Files\apache-jmeter-5.6.3\apache-jmeter-5.6.3\bin\jmeter.bat" -n -t src\test\java\jmeter\stress\register-login-stress.jmx -l target\jmeter\register-login-stress.jtl -e -o target\jmeter\register-login-stress-report
```

Spike test:

```powershell
& "C:\Program Files\apache-jmeter-5.6.3\apache-jmeter-5.6.3\bin\jmeter.bat" -n -t src\test\java\jmeter\spike\register-login-spike.jmx -l target\jmeter\register-login-spike.jtl -e -o target\jmeter\register-login-spike-report
```

JMeter overskriver ikke gamle rapportmapper. Hvis du får en fejl som `folder is not empty`, kan du enten bruge nye output-navne eller slette den gamle rapportmappe først.

Brug nye navne, fx:

```powershell
& "C:\Program Files\apache-jmeter-5.6.3\apache-jmeter-5.6.3\bin\jmeter.bat" -n -t src\test\java\jmeter\load\register-login-load.jmx -l target\jmeter\register-login-load-2.jtl -e -o target\jmeter\register-login-load-report-2
```

Eller slet den gamle load-rapport og `.jtl` fil, før du kører samme kommando igen:

```powershell
Remove-Item -Recurse -Force target\jmeter\register-login-load-report
Remove-Item -Force target\jmeter\register-login-load.jtl
```

### 5. Find og åbn rapporterne

Når en test er færdig, ligger HTML-rapporten i den report-mappe du gav til `-o`.

Load report:

```text
target/jmeter/register-login-load-report/index.html
```

Stress report:

```text
target/jmeter/register-login-stress-report/index.html
```

Spike report:

```text
target/jmeter/register-login-spike-report/index.html
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
2. **Seed-data indlæses automatisk** – Hibernate indlæser `src/main/resources/seed.sql` ved schema-oprettelse. Den fil indeholder bl.a. todoen *"Afslut inaktive Rocket League"* og Q&A-spørgsmålet *"Hvordan lukker jeg en gammel licens?"*. Testcontainers bruger samme seed-fil.

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
