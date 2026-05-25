INSERT INTO role (name)
VALUES
    ('ADMIN'),
    ('REGULAR'),
    ('NONE')
ON CONFLICT (name) DO NOTHING;

INSERT INTO account (username, password, role_name)
VALUES
    ('admin', 'AdminPass2026', (SELECT id FROM role WHERE name = 'ADMIN')),
    ('mikkel', 'MikkelPass2026', (SELECT id FROM role WHERE name = 'REGULAR')),
    ('sarah', 'SarahPass2026', (SELECT id FROM role WHERE name = 'REGULAR')),
    ('support', 'SupportPass2026', (SELECT id FROM role WHERE name = 'REGULAR'))
ON CONFLICT (username) DO NOTHING;

INSERT INTO header (text, role_id)
SELECT seed.text, role.id
FROM (
    VALUES
        ('Dashboard', 'REGULAR'),
        ('Licenser', 'REGULAR'),
        ('Spilbibliotek', 'REGULAR'),
        ('Opgaver', 'REGULAR'),
        ('FAQ', 'REGULAR'),
        ('Admin', 'ADMIN')
) AS seed(text, role_name)
JOIN role ON role.name = seed.role_name
WHERE NOT EXISTS (
    SELECT 1
    FROM header
    WHERE header.text = seed.text
      AND header.role_id = role.id
);

INSERT INTO footer (header, description, role_id)
SELECT seed.header, seed.description, role.id
FROM (
    VALUES
        ('Support', 'Kontakt support@astralis.local ved problemer med adgang eller licenser.', 'REGULAR'),
        ('Driftstatus', 'Planlagt vedligehold varsles mindst 24 timer foer pa dashboardet.', 'REGULAR'),
        ('Sikkerhed', 'Del aldrig adgangskoder eller licensnoegler uden for teamets godkendte kanaler.', 'ADMIN')
) AS seed(header, description, role_name)
JOIN role ON role.name = seed.role_name
WHERE NOT EXISTS (
    SELECT 1
    FROM footer
    WHERE footer.header = seed.header
      AND footer.description = seed.description
      AND footer.role_id = role.id
);

INSERT INTO game (name, account_id)
SELECT seed.name, account.id
FROM (
    VALUES
        ('Counter-Strike 2', 'mikkel'),
        ('League of Legends', 'sarah'),
        ('Valorant', 'support'),
        ('Rocket League', 'mikkel')
) AS seed(name, username)
JOIN account ON account.username = seed.username
ON CONFLICT (name) DO NOTHING;

INSERT INTO license (username, password, email, pc_number, game_id, status)
SELECT seed.username, seed.password, seed.email, seed.pc_number, game.id, seed.status
FROM (
    VALUES
        ('mikkel_cs2', 'LicensePass2026', 'mikkel.cs2@example.com', 2, 'Counter-Strike 2', 'ACTIVE'),
        ('sarah_lol', 'LicensePass2026', 'sarah.lol@example.com', 1, 'League of Legends', 'ACTIVE'),
        ('support_val', 'LicensePass2026', 'support.valorant@example.com', 0, 'Valorant', 'INACTIVE'),
        ('mikkel_rl', 'LicensePass2026', 'mikkel.rocket@example.com', 1, 'Rocket League', 'ACTIVE')
) AS seed(username, password, email, pc_number, game_name, status)
JOIN game ON game.name = seed.game_name
ON CONFLICT (username) DO NOTHING;

INSERT INTO information (description, importanceLevel, account_id)
SELECT seed.description, seed.importance_level, account.id
FROM (
    VALUES
        ('Nye licenser skal kontrolleres og aktiveres inden naeste turneringsrunde.', 'HIGH', 'admin'),
        ('Husk at opdatere pc-nummer, naar en licens flyttes til en ny maskine.', 'MEDIUM', 'support'),
        ('Spilbiblioteket bliver gennemgaaet for dubletter hver fredag formiddag.', 'LOW', 'sarah')
) AS seed(description, importance_level, username)
JOIN account ON account.username = seed.username
WHERE NOT EXISTS (
    SELECT 1
    FROM information
    WHERE information.description = seed.description
      AND information.account_id = account.id
);

INSERT INTO qa (question, answer, account_id)
SELECT seed.question, seed.answer, account.id
FROM (
    VALUES
        ('Hvordan aktiverer jeg en ny licens?', 'Find spillet i biblioteket, opret licensen med brugerens email og marker status som ACTIVE.', 'support'),
        ('Hvad betyder pc-nummer?', 'Pc-nummeret viser hvor mange maskiner licensen allerede er bundet til.', 'support'),
        ('Hvordan lukker jeg en gammel licens?', 'Skift licensens status til INACTIVE og opret en opgave, hvis der skal foelges op.', 'admin')
) AS seed(question, answer, username)
JOIN account ON account.username = seed.username
WHERE NOT EXISTS (
    SELECT 1
    FROM qa
    WHERE qa.question = seed.question
      AND qa.account_id = account.id
);

INSERT INTO todo (date, description, status, source, done_by, account_id)
SELECT seed.date_value, seed.description, seed.status, seed.source, seed.done_by, account.id
FROM (
    VALUES
        (CURRENT_DATE, 'Kontroller aktive CS2-licenser mod teamlisten', 'IN_PROGRESS', 'GAMEHUB', 'mikkel', 'mikkel'),
        (CURRENT_DATE + 1, 'Opret licens til ny Valorant-spiller', 'PENDING', 'STORE', 'support', 'support'),
        (CURRENT_DATE - 1, 'Afslut inaktive Rocket League', 'COMPLETED', 'GAMEHUB', 'mikkel', 'mikkel'),
        (CURRENT_DATE + 3, 'Gennemgaa FAQ-svar for licensprocessen', 'PENDING', 'STORE', 'sarah', 'sarah')
) AS seed(date_value, description, status, source, done_by, username)
JOIN account ON account.username = seed.username
WHERE NOT EXISTS (
    SELECT 1
    FROM todo
    WHERE todo.description = seed.description
      AND todo.account_id = account.id
);
