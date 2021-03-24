INSERT INTO languages(id, name, abbreviation) VALUES
(1, 'english', 'eng'),
(2, 'german', 'ger'),
(3, 'polish', 'pl');

INSERT INTO collections(id, name, description, user, language1, language2, is_public) VALUES
(1, 'Angielski 1', 'To jest kurs angielskiego', 1, 1, 3, FALSE),
(2, 'Angielski 2', 'To jest nowy kurs angielskiego', 2, 1, 3, FALSE),
(3, 'Niemiecki 1', 'To jest kolekcja niemiecka 1', 1, 2, 3, TRUE),
(4, 'Niemiecki 2', 'To jest kolekcja niemiecka 2', 1, 2, 3, TRUE),
(5, 'Angielski 3', 'To jest kolekcja angielska', 1,1,2, TRUE),
(6, 'Niemiecki osiem', NULL, 1, 2, 2, TRUE),
(7, 'Publiczny', NULL, 2, 1,2, TRUE);

INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES
(1,'dog','Dog dog', 1, 1, 0),
(2,'dog','Dog dog', 1, 1, 1),
(3,'cat','This is not long description', 2, 1, 0),
(4,'mouse','Another not long desc', 1, 1, 0),
(5,'duck','Qua qua', 2, 2, 0),
(6,'phone','Ring ring', 2, 2, 0),
(7, 'hound', null, 1, 2, 0);

INSERT INTO translations(id,name, description, meaning, user, language_id, part_of_speech) VALUES
(1,'pies', 'Piesek', 1,1, 1, 0),
(2,'dogryźć', null, 0, 1, 1, 0),
(3,'kot', 'This is not long description', 0, 1, 1, 1),
(4,'mysz', null, 0, 1, 1, 2),
(5,'myszka', 'Another not LONG description', 1, 1, 1,2),
(6,'kaczka', null, 0, 2, 2,0),
(7,'telefon', null, 0, 2, 2,1),
(8, 'mysz', null, 1, 2, 2,2);

INSERT INTO words_translations(word_fk, translation_fk) VALUES
(1,1),
(7, 1),
(2,2),
(3,3),
(4,4),
(4,5),
(5,6),
(6,7),
(4,8);
