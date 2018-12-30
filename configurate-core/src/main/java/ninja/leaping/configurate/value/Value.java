package ninja.leaping.configurate.value;

public interface Value {

    ValueType getType();

    boolean isEmpty();

    boolean isValid();

}
