package cat.nyaa.catail.common.impl;

import cat.nyaa.catail.common.Area;
import cat.nyaa.catail.common.Identifier;
import cat.nyaa.catail.common.Localizer;
import cat.nyaa.catail.common.Location;
import java.util.Locale;
import java.util.Map;

public class CommonArea implements Area {

    private final Localizer localizer;
    private final Identifier id;
    private final Location upperSouthEastBound;
    private final Location lowerNorthWestBound;
    private final Map<Locale, String> names;

    public CommonArea(
        Identifier id,
        Location upperSouthEastBound,
        Location lowerNorthWestBound,
        Map<Locale, String> names,
        Localizer localizer
    ) {
        this.localizer = localizer;
        this.upperSouthEastBound = upperSouthEastBound;
        this.lowerNorthWestBound = lowerNorthWestBound;
        this.id = id;
        this.names = names;
    }

    @Override
    public Location getUpperSouthEastBound() {
        return upperSouthEastBound;
    }

    @Override
    public Location getLowerNorthWestBound() {
        return lowerNorthWestBound;
    }

    @Override
    public boolean isInArea(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return (
            (x > lowerNorthWestBound.getX()) &&
            (y > lowerNorthWestBound.getY()) &&
            (z > lowerNorthWestBound.getZ()) &&
            (x < upperSouthEastBound.getX()) &&
            (y < upperSouthEastBound.getY()) &&
            (z < upperSouthEastBound.getZ())
        );
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public String getName(Locale locale) {
        return localizer.getLocalized(this.names, locale);
    }
}
