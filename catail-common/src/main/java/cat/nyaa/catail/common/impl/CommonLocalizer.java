package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.LocaleFallback;
import cat.nyaa.catail.common.Localizer;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommonLocalizer implements Localizer {

    private final LocaleFallback localeFallback;

    public CommonLocalizer(LocaleFallback localeFallback) {
        this.localeFallback = localeFallback;
    }

    @Override
    public String getLocalized(Map<Locale, String> locMap, Locale desired) {
        Locale locale = desired;
        String localized = locMap.get(locale);
        while (Objects.isNull(localized)) {
            Locale next = localeFallback.getLocaleFallback(locale);
            if (next.equals(locale)) {
                break;
            }
            locale = next;
            localized = locMap.get(locale);
        }
        return localized;
    }
}
