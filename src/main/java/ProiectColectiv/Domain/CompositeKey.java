package ProiectColectiv.Domain;

import java.io.Serializable;

public record CompositeKey<K1, K2>(K1 key1, K2 key2) implements Serializable {
}
