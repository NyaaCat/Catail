package cat.nyaa.catail.common;

import java.util.Locale;
import java.util.Map;

@FunctionalInterface
public interface Localizer {
    String getLocalized(Map<Locale, String> locMap, Locale desired);
}
