package com.example;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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
        List<String> list = Arrays.asList("Java es genial", "Me gusta programar");
        assertTrue(searcher.searchWord("Java", list));
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
        assertThrows(IndexOutOfBoundsException.class,
                () -> searcher.getWordByIndex(list, 5));
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
