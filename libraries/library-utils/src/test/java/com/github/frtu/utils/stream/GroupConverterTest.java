package com.github.frtu.utils.stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
public class GroupConverterTest {
    @Test
    public void toMapByKey() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        List<String> wordList = Arrays.asList("alphabet", "ballon", "cat", "dog");

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        // Dict of first letter
        final Map<Character, String> characterStringMap = GroupConverter.toMapByKey(wordList, word -> word.charAt(0));

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertEquals("alphabet", characterStringMap.get('a'));
        assertEquals("ballon", characterStringMap.get('b'));
        assertEquals("cat", characterStringMap.get('c'));
        assertEquals("dog", characterStringMap.get('d'));
    }

    @Test
    public void toMapByValue() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        List<String> wordList = Arrays.asList("alphabet", "ballon", "cat", "dog");

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final Map<String, Integer> characterStringMap = GroupConverter.toMapByValue(wordList, String::length);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertEquals((Integer) 8, characterStringMap.get("alphabet"));
        assertEquals((Integer) 6, characterStringMap.get("ballon"));
        assertEquals((Integer) 3, characterStringMap.get("cat"));
        assertEquals((Integer) 3, characterStringMap.get("dog"));
    }

    @Test
    public void toJsonNumberArray() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4);

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final String json = GroupConverter.toJsonNumberArray(numberList);
        LOGGER.debug(json);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        ObjectMapper objectMapper = new ObjectMapper();
        final List<Integer> resultList = objectMapper
                .readValue(json, new TypeReference<List<Integer>>() {
                });

        assertEquals(numberList.size(), resultList.size());
        final Integer integer = resultList.get(0);
        LOGGER.debug("First item:{}, type:{}", integer);
        assertEquals(1, integer.intValue());
    }

    @Test
    public void toJsonStringArray() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        List<String> wordList = Arrays.asList("alphabet", "ballon", "cat", "dog");

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final String json = GroupConverter.toJsonStringArray(wordList);
        LOGGER.debug(json);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        ObjectMapper objectMapper = new ObjectMapper();
        final List<String> resultList = objectMapper
                .readValue(json, new TypeReference<List<String>>() {
                });
        assertEquals(wordList.size(), resultList.size());
        assertEquals("alphabet", resultList.get(0));
        assertEquals("ballon", resultList.get(1));
        assertEquals("cat", resultList.get(2));
        assertEquals("dog", resultList.get(3));
    }
}