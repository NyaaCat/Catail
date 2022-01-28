package cat.nyaa.catail.common;

public interface ElementDataRegistry {
    <T> ElementData<T> get(Identifier key, String name);

    <T> ElementData<T> match(Element<T> element);
}
