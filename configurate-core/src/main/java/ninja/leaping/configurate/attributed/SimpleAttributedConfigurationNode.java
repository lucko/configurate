/**
 * Configurate
 * Copyright (C) zml and Configurate contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninja.leaping.configurate.attributed;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.SimpleConfigurationNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a configuration node containing comments
 */
public class SimpleAttributedConfigurationNode extends SimpleConfigurationNode implements AttributedConfigurationNode {
    private String tagName;
    private final Map<String, String> attributes = new HashMap<>();

    public static SimpleAttributedConfigurationNode root() {
        return root("root", ConfigurationOptions.defaults());
    }

    public static SimpleAttributedConfigurationNode root(String tagName) {
        return root(tagName, ConfigurationOptions.defaults());
    }

    public static SimpleAttributedConfigurationNode root(String tagName, ConfigurationOptions options) {
        return new SimpleAttributedConfigurationNode(tagName, null, null, options);
    }

    protected SimpleAttributedConfigurationNode(String tagName, Object path, SimpleConfigurationNode parent, ConfigurationOptions options) {
        super(path, parent, options);
        this.tagName = tagName;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public SimpleAttributedConfigurationNode setTagName(String tagName) {
        if (Strings.isNullOrEmpty(tagName)) {
            throw new IllegalArgumentException("Tag name cannot be null/empty");
        }

        this.tagName = tagName;
        return this;
    }

    @Override
    public SimpleAttributedConfigurationNode addAttribute(String name, String value) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Attribute name cannot be null/empty");
        }

        attributes.put(name, value);
        return this;
    }

    @Override
    public SimpleAttributedConfigurationNode removeAttribute(String name) {
        attributes.remove(name);
        return this;
    }

    @Override
    public SimpleAttributedConfigurationNode setAttributes(Map<String, String> attributes) {
        for (String name : attributes.keySet()) {
            if (Strings.isNullOrEmpty(name)) {
                throw new IllegalArgumentException("Attribute name cannot be null/empty");
            }
        }

        this.attributes.clear();
        this.attributes.putAll(attributes);
        return this;
    }

    @Override
    public boolean hasAttributes() {
        return !attributes.isEmpty();
    }

    @Override
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Map<String, String> getAttributes() {
        return ImmutableMap.copyOf(attributes);
    }

    @Override
    public SimpleAttributedConfigurationNode getParent() {
        return (SimpleAttributedConfigurationNode) super.getParent();
    }

    @Override
    protected SimpleAttributedConfigurationNode createNode(Object path) {
        return new SimpleAttributedConfigurationNode("element", path, this, getOptions());
    }

    @Override
    public SimpleAttributedConfigurationNode setValue(Object value) {
        if (value instanceof AttributedConfigurationNode) {
            AttributedConfigurationNode node = (AttributedConfigurationNode) value;
            setTagName(node.getTagName());
            setAttributes(node.getAttributes());
        }
        return (SimpleAttributedConfigurationNode) super.setValue(value);
    }

    @Override
    public SimpleAttributedConfigurationNode mergeValuesFrom(ConfigurationNode other) {
        if (other instanceof AttributedConfigurationNode) {
            AttributedConfigurationNode node = (AttributedConfigurationNode) other;
            setTagName(node.getTagName());
            for (Map.Entry<String, String> attribute : node.getAttributes().entrySet()) {
                addAttribute(attribute.getKey(), attribute.getValue());
            }
        }
        return (SimpleAttributedConfigurationNode) super.mergeValuesFrom(other);
    }

    @Override
    public SimpleAttributedConfigurationNode getNode(Object... path) {
        return (SimpleAttributedConfigurationNode) super.getNode(path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<? extends SimpleAttributedConfigurationNode> getChildrenList() {
        return (List<SimpleAttributedConfigurationNode>) super.getChildrenList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, ? extends SimpleAttributedConfigurationNode> getChildrenMap() {
        return (Map<Object, SimpleAttributedConfigurationNode>) super.getChildrenMap();
    }

    @Override
    public SimpleAttributedConfigurationNode getAppendedNode() {
        return (SimpleAttributedConfigurationNode) super.getAppendedNode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleAttributedConfigurationNode)) return false;
        if (!super.equals(o)) return false;
        SimpleAttributedConfigurationNode that = (SimpleAttributedConfigurationNode) o;
        return tagName.equals(that.tagName) && attributes.equals(that.attributes);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + tagName.hashCode();
        result = 31 * result + attributes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SimpleAttributedConfigurationNode{" +
                "super=" + super.toString() + ", " +
                "tagName=" + tagName + ", " +
                "attributes=" + attributes +
                '}';
    }
}
