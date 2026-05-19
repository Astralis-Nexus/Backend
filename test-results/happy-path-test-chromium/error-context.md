# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: happy-path.spec.js >> test
- Location: src/test/java/e2e/happy-path.spec.js:3:5

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: getByRole('button', { name: 'Sign out' })
Expected: visible
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for getByRole('button', { name: 'Sign out' })

```

```yaml
- text: ✦
- heading "Astralis" [level=1]
- paragraph: Quality Assurance Platform
- button "Sign in"
- button "Register"
- text: Username
- textbox "Choose a username": player
- text: Password
- textbox "Choose a password": testtest
- button "Create account"
- paragraph: "19-05-2026 12:32:15: Error persisting Account(salt=$2a$10$w3tleVsgJltPAW/88JpWnO, id=null, username=player, password=testtest, role=Role{name='REGULAR'})."
```

# Test source

```ts
  1  | import { test, expect } from '@playwright/test';
  2  | 
  3  | test('test', async ({ page }) => {
  4  | 
  5  |   // Go to baseURL
  6  |   await page.goto('');
  7  | 
  8  |   // Register a new user
  9  |   await page.getByRole('button', { name: 'Register' }).click();
  10 |   await page.getByRole('textbox', { name: 'Choose a username' }).click();
  11 |   await page.getByRole('textbox', { name: 'Choose a username' }).dblclick();
  12 |   await page.getByRole('textbox', { name: 'Choose a username' }).fill('player');
  13 |   await page.getByRole('textbox', { name: 'Choose a password' }).dblclick();
  14 |   await page.getByRole('textbox', { name: 'Choose a password' }).fill('testtest');
  15 |   await page.getByRole('button', { name: 'Create account' }).click();
  16 | 
  17 |   // Assert user is logged in
  18 |   await expect(page.getByText('player')).toBeVisible(); // username bottom left
> 19 |   await expect(page.getByRole('button', { name: 'Sign out' })).toBeVisible();
     |                                                                ^ Error: expect(locator).toBeVisible() failed
  20 | 
  21 |   // Game page
  22 |   await page.getByRole('button', { name: '+ Add Game' }).click();
  23 |   await page.getByRole('textbox', { name: 'e.g. Cyberpunk' }).fill('Fifa 14');
  24 |   await page.getByRole('button', { name: 'Create' }).click(); // create new game
  25 | 
  26 |   // Assert game is visible
  27 |   await expect(page.getByRole('heading', { name: 'Fifa 14' })).toBeVisible();
  28 | 
  29 |   await page.getByRole('button', { name: '✕' }).nth(4).click();
  30 |   await page.getByRole('button', { name: 'Delete' }).click(); // delete game
  31 |   //delay 1 second to allow for deletion to complete before assertion
  32 |   await page.waitForTimeout(1000);
  33 |   // Assert game is deleted
  34 |   await expect(page.getByRole('heading', { name: 'Fifa 14' })).not.toBeVisible();
  35 | 
  36 | 
  37 |   // Todo page
  38 |   await page.getByRole('button', { name: '✓ Todos' }).click();
  39 |   await page.waitForLoadState('networkidle'); // wait for todos to load
  40 |   await page.getByRole('button', { name: 'Pending' }).click();
  41 |   await page.getByRole('button', { name: 'All' }).click();
  42 |   await page.getByRole('row', { name: 'Afslut inaktive Rocket League' }).getByRole('combobox').selectOption('IN_PROGRESS'); // Change status of todo
  43 |   
  44 |   // Assert status changed
  45 |   await expect(page.getByRole('row', { name: 'Afslut inaktive Rocket League' }).getByRole('combobox')).toHaveValue('IN_PROGRESS');
  46 | 
  47 |   await page.getByRole('button', { name: '✕' }).nth(1).click();
  48 |   await page.getByRole('button', { name: 'Delete' }).click();
  49 | 
  50 |   // Assert todo was deleted
  51 |   await expect(page.getByRole('row', { name: 'Afslut inaktive Rocket League' })).not.toBeVisible();
  52 | 
  53 |   await page.getByRole('button', { name: '+ Add Todo' }).click();
  54 |   await page.getByRole('textbox', { name: 'Describe the task…' }).fill('Add Milk');
  55 |   await page.getByRole('button', { name: 'Create' }).click();
  56 | 
  57 |   // Assert new todo was added
  58 |   await expect(page.getByRole('row', { name: 'Add Milk' }).first()).toBeVisible();
  59 | 
  60 |   // Q&A page
  61 |   await page.getByRole('button', { name: '? Q&A' }).click();
  62 |   await page.getByText('Hvordan lukker jeg en gammel').click();
  63 |   await page.getByText('Hvordan lukker jeg en gammel licens? ✕ ▾').click();
  64 |   await page.getByRole('button', { name: '+ Add Q&A' }).click();
  65 |   await page.getByRole('textbox', { name: 'Enter your question…' }).fill('Milk');
  66 |   await page.getByRole('textbox', { name: 'Enter your question…' }).press('Tab');
  67 |   await page.getByRole('textbox', { name: 'Enter the answer…' }).fill('Milk');
  68 |   await page.getByRole('button', { name: 'Add', exact: true }).click();
  69 | 
  70 |   // Assert Q&A was added
  71 |   await expect(page.locator('span.qa-q-text', { hasText: 'Milk' })).toBeVisible(); // check question
  72 |   
  73 |   // Sign out
  74 |   await page.getByRole('button', { name: 'Sign out' }).click();
  75 | });
  76 | 
```