package com.plexobject.dp.marshal;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MarshallerFactory {
    private static final class Key {
        private final Class<?> from;
        private final Class<?> to;

        private Key(final Class<?> from, final Class<?> to) {
            Objects.requireNonNull(from);
            Objects.requireNonNull(to);
            this.from = from;
            this.to = to;
        }

        /**
         * @see java.lang.Object#equals(Object)
         */
        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Key)) {
                return false;
            }
            Key rhs = (Key) object;
            return from == rhs.from && to == rhs.to;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return from.hashCode() ^ to.hashCode();
        }
    }

    private final Map<Key, Marshaller<?, ?>> converters = new ConcurrentHashMap<>();
    private static MarshallerFactory instance = new MarshallerFactory();

    private MarshallerFactory() {
    }

    public static MarshallerFactory getInstance() {
        return instance;
    }

    public void register(final Class<?> from, final Class<?> to,
            final Marshaller<?, ?> converter) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(converter);

        converters.put(new Key(from, to), converter);
    }

    @SuppressWarnings("unchecked")
    public <FROM, TO> Marshaller<FROM, TO> getMarshaller(
            final Class<FROM> from, final Class<TO> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        return (Marshaller<FROM, TO>) converters.get(new Key(from, to));
    }
}
