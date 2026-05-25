import { test, expect } from '@playwright/test';

const API = 'http://localhost:7007/api';
const TEST_PASSWORD = 'testtest';
const ROCKET_LEAGUE_TODO = 'Afslut inaktive Rocket League';
const OLD_LICENSE_QUESTION = 'Hvordan lukker jeg en gammel licens?';
const OLD_LICENSE_ANSWER = 'Skift licensens status til INACTIVE og opret en opgave, hvis der skal foelges op.';

test('happy path: user can manage games, todos and Q&A', async ({ page, request }) => {
  const id = Date.now();
  const username = `player${id}`;
  const gameName = `Fifa 14 ${id}`;
  const todoDescription = `Add Milk ${id}`;
  const qaQuestion = `Milk ${id}`;
  const qaAnswer = `Milk answer ${id}`;

  await test.step('Given a new user is registered and required test data exists', async () => {
    await page.goto('');
    await page.getByRole('button', { name: 'Register' }).click();
    await page.getByRole('textbox', { name: 'Choose a username' }).fill(username);
    await page.getByRole('textbox', { name: 'Choose a password' }).fill(TEST_PASSWORD);
    await page.getByRole('button', { name: 'Create account' }).click();

    await expect(page.getByText(username)).toBeVisible();
    await expect(page.getByRole('button', { name: 'Sign out' })).toBeVisible();

    const userId = await page.evaluate(() => JSON.parse(localStorage.getItem('astralis_user')).id);

    const todosResponse = await request.get(`${API}/todos/`);
    const todos = todosResponse.ok() ? await todosResponse.json() : [];
    if (!todos.some(todo => todo.description === ROCKET_LEAGUE_TODO)) {
      await request.post(`${API}/todos/`, {
        data: {
          description: ROCKET_LEAGUE_TODO,
          status: 'COMPLETED',
          source: 'GAMEHUB',
          account: { id: userId },
        },
      });
    }

    const qasResponse = await request.get(`${API}/qas/`);
    const qas = qasResponse.ok() ? await qasResponse.json() : [];
    if (!qas.some(qa => qa.question === OLD_LICENSE_QUESTION)) {
      await request.post(`${API}/qas/`, {
        data: {
          question: OLD_LICENSE_QUESTION,
          answer: OLD_LICENSE_ANSWER,
          accountId: userId,
        },
      });
    }
  });

  await test.step('When the user creates and deletes a game', async () => {
    await page.getByRole('button', { name: '+ Add Game' }).click();
    await page.getByRole('textbox', { name: 'e.g. Cyberpunk' }).fill(gameName);
    await page.getByRole('button', { name: 'Create' }).click();

    const gameCard = page.locator('.game-card')
      .filter({ has: page.getByRole('heading', { name: gameName }) });
    await expect(gameCard).toBeVisible();
    await expect(gameCard.getByText(/^ID:/)).toBeVisible();

    await gameCard.getByRole('button', { name: '✕' }).click();
    await expect(page.getByRole('dialog')).toBeVisible();
    await page.getByRole('button', { name: 'Delete' }).click();

    await expect(gameCard).not.toBeVisible();
  });

  await test.step('When the user updates, creates and deletes todos', async () => {
    await page.getByRole('button', { name: '✓ Todos' }).click();
    await expect(page.getByRole('heading', { name: 'Todos' })).toBeVisible();

    await page.getByRole('button', { name: 'Pending' }).click();
    await expect(page.getByRole('button', { name: 'Pending' })).toHaveClass(/active/);
    await page.getByRole('button', { name: 'All' }).click();
    await expect(page.getByRole('button', { name: 'All' })).toHaveClass(/active/);

    const rocketLeagueRow = page.getByRole('row', { name: ROCKET_LEAGUE_TODO }).first();
    await expect(rocketLeagueRow).toBeVisible();
    await expect(rocketLeagueRow.getByText('GAMEHUB')).toBeVisible();

    await rocketLeagueRow.getByRole('combobox').selectOption('IN_PROGRESS');

    await expect(rocketLeagueRow.getByRole('combobox')).toHaveValue('IN_PROGRESS');
    await expect(rocketLeagueRow.locator('.badge.status-in-progress')).toHaveText('In Progress');

    await page.getByRole('button', { name: '+ Add Todo' }).click();
    await page.getByRole('textbox', { name: 'Describe the task…' }).fill(todoDescription);
    await page.getByRole('button', { name: 'Create' }).click();

    const todoRow = page.getByRole('row', { name: todoDescription });
    await expect(todoRow).toBeVisible();
    await expect(todoRow.getByRole('combobox')).toHaveValue('PENDING');

    await todoRow.getByRole('button', { name: '✕' }).click();
    await expect(page.getByRole('dialog')).toBeVisible();
    await page.getByRole('button', { name: 'Delete' }).click();

    await expect(todoRow).not.toBeVisible();
  });

  await test.step('When the user opens existing Q&A and adds a new entry', async () => {
    await page.getByRole('button', { name: '? Q&A' }).click();
    await expect(page.getByRole('heading', { name: 'Q&A' })).toBeVisible();

    const oldLicenseQuestion = page.locator('span.qa-q-text', { hasText: OLD_LICENSE_QUESTION }).first();
    await expect(oldLicenseQuestion).toBeVisible();
    await oldLicenseQuestion.click();

    await expect(page.getByText(OLD_LICENSE_ANSWER)).toBeVisible();

    await page.getByRole('button', { name: '+ Add Q&A' }).click();
    await page.getByRole('textbox', { name: 'Enter your question…' }).fill(qaQuestion);
    await page.getByRole('textbox', { name: 'Enter the answer…' }).fill(qaAnswer);
    await page.getByRole('button', { name: 'Add', exact: true }).click();

    await expect(page.locator('span.qa-q-text', { hasText: qaQuestion })).toBeVisible();
  });

  await test.step('Then the user can sign out', async () => {
    await page.getByRole('button', { name: 'Sign out' }).click();

    await expect(page.getByRole('button', { name: 'Sign in' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Register' })).toBeVisible();
  });
});
