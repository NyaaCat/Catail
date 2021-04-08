package cat.nyaa.catail.common;

public interface BlockDataRegistry {
    BlockData get(Identifier key, String name);

    BlockData match(Block block);
}
