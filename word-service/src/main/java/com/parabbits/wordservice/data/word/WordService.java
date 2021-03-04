package com.parabbits.wordservice.data.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WordService {

    private WordRepository wordRepository;
    private TranslationRepository translationRepository;

    @Autowired
    public WordService(WordRepository wordRepository, TranslationRepository translationRepository){
        this.wordRepository = wordRepository;
        this.translationRepository = translationRepository;
    }

    // TODO: wstawienie nowego wpisyu

    public Optional<Word> getWordById(long id){
        return wordRepository.findByIdWithTranslations(id);
    }

    public Optional<Word> saveWord(Word word){
        return wordRepository.save(word);
    }

    public void addWord(Word word, Translation translation){
        // TODO: sprawdzić, czy słowo istnieje
        // TODO: sprawdzić, czy tłumaczenie istnieje

        // TODO: sprawdzić, czy słowo istnieje w bazie danych

    }
}
