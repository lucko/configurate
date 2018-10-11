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
package ninja.leaping.configurate.component.name;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import ninja.leaping.configurate.component.NodeComponent;

import java.util.Objects;

/**
 * Node component that represents a "name" for the corresponding node.
 */
public interface Name extends NodeComponent {

    /**
     * Returns an {@link Name} component with no value.
     *
     * @return An empty name component
     */
    @NonNull
    static Name unnamed() {
        return SimpleName.UNNAMED;
    }

    /**
     * Returns a {@link Name} component with the given value.
     *
     * @param value The value
     * @return The resultant name component
     */
    @NonNull
    static Name of(@NonNull String value) {
        return new SimpleName(Objects.requireNonNull(value, "value"));
    }

    /**
     * Gets the name.
     *
     * @return The name, or null if one isn't present
     */
    @Nullable
    String getValue();

    /**
     * Gets if a value is present.
     *
     * @return If a value is present
     */
    default boolean hasValue() {
        return getValue() != null;
    }

}
