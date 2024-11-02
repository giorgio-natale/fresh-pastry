package it.gionatale.fp.commons;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class EntityCollectionUtils {
    public static <E, V, K> Collection<E> createEntityCollection(
            Collection<E> originalCollection, Collection<V> desiredCollection,
            Function<E, K> entityToEntityKeyMapper,
            Function<V, K> valueToEntityKeyMapper,
            BiConsumer<V, E> updater,
            Function<V, E> entityFactory) {

        Map<K, E> indexedOriginalCollection = new HashMap<>();
        for (E originalItem : originalCollection) {
            K key = entityToEntityKeyMapper.apply(originalItem);
            indexedOriginalCollection.put(key, originalItem);
        }

        return desiredCollection.stream()
                .map(desiredItem -> {
                    K desiredEntityKey = valueToEntityKeyMapper.apply(desiredItem);
                    if (indexedOriginalCollection.containsKey(desiredEntityKey)) {
                        E desiredEntity = indexedOriginalCollection.get(desiredEntityKey);
                        updater.accept(desiredItem, desiredEntity);
                        return desiredEntity;
                    } else {
                        return entityFactory.apply(desiredItem);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

    }
}
