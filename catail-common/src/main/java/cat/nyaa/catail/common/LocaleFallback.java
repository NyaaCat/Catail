package cat.nyaa.catail.common;

import java.util.Locale;

@FunctionalInterface
public interface LocaleFallback {
    Locale getLocaleFallback(Locale desired);
}
