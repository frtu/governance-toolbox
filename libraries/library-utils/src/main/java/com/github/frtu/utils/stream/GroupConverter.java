package com.github.frtu.utils.stream;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class illustrating a few {@link java.util.stream.Stream} use cases.
 *
 * @author Frédéric TU
 * @since 1.0.1
 */
public class GroupConverter {
    /**
     * Create a Map keyed using internal items
     *
     * @param sourceList source list
     * @param keyMapper  from source type to key
     * @param <Source>   Source Type
     * @param <Key>      Key Type
     * @return converted map
     */
    public static <Source, Key> Map<Key, Source> toMapByKey(List<Source> sourceList, Function<Source, Key> keyMapper) {
        return toMap(sourceList, keyMapper, Function.identity());
    }

    /**
     * Create a Map keyed using internal items
     *
     * @param sourceList  source list
     * @param valueMapper from source type to value
     * @param <Source>    Source Type
     * @param <Value>     Value Type
     * @return converted map
     */
    public static <Source, Value> Map<Source, Value> toMapByValue(List<Source> sourceList, Function<Source, Value> valueMapper) {
        return toMap(sourceList, Function.identity(), valueMapper);
    }

    /**
     * Create a Map from a List using internal items
     *
     * @param sourceList  source list
     * @param keyMapper   from source type to key
     * @param valueMapper from source type to value
     * @param <Source>    Source Type
     * @param <Key>       Key Type
     * @param <Value>     Value Type
     * @return converted map
     */
    public static <Source, Key, Value> Map<Key, Value> toMap(List<Source> sourceList, Function<Source, Key> keyMapper, Function<Source, Value> valueMapper) {
        return sourceList.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * Illustration of list joining with prefix and suffix
     *
     * @param sourceList list of {@link Integer}
     * @return string of list of element under bracket
     * @see <a href="https://www.baeldung.com/java-8-collectors">Guide to Java 8’s Collectors</a>
     */
    public static String toJsonNumberArray(List<Integer> sourceList) {
        return sourceList.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Illustration of list joining with prefix and suffix
     *
     * @param sourceList list of {@link String}
     * @return string of list of quoted element under bracket
     * @see <a href="https://www.baeldung.com/java-8-collectors">Guide to Java 8’s Collectors</a>
     */
    public static String toJsonStringArray(List<String> sourceList) {
        return sourceList.stream().map(word -> "\"" + word + "\"").collect(Collectors.joining(", ", "[", "]"));
    }
}
