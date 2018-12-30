package ninja.leaping.configurate.value.types;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.value.Value;

import java.util.Map;
import java.util.stream.Stream;

public interface MapValue extends Value {

    // read

    Map<String, ?> unwrap();

    Map<String, ConfigurationNode> asMap();

    Stream<String> keys();

    Stream<ConfigurationNode> values();

    Stream<Map.Entry<String, ConfigurationNode>> entries();



}
