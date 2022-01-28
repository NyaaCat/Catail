package cat.nyaa.catail.common;

public interface ElementType<T> {
    Identifier getIdentifier();
    Class<T> getBaseType();
}
