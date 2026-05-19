import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {

  // Go to baseURL
  await page.goto('');

  // Register a new user
  await page.getByRole('button', { name: 'Register' }).click();
  await page.getByRole('textbox', { name: 'Choose a username' }).click();
  await page.getByRole('textbox', { name: 'Choose a username' }).dblclick();
  await page.getByRole('textbox', { name: 'Choose a username' }).fill('player');
  await page.getByRole('textbox', { name: 'Choose a password' }).dblclick();
  await page.getByRole('textbox', { name: 'Choose a password' }).fill('testtest');
  await page.getByRole('button', { name: 'Create account' }).click();

  // Assert user is logged in
  await expect(page.getByText('player')).toBeVisible(); // username bottom left
  await expect(page.getByRole('button', { name: 'Sign out' })).toBeVisible();

  // Game page
  await page.getByRole('button', { name: '+ Add Game' }).click();
  await page.getByRole('textbox', { name: 'e.g. Cyberpunk' }).fill('Fifa 14');
  await page.getByRole('button', { name: 'Create' }).click(); // create new game

  // Assert game is visible
  await expect(page.getByRole('heading', { name: 'Fifa 14' })).toBeVisible();

  await page.getByRole('button', { name: '✕' }).nth(4).click();
  await page.getByRole('button', { name: 'Delete' }).click(); // delete game
  //delay 1 second to allow for deletion to complete before assertion
  await page.waitForTimeout(1000);
  // Assert game is deleted
  await expect(page.getByRole('heading', { name: 'Fifa 14' })).not.toBeVisible();


  // Todo page
  await page.getByRole('button', { name: '✓ Todos' }).click();
  await page.waitForLoadState('networkidle'); // wait for todos to load
  await page.getByRole('button', { name: 'Pending' }).click();
  await page.getByRole('button', { name: 'All' }).click();
  await page.getByRole('row', { name: 'Afslut inaktive Rocket League' }).getByRole('combobox').selectOption('IN_PROGRESS'); // Change status of todo
  
  // Assert status changed
  await expect(page.getByRole('row', { name: 'Afslut inaktive Rocket League' }).getByRole('combobox')).toHaveValue('IN_PROGRESS');

  await page.getByRole('button', { name: '✕' }).nth(1).click();
  await page.getByRole('button', { name: 'Delete' }).click();

  // Assert todo was deleted
  await expect(page.getByRole('row', { name: 'Afslut inaktive Rocket League' })).not.toBeVisible();

  await page.getByRole('button', { name: '+ Add Todo' }).click();
  await page.getByRole('textbox', { name: 'Describe the task…' }).fill('Add Milk');
  await page.getByRole('button', { name: 'Create' }).click();

  // Assert new todo was added
  await expect(page.getByRole('row', { name: 'Add Milk' }).first()).toBeVisible();

  // Q&A page
  await page.getByRole('button', { name: '? Q&A' }).click();
  await page.getByText('Hvordan lukker jeg en gammel').click();
  await page.getByText('Hvordan lukker jeg en gammel licens? ✕ ▾').click();
  await page.getByRole('button', { name: '+ Add Q&A' }).click();
  await page.getByRole('textbox', { name: 'Enter your question…' }).fill('Milk');
  await page.getByRole('textbox', { name: 'Enter your question…' }).press('Tab');
  await page.getByRole('textbox', { name: 'Enter the answer…' }).fill('Milk');
  await page.getByRole('button', { name: 'Add', exact: true }).click();

  // Assert Q&A was added
  await expect(page.locator('span.qa-q-text', { hasText: 'Milk' })).toBeVisible(); // check question
  
  // Sign out
  await page.getByRole('button', { name: 'Sign out' }).click();
});
