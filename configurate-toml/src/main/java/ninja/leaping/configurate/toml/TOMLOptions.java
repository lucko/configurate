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
package ninja.leaping.configurate.toml;

import com.moandjiezana.toml.TomlWriter;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * Represents various options for TOML saving.
 */
public class TOMLOptions {
    private final int keyIndent;
    private final int tableIndent;
    private final int arrayPadding;
    private final ZoneOffset zoneOffset;
    private final boolean fractionalSeconds;

    public static class Builder {
        private int keyIndent = 0;
        private int tableIndent = 0;
        private int arrayPadding = 0;
        private ZoneOffset zoneOffset = ZoneOffset.UTC;
        private boolean fractionalSeconds = false;
        private Builder() {}

        public Builder setKeyIndentation(int indent) {
            this.keyIndent = indent;
            return this;
        }

        public Builder setTableIndentation(int indent) {
            this.tableIndent = indent;
            return this;
        }

        public Builder setZoneOffset(ZoneOffset offset) {
            this.zoneOffset = offset;
            return this;
        }

        public Builder setArrayPadding(int padding) {
            this.arrayPadding = padding;
            return this;
        }

        public Builder setUseFractionalSeconds(boolean use) {
            this.fractionalSeconds = use;
            return this;
        }

        public Builder from(TOMLOptions options) {
            this.keyIndent = options.keyIndent;
            this.tableIndent = options.tableIndent;
            this.arrayPadding = options.arrayPadding;
            this.zoneOffset = options.zoneOffset;
            this.fractionalSeconds = options.fractionalSeconds;
            return this;
        }
        public TOMLOptions build() {
            return new TOMLOptions(this);
        }
    }

    private TOMLOptions(Builder builder) {
        this.keyIndent = builder.keyIndent;
        this.tableIndent = builder.tableIndent;
        this.arrayPadding = builder.arrayPadding;
        this.zoneOffset = builder.zoneOffset;
        this.fractionalSeconds = builder.fractionalSeconds;
    }

    public int getKeyIndent() {
        return keyIndent;
    }

    public int getTableIndent() {
        return tableIndent;
    }

    public int getArrayPaddding() {
        return arrayPadding;
    }

    public ZoneOffset getZoneOffset() {
        return zoneOffset;
    }

    public boolean isFractionalSeconds() {
        return fractionalSeconds;
    }

    TomlWriter toTomlWriter() {
        TomlWriter.Builder builder = new TomlWriter.Builder().indentTablesBy(tableIndent).indentValuesBy(keyIndent).padArrayDelimitersBy(arrayPadding).timeZone(TimeZone.getTimeZone(zoneOffset.getId()));
        if (fractionalSeconds) builder = builder.showFractionalSeconds();
        return builder.build();
    }
}
