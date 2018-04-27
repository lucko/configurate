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

import ninja.leaping.configurate.ConfigurationNode;

import java.util.List;
import java.util.Map;

/**
 * A configuration node that is capable of having attributes
 */
public interface AttributedConfigurationNode extends ConfigurationNode {

    /**
     * Gets the tag name of this node.
     *
     * @return the tag name
     */
    String getTagName();

    /**
     * Sets the tag name of this node.
     *
     * <p>Will have no effect when called on nodes which are direct values of a
     * {@link #getChildrenMap() child map}, as the corresponding key is used as
     * the tag name.</p>
     *
     * @param name the name to set, cannot be null
     * @return this
     */
    AttributedConfigurationNode setTagName(String name);

    /**
     * Adds an attribute to this node
     *
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return this
     */
    AttributedConfigurationNode addAttribute(String name, String value);

    /**
     * Removes an attribute from this node
     *
     * @param name the name of the attribute to remove
     * @return this
     */
    AttributedConfigurationNode removeAttribute(String name);

    /**
     * Sets the attributes of this node
     *
     * @param attributes the attributes to set
     * @return this
     */
    AttributedConfigurationNode setAttributes(Map<String, String> attributes);

    /**
     * Gets if this node has any attributes
     *
     * @return true if this node has any attributes
     */
    boolean hasAttributes();

    /**
     * Gets the value of an attribute, or null if this node doesn't have the
     * given attribute
     *
     * @param name the name of the attribute to get
     * @return this
     */
    String getAttribute(String name);

    /**
     * Gets the attributes this node has
     *
     * <p>The returned map is immutable.</p>
     *
     * @return this
     */
    Map<String, String> getAttributes();

    // Methods from superclass overridden to have correct return types

    AttributedConfigurationNode getParent();
    @Override
    List<? extends AttributedConfigurationNode> getChildrenList();
    @Override
    Map<Object, ? extends AttributedConfigurationNode> getChildrenMap();
    @Override
    AttributedConfigurationNode setValue(Object value);
    @Override
    AttributedConfigurationNode mergeValuesFrom(ConfigurationNode other);
    @Override
    AttributedConfigurationNode getAppendedNode();
    @Override
    AttributedConfigurationNode getNode(Object... path);
}
