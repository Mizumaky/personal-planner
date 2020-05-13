INSERT INTO task (title, description) VALUES
    ('uklidit si pokoj', ''),
    ('zaridit kolej', 'rezervovat v systemu na pristi rok'),
    ('pracovat na semestralce DBS', ''),
    ('ucit se japonstinu', 'genki textbook, pripadne neco watchnout bez titulku'),
    ('nakoupit jidlo na vikend', ''),
    ('uznani predmetu z KYRu', 'donest papiry na studijni')
;

INSERT INTO category (title, parent) VALUES
    ('ucel', NULL),
        ('skola', 1),
    ('typ', NULL),
    ('misto', NULL),
    ('priorita', NULL),
    ('plan', NULL)
;

INSERT INTO tag (title, category, color) VALUES
        ('ukoly', 2, 'FFFFFF'),      --1
        ('skola ostatni', 2, 'FFFFFF'),
    ('preziti', 1, 'FFFFFF'),
    ('osobni', 1, 'FFFFFF'),
    ('pratele', 1, 'FFFFFF'),
    ('ostatni', 1, 'FFFFFF'),   --6

    ('quick', 3, 'FFFFFF'),   --7
    ('odpocinkovy', 3, 'FFFFFF'),
    ('bezny', 3, 'FFFFFF'),
    ('narocny', 3, 'FFFFFF'),
    ('special', 3, 'FFFFFF'),   --11

    ('doma', 4, 'FFFFFF'),  --12
    ('ve skole', 4, 'FFFFFF'),
    ('na koleji', 4, 'FFFFFF'),
    ('na ceste', 4, 'FFFFFF'),
    ('jinde', 4, 'FFFFFF'),     --16

    ('critical', 5, 'ff704d'),  --17
    ('must do', 5, 'ffa64d'),
    ('todo', 5, 'ffff4d'),
    ('maybe do', 5, 'c2d6d6'),   --20

    ('brzy', 6, 'FFFFFF'),   --21
    ('tento tyden', 6, 'FFFFFF'),
    ('behem 14 dni', 6, 'FFFFFF'),
    ('az bude cas', 6, 'FFFFFF'),
    ('bez urceni', 6, 'FFFFFF')  --25
;

INSERT INTO task_tag (task_id, tag_id) VALUES
    (1, 6),
    (1, 8),
    (1, 14),
    (1, 19),
    (1, 22),

    (2, 3),
    (2, 6),
    (2, 13),
    (2, 18),
    (2, 24),

    (3, 1),
    (3, 10),
    (3, 12),
    (3, 14),
    (3, 17),
    (3, 21),

    (4, 4),
    (4, 8),
    (4, 12),
    (4, 14),
    (4, 15),
    (4, 20),
    (4, 25),

    (5, 3),
    (5, 9),
    (5, 15),
    (5, 19),
    (5, 21),

    (6, 2),
    (6, 9),
    (6, 13),
    (6, 17),
    (6, 23)
;