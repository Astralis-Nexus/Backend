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
