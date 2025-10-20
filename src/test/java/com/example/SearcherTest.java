package com.example;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class SearcherTest {

    @Test
    void testSearchExactPhrase_found() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("hola mundo", "adios mundo", "hola de nuevo");
        assertTrue(searcher.searchExactPhrase("hola mundo", list));
    }

    @Test
    void testSearchExactPhrase_notFound() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("hola mundo", "adios mundo");
        assertFalse(searcher.searchExactPhrase("hola planeta", list));
    }

    @Test
    void testSearchWord_found() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("Java es genial", "Java", "Me gusta programar");
        boolean resultado = searcher.searchWord("Java", list) ;
        assertTrue(resultado);
    }

    @Test
    void testGetWordByIndex_valid() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("uno", "dos", "tres");
        assertEquals("dos", searcher.getWordByIndex(list, 1));
    }

    @Test
    void testGetWordByIndex_outOfBounds() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("uno", "dos");
        assertNull( searcher.getWordByIndex(list, 5));
    }

    @Test
    void testSearchByPrefix_found() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("perro", "persona", "gato");
        List<String> result = searcher.searchByPrefix("per", list);
        assertEquals(2, result.size());
        assertTrue(result.contains("perro"));
    }

    @Test
    void testFilterByKeyword() {
        Searcher searcher = new Searcher();
        List<String> list = Arrays.asList("el sol brilla", "la luna brilla", "oscuro");
        List<String> result = searcher.filterByKeyword("brilla", list);
        assertEquals(2, result.size());
    }
}
