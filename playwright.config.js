const { defineConfig, devices } = require("@playwright/test");

module.exports = defineConfig({
  testDir: "./src/test/java/e2e",
  fullyParallel: true,
  retries: process.env.CI ? 2 : 0,
  reporter: process.env.CI
    ? [["list"], ["html", { outputFolder: "playwright-report", open: "never" }]]
    : "list",
  use: {
    baseURL: "http://127.0.0.1:5501/frontend/",
    locale: "en-US",
    extraHTTPHeaders: {
      "Accept-Language": "en-US,en;q=0.9"
    },
    trace: "on-first-retry"
  },
  projects: [
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
        launchOptions: {
          args: [
            "--disable-features=Translate,TranslateUI",
            "--disable-translate"
          ]
        }
      }
    }
  ],
  webServer: {
    command: "npx http-server . -p 5501 -c-1 --silent",
    url: "http://127.0.0.1:5501/frontend/",
    reuseExistingServer: !process.env.CI
  }
});
