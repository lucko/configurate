package ninja.leaping.configurate.value.types;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.value.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface ListValue extends Value {

    // read

    List<?> unwrap();

    List<ConfigurationNode> asList();

    Stream<ConfigurationNode> stream();

    // write

    boolean add(ConfigurationNode node);

    boolean remove(ConfigurationNode node);

    ConfigurationNode remove(int index);

    boolean addAll(Collection<? extends ConfigurationNode> nodes);

    void clear();

}
