MERGE INTO MPA_RATING (ID_RATING, name,DESCRIPTION)
    VALUES (1, 'G','1'),
           (2, 'PG','2'),
           (3, 'PG-13','3'),
           (4, 'R','4'),
           (5, 'NC-17','5');

MERGE INTO GENRES (ID_GENRE, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');