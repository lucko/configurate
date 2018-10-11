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
package ninja.leaping.configurate.component.attributes;

import com.google.common.collect.ImmutableMap;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

final class SimpleAttributes implements Attributes {
    static final Attributes EMPTY = new SimpleAttributes(ImmutableMap.of());

    private final ImmutableMap<String, String> attributes;

    SimpleAttributes(@NonNull ImmutableMap<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean hasAttributes() {
        return !this.attributes.isEmpty();
    }

    @Nullable
    @Override
    public String getAttribute(@NonNull String name) {
        return this.attributes.get(name);
    }

    @NonNull
    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @NonNull
    @Override
    public SimpleAttributes addAttribute(@NonNull String name, @NonNull String value) {
        checkAttribute("name", name);
        checkAttribute("value", value);

        Map<String, String> builder = new HashMap<>(this.attributes);
        builder.put(name, value);
        return new SimpleAttributes(ImmutableMap.copyOf(builder));
    }

    @NonNull
    @Override
    public SimpleAttributes removeAttribute(@NonNull String name) {
        checkAttribute("name", name);

        Map<String, String> builder = new HashMap<>(this.attributes);
        builder.remove(name);
        return new SimpleAttributes(ImmutableMap.copyOf(builder));
    }

    private static void checkAttribute(String type, String value) {
        if (value == null) {
            throw new NullPointerException("Attribute " + type + " is null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Attribute " + type + " is empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attributes)) return false;

        Attributes that = (Attributes) o;
        return this.attributes.equals(that.getAttributes());
    }

    @Override
    public int hashCode() {
        return this.attributes.hashCode();
    }

    @Override
    public String toString() {
        return "Attributes{attributes=" + this.attributes + '}';
    }
}
