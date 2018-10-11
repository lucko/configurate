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

import org.checkerframework.checker.nullness.qual.Nullable;

final class SimpleComment implements Comment {
    static final Comment NO_COMMENT = new SimpleComment(null);

    private final String comment;

    SimpleComment(String comment) {
        this.comment = comment;
    }

    @Override
    public @Nullable String getValue() {
        return this.comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment that = (Comment) o;
        return this.comment != null ? this.comment.equals(that.getValue()) : that.getValue() == null;
    }

    @Override
    public int hashCode() {
        return this.comment != null ? this.comment.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Comment{value=" + this.comment + '}';
    }
}
