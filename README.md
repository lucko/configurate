# Configurate
Configurate is a simple configuration library released under the [Apache 2.0](LICENSE) that provides a node-tree representation of configurations in a variety of formats.

The upstream repository is located at [SpongePowered/configurate](https://github.com/SpongePowered/configurate) - but doesn't seem to be receiving any updates.

This fork of configurate contains the following changes:

* Implemented a loader for the XML format (upstream PR #90)
* Implemented a loader for the TOML format (upstream PR #69)
* A considerable number of improvements to the projects JavaDocs (upstream PR #92)
* Changed the TypeSerializerCollection to select from available type serializers in the order they were added (upstream PR #88)
* Introduced more consistent formatting in the build scripts (upstream PR #92)
* Fix object mapping for interface/abstract field types (upstream PR #91 by @dags-)

## Building
We use Maven, so this part is pretty easy. 

Configurate requires JDK 8 to build and run.

Make sure Maven is installed and from the project's directory (the root of this repository), run `mvn clean install` to build Configuate and install its artifacts to the local Maven repository.

## Usage

**Maven**:
```xml
<dependency>
    <groupId>me.lucko.configurate</groupId>
    <artifactId>configurate-hocon</artifactId>
    <version>3.4</version>
</dependency>
``` 

**Gradle**:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'me.lucko.configurate:configurate-hocon:3.4'
}
```

This dependency statement is for the hocon format implementation. Other formats managed in this repository use the same group id and versioning.

Now, to load:
```java
ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(file).build(); // Create the loader
CommentedConfigurationNode node = loader.load(); // Load the configuration into memory

node.getNode("some", "value").getValue(); // Get the value
```

More detailed explanations of all the methods available in ConfigurationNode are available in the javadocs.

## Contributing
We love PRs! However, when contributing, here are some things to keep in mind:

- Take a look at open issues first before you get too far in -- someone might already be working on what you were planning on doing
- In general, we follow the Oracle style guidelines for code style
- Please, please, please test PRs. It makes the process a lot easier for everybody :)
