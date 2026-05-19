import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {

  await page.goto('');
  await page.getByRole('button', { name: 'Register' }).click();
  await page.getByRole('textbox', { name: 'Choose a username' }).click();
  await page.getByRole('textbox', { name: 'Choose a username' }).dblclick();
  await page.getByRole('textbox', { name: 'Choose a username' }).fill('test1');
  await page.getByRole('textbox', { name: 'Choose a password' }).dblclick();
  await page.getByRole('textbox', { name: 'Choose a password' }).fill('testtest');
  await page.getByRole('button', { name: 'Create account' }).click();

  
  await page.getByRole('button', { name: '+ Add Game' }).click();
  await page.getByRole('textbox', { name: 'e.g. Cyberpunk' }).fill('Fifa 27');
  await page.getByRole('button', { name: 'Create' }).click();
  page.once('dialog', dialog => {
    console.log(`Dialog message: ${dialog.message()}`);
    dialog.dismiss().catch(() => {});
  });
  await page.getByRole('button', { name: '✕' }).nth(4).click();


  await page.getByRole('button', { name: '✓ Todos' }).click();
  await page.getByRole('button', { name: 'Pending' }).click();
  await page.getByRole('button', { name: 'All' }).click();
  await page.getByRole('row', { name: 'Afslut inaktive Rocket League' }).getByRole('combobox').selectOption('IN_PROGRESS');


  await page.getByRole('button', { name: '+ Add Todo' }).click();
  await page.getByRole('textbox', { name: 'Describe the task…' }).fill('Add Milk');
  await page.getByRole('button', { name: 'Create' }).click();
  await page.getByRole('button', { name: '? Q&A' }).click();
  await page.locator('#qa-icon-1').click();
  await page.locator('#qa-icon-1').click();


  await page.getByRole('button', { name: '+ Add Q&A' }).click();
  await page.getByRole('textbox', { name: 'Enter your question…' }).fill('Milk');
  await page.getByRole('textbox', { name: 'Enter your question…' }).press('Tab');
  await page.getByRole('textbox', { name: 'Enter the answer…' }).fill('Milk');
  await page.getByRole('button', { name: 'Add', exact: true }).click();

  
  await page.getByRole('button', { name: 'Sign out' }).click();
});