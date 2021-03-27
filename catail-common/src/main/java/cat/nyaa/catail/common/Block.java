package cat.nyaa.catail.common;

public interface Block extends Location {
    BlockData getState();
    void setState(BlockData state);
}
