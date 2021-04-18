package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.LocaleFallback;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonLocaleFallback implements LocaleFallback {

    private final Locale fallback;

    private final Map<Locale, Locale> fallbackMap = new ConcurrentHashMap<>();

    public CommonLocaleFallback(Locale fallback, Map<Locale, Locale> fallbackMap) {
        this.fallback = fallback;
        this.fallbackMap.putAll(fallbackMap);
    }

    @Override
    public Locale getLocaleFallback(Locale desired) {
        return fallbackMap.getOrDefault(desired, fallback);
    }
}
