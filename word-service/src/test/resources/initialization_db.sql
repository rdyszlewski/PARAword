INSERT INTO languages(id, name, abbreviation) VALUES(1, 'english', 'eng');
INSERT INTO languages(id, name, abbreviation) VALUES(2, 'german', 'ger');

INSERT INTO collections(id, name, description, user, language_id) VALUES (1, 'Angielski 1', 'To jest kurs angielskiego', 1, 1);
INSERT INTO collections(id, name, description, user, language_id) VALUES (2, 'Angielski 2', 'To jest nowy kurs angielskiego', 2, 2);

INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES(1,'dog','Dog dog', 1, 1, 0);
INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES(2,'dog','Dog dog', 1, 1, 1);
INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES(3,'cat','Cute', 2, 1, 0);
INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES(4,'mouse','Monitor description', 1, 1, 0);
INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES(5,'duck','Qua qua', 2, 2, 0);
INSERT INTO words(id, word, description, part_of_speech, collection_id, meaning) VALUES(6,'phone','Ring ring', 2, 2, 0);

INSERT INTO translations(id,name, description, meaning) VALUES(1,'pies', 'Piesek', 0);
INSERT INTO translations(id,name, description, meaning) VALUES(2,'dogryźć', null, 0);
INSERT INTO translations(id,name, description, meaning) VALUES(3,'cat', 'Kotek', 0);
INSERT INTO translations(id,name, description, meaning) VALUES(4,'mysz', null, 0);
INSERT INTO translations(id,name, description, meaning) VALUES(5,'myszka', null, 0);
INSERT INTO translations(id,name, description, meaning) VALUES(6,'kaczka', null, 0);
INSERT INTO translations(id,name, description, meaning) VALUES(7,'telefon', null, 0);
INSERT INTO translations(id,name, description, meaning) VALUES(8, 'mysz', null, 1);

INSERT INTO words_translations(word_fk, translation_fk) VALUES (1,1);
INSERT INTO words_translations(word_fk, translation_fk) VALUES (2,2);
INSERT INTO words_translations(word_fk, translation_fk) VALUES (3,3);
INSERT INTO words_translations(word_fk, translation_fk) VALUES (4,4);
INSERT INTO words_translations(word_fk, translation_fk) VALUES (4,5);
INSERT INTO words_translations(word_fk, translation_fk) VALUES (5,6);
INSERT INTO words_translations(word_fk, translation_fk) VALUES (6,7);
INSERT INTo words_translations(word_fk, translation_fk) VALUES(4,8);