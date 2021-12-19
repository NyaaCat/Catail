package cat.nyaa.catail.common.fabric;

import cat.nyaa.catail.common.Block;
import cat.nyaa.catail.common.BlockGroupState;
import cat.nyaa.catail.common.Identifier;
import cat.nyaa.catail.common.Junctions;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class FakeJunctions implements Junctions {

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public String getName(Locale locale) {
        return null;
    }

    @Override
    public Set<Block> getJunctionBlocks() {
        return null;
    }

    @Override
    public Map<String, BlockGroupState> getJunctionStates() {
        return null;
    }

    @Override
    public Map<String, String> getJunctionStateNames() {
        return null;
    }

    @Override
    public void addJunctionState(String id, String name, BlockGroupState state) {

    }

    @Override
    public void removeJunctionState(String id, String name, BlockGroupState state) {

    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getZ() {
        return 0;
    }
}
