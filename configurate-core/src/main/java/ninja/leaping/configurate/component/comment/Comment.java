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
package ninja.leaping.configurate.component.comment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import ninja.leaping.configurate.component.NodeComponent;

import java.util.Objects;

/**
 * Node component that represents a "comment".
 *
 * <p>A comment is a string description of the corresponding node.</p>
 */
public interface Comment extends NodeComponent {

    /**
     * Returns an {@link Comment} component with no value.
     *
     * @return An empty comment component
     */
    @NonNull
    static Comment noComment() {
        return SimpleComment.NO_COMMENT;
    }

    /**
     * Returns a {@link Comment} component with the given value.
     *
     * @param value The value
     * @return The resultant comment component
     */
    @NonNull
    static Comment of(@NonNull String value) {
        return new SimpleComment(Objects.requireNonNull(value, "value"));
    }

    /**
     * Gets the value of the comment.
     *
     * <p>If the comment contains multiple lines, the lines will be split by <code>\n</code>.</p>
     *
     * @return The comment, or null if it doesn't have one
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
