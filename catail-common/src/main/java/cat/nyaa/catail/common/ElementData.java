package cat.nyaa.catail.common;

public interface ElementData<T> {
    ElementType<T> getElementType();

    String getStateName();

    String getAsString();
}
