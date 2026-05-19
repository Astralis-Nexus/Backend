# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: happy-path.spec.js >> test
- Location: src/test/java/e2e/happy-path.spec.js:3:5

# Error details

```
Test timeout of 30000ms exceeded.
```

```
Error: page.waitForResponse: Test timeout of 30000ms exceeded.
```

# Page snapshot

```yaml
- generic [ref=e3]:
  - complementary [ref=e4]:
    - generic [ref=e5]:
      - generic [ref=e6]: ✦
      - generic [ref=e7]: Astralis
    - navigation [ref=e8]:
      - button "🎮 Games" [ref=e9] [cursor=pointer]:
        - generic [ref=e10]: 🎮
        - text: Games
      - button "✓ Todos" [ref=e11] [cursor=pointer]:
        - generic [ref=e12]: ✓
        - text: Todos
      - button "? Q&A" [ref=e13] [cursor=pointer]:
        - generic [ref=e14]: "?"
        - text: Q&A
    - generic [ref=e15]:
      - generic [ref=e16]:
        - generic [ref=e17]: T
        - generic [ref=e18]:
          - generic [ref=e19]: test
          - generic [ref=e20]: REGULAR
      - button "Sign out" [ref=e21] [cursor=pointer]
  - main [ref=e22]:
    - generic [ref=e23]:
      - generic [ref=e24]:
        - generic [ref=e25]:
          - heading "Todos" [level=2] [ref=e26]
          - paragraph [ref=e27]: 6 tasks
        - button "+ Add Todo" [ref=e28] [cursor=pointer]
      - generic [ref=e29]:
        - button "All 6" [ref=e30] [cursor=pointer]:
          - text: All
          - generic [ref=e31]: "6"
        - button "Pending 4" [ref=e32] [cursor=pointer]:
          - text: Pending
          - generic [ref=e33]: "4"
        - button "In Progress 1" [ref=e34] [cursor=pointer]:
          - text: In Progress
          - generic [ref=e35]: "1"
        - button "Completed 1" [ref=e36] [cursor=pointer]:
          - text: Completed
          - generic [ref=e37]: "1"
      - table [ref=e39]:
        - rowgroup [ref=e40]:
          - row "Description Status Source Date Change status" [ref=e41]:
            - columnheader "Description" [ref=e42]
            - columnheader "Status" [ref=e43]
            - columnheader "Source" [ref=e44]
            - columnheader "Date" [ref=e45]
            - columnheader "Change status" [ref=e46]
            - columnheader [ref=e47]
        - rowgroup [ref=e48]:
          - row "Gennemgaa FAQ-svar for licensprocessen Pending STORE 2026,5,22 Pending ✕" [ref=e49]:
            - cell "Gennemgaa FAQ-svar for licensprocessen" [ref=e50]
            - cell "Pending" [ref=e51]:
              - generic [ref=e52]: Pending
            - cell "STORE" [ref=e53]:
              - generic [ref=e54]: STORE
            - cell "2026,5,22" [ref=e55]
            - cell "Pending" [ref=e56]:
              - combobox [ref=e57] [cursor=pointer]:
                - option "Pending" [selected]
                - option "In Progress"
                - option "Completed"
            - cell "✕" [ref=e58]:
              - button "✕" [active] [ref=e59] [cursor=pointer]
          - row "Afslut inaktive Rocket League-adgange In Progress GAMEHUB 2026,5,18 In Progress ✕" [ref=e60]:
            - cell "Afslut inaktive Rocket League-adgange" [ref=e61]
            - cell "In Progress" [ref=e62]:
              - generic [ref=e63]: In Progress
            - cell "GAMEHUB" [ref=e64]:
              - generic [ref=e65]: GAMEHUB
            - cell "2026,5,18" [ref=e66]
            - cell "In Progress" [ref=e67]:
              - combobox [ref=e68] [cursor=pointer]:
                - option "Pending"
                - option "In Progress" [selected]
                - option "Completed"
            - cell "✕" [ref=e69]:
              - button "✕" [ref=e70] [cursor=pointer]
          - row "Add Milk Pending GAMEHUB 2026,5,19 Pending ✕" [ref=e71]:
            - cell "Add Milk" [ref=e72]
            - cell "Pending" [ref=e73]:
              - generic [ref=e74]: Pending
            - cell "GAMEHUB" [ref=e75]:
              - generic [ref=e76]: GAMEHUB
            - cell "2026,5,19" [ref=e77]
            - cell "Pending" [ref=e78]:
              - combobox [ref=e79] [cursor=pointer]:
                - option "Pending" [selected]
                - option "In Progress"
                - option "Completed"
            - cell "✕" [ref=e80]:
              - button "✕" [ref=e81] [cursor=pointer]
          - row "Add Milk Pending GAMEHUB 2026,5,19 Pending ✕" [ref=e82]:
            - cell "Add Milk" [ref=e83]
            - cell "Pending" [ref=e84]:
              - generic [ref=e85]: Pending
            - cell "GAMEHUB" [ref=e86]:
              - generic [ref=e87]: GAMEHUB
            - cell "2026,5,19" [ref=e88]
            - cell "Pending" [ref=e89]:
              - combobox [ref=e90] [cursor=pointer]:
                - option "Pending" [selected]
                - option "In Progress"
                - option "Completed"
            - cell "✕" [ref=e91]:
              - button "✕" [ref=e92] [cursor=pointer]
          - row "hh Pending GAMEHUB 2026,5,19 Pending ✕" [ref=e93]:
            - cell "hh" [ref=e94]
            - cell "Pending" [ref=e95]:
              - generic [ref=e96]: Pending
            - cell "GAMEHUB" [ref=e97]:
              - generic [ref=e98]: GAMEHUB
            - cell "2026,5,19" [ref=e99]
            - cell "Pending" [ref=e100]:
              - combobox [ref=e101] [cursor=pointer]:
                - option "Pending" [selected]
                - option "In Progress"
                - option "Completed"
            - cell "✕" [ref=e102]:
              - button "✕" [ref=e103] [cursor=pointer]
          - row "Kontroller aktive CS2-licenser mod teamlisten Completed GAMEHUB 2026,5,19 Completed ✕" [ref=e104]:
            - cell "Kontroller aktive CS2-licenser mod teamlisten" [ref=e105]
            - cell "Completed" [ref=e106]:
              - generic [ref=e107]: Completed
            - cell "GAMEHUB" [ref=e108]:
              - generic [ref=e109]: GAMEHUB
            - cell "2026,5,19" [ref=e110]
            - cell "Completed" [ref=e111]:
              - combobox [ref=e112] [cursor=pointer]:
                - option "Pending"
                - option "In Progress"
                - option "Completed" [selected]
            - cell "✕" [ref=e113]:
              - button "✕" [ref=e114] [cursor=pointer]
```

