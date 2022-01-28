package cat.nyaa.catail.common;

public interface Element<T> extends Location {
    ElementData<T> getState();

    void setState(ElementData<T> state);
}
