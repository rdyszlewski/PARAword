package com.parabbits.wordservice.word.data.word;

import com.parabbits.wordservice.word.data.translation.Translation;
import com.parabbits.wordservice.word.data.translation.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final TranslationRepository translationRepository;

    @Autowired
    public WordService(WordRepository wordRepository, TranslationRepository translationRepository) {
        this.wordRepository = wordRepository;
        this.translationRepository = translationRepository;
    }

    // TODO: wstawienie nowego wpisyu

    public Optional<Word> getWordById(long id) {
//        return wordRepository.findByIdWithTranslations(id);
        return null;
    }

    public Optional<Word> saveWord(Word word) {
//        return wordRepository.save(word);
        return null;
    }

    public void addWord(Word word, Translation translation) {
        // TODO: sprawdzić, czy słowo istnieje
        // TODO: sprawdzić, czy tłumaczenie istnieje

        // TODO: sprawdzić, czy słowo istnieje w bazie danych

    }
}
