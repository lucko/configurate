/*
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
package ninja.leaping.configurate.component.types;

import com.google.common.collect.ImmutableMap;

import ninja.leaping.configurate.component.NodeComponent;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * Node component that represents "attributes".
 *
 * <p>Attributes are extra properties a node can hold, in the form of a
 * map of string key/value pairs.</p>
 *
 * <p>All {@link NodeComponent}s are immutable, so the {@link #addAttribute(String, String)} and
 * {@link #removeAttribute(String)} methods return copies of the source component with the given
 * change made.</p>
 */
public interface Attributes extends NodeComponent {

    /**
     * Returns an {@link Attributes} component with no set values.
     *
     * @return An empty attributes component
     */
    @NonNull
    static Attributes empty() {
        return SimpleAttributes.EMPTY;
    }

    /**
     * Returns an {@link Attributes} component with the values from the given source map.
     *
     * @param attributes The source attributes
     * @return An attributes component for the given source
     */
    @NonNull
    static Attributes of(@NonNull Map<String, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");
        if (attributes.isEmpty()) {
            return empty();
        }
        return new SimpleAttributes(ImmutableMap.copyOf(attributes));
    }

    /**
     * Gets if any attributes are set.
     *
     * @return <code>true</code> if attributes are set
     */
    boolean hasAttributes();

    /**
     * Gets the value of an attribute, or null if no such attribute is present.
     *
     * @param name The name of the attribute to get
     * @return The value
     */
    @Nullable
    String getAttribute(@NonNull String name);

    /**
     * Gets the attributes.
     *
     * @return An immutable map of attributes
     */
    @NonNull
    Map<String, String> getAttributes();

    /**
     * Makes a copy of this set of {@link Attributes}, adds the given
     * attribute to it, and returns the result.
     *
     * @param name The name of the attribute to add
     * @param value The value of the attribute to add
     * @return The result
     */
    @NonNull
    Attributes addAttribute(@NonNull String name, @NonNull String value);

    /**
     * Makes a copy of this set of {@link Attributes}, removes the given
     * attribute from it, and returns the result.
     *
     * @param name The name of the attribute to remove
     * @return The result
     */
    @NonNull
    Attributes removeAttribute(@NonNull String name);

}
