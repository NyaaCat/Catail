package cat.nyaa.catail.common;

public interface Area extends IdNamed {
    Location getUpperSouthEastBound();

    Location getLowerNorthWestBound();

    boolean isInArea(Location location);
}
