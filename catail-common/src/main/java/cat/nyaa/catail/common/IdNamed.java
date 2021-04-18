package cat.nyaa.catail.common;

import java.util.Locale;

public interface IdNamed {
    Identifier getId();

    String getName(Locale locale);
}