# Test source

```ts
  1  | import { test, expect } from '@playwright/test';
  2  | 
  3  | test('test', async ({ page }) => {
  4  | 
  5  |   await page.goto('');
  6  |   await page.getByRole('button', { name: 'Register' }).click();
  7  |   await page.getByRole('textbox', { name: 'Choose a username' }).click();
  8  |   await page.getByRole('textbox', { name: 'Choose a username' }).dblclick();
  9  |   await page.getByRole('textbox', { name: 'Choose a username' }).fill('test');
  10 |   await page.getByRole('textbox', { name: 'Choose a password' }).dblclick();
  11 |   await page.getByRole('textbox', { name: 'Choose a password' }).fill('testtest');
  12 |   await page.getByRole('button', { name: 'Create account' }).click();
  13 | 
  14 |   
  15 |   await page.getByRole('button', { name: '+ Add Game' }).click();
  16 |   await page.getByRole('textbox', { name: 'e.g. Cyberpunk' }).fill('Fifa 28');
  17 |   await page.getByRole('button', { name: 'Create' }).click();
  18 |   page.once('dialog', dialog => {
  19 |     console.log(`Dialog message: ${dialog.message()}`);
  20 |     dialog.dismiss().catch(() => {});
  21 |   });
  22 |   await page.getByRole('button', { name: '✕' }).nth(4).click();
  23 | 
  24 | 
  25 |   await page.getByRole('button', { name: '✓ Todos' }).click();
  26 |   await page.getByRole('button', { name: 'Pending' }).click();
  27 |   await page.getByRole('button', { name: 'All' }).click();
  28 |   await page.getByRole('row', { name: 'Afslut inaktive Rocket League' }).getByRole('combobox').selectOption('IN_PROGRESS');
  29 | 
  30 | 
> 31 |   const todoCreated = page.waitForResponse(
     |                            ^ Error: page.waitForResponse: Test timeout of 30000ms exceeded.
  32 |     r => r.url().includes('/todos') && r.request().method() === 'POST'
  33 |   );
  34 |   await page.getByRole('button', { name: 'Create' }).click();
  35 |   await todoCreated; // ensure todo creation fully settles before navigating
  36 | 
  37 |   await page.getByRole('button', { name: '? Q&A' }).click();
  38 |   // wait for Q&A content to render before interacting
  39 |   await page.getByRole('button', { name: '+ Add Q&A' }).waitFor();
  40 | 
  41 |   await page.locator('#qa-icon-1').click();
  42 |   await page.locator('#qa-icon-1').click();
  43 | 
  44 | 
  45 | 
  46 |   await page.getByRole('button', { name: '+ Add Q&A' }).click();
  47 |   await page.getByRole('textbox', { name: 'Enter your question…' }).fill('Milk');
  48 |   await page.getByRole('textbox', { name: 'Enter your question…' }).press('Tab');
  49 |   await page.getByRole('textbox', { name: 'Enter the answer…' }).fill('Milk');
  50 |   await page.getByRole('button', { name: 'Add', exact: true }).click();
  51 | 
  52 |   
  53 |   await page.getByRole('button', { name: 'Sign out' }).click();
  54 | });
```