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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.moandjiezana.toml.Toml;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.Types;
import ninja.leaping.configurate.loader.AbstractConfigurationLoader;
import ninja.leaping.configurate.loader.CommentHandler;
import ninja.leaping.configurate.loader.CommentHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A loader for the TOML language, using toml4j.
 */
public class TOMLConfigurationLoader extends AbstractConfigurationLoader<ConfigurationNode> {
    private final TOMLOptions opts;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractConfigurationLoader.Builder<Builder> {
        private TOMLOptions opts;

        public Builder setTomlOptions(TOMLOptions opts) {
            this.opts = opts;
            return this;
        }

        @Override
        public TOMLConfigurationLoader build() {
            return new TOMLConfigurationLoader(this);
        }
    }
    private TOMLConfigurationLoader(Builder builder) {
        super(builder, new CommentHandler[]{CommentHandlers.HASH});
        opts = builder.opts;
    }
    @Override
    protected void loadInternal(ConfigurationNode node, BufferedReader reader) throws IOException {
        Toml toml = new Toml().read(reader);
        Map<String, Object> map = toml.toMap();
        fixToml(node, map);
    }

    @SuppressWarnings("unchecked")
    void fixToml(ConfigurationNode node, Map<String, Object> input) {
        input.forEach((str, obj) -> {
            String str2 = str.replace("\"","");
            ConfigurationNode subnode = node.getNode(str2);
            if (obj instanceof Map) {
                fixToml(subnode, (Map<String, Object>) obj);
            } else if (obj instanceof List) {
                List list = (List) obj;
                list.forEach(el -> {
                    ConfigurationNode listNode = subnode.getAppendedNode();
                    if (el instanceof Map) {
                        fixToml(listNode, (Map<String, Object>) el);
                    } else {
                        listNode.setValue(el);
                    }
                });
            } else {
                subnode.setValue(obj);
            }
        });
    }

    Map<String, Object> breakToml(ConfigurationNode input) {
        Map<String, Object> map = Maps.newHashMap();
        input.getChildrenMap().forEach((obj, node) -> {
            if (node.hasListChildren()) {
                List<Object> list = Lists.newArrayList();
                node.getChildrenList().forEach(listNode -> {
                    if (listNode.hasMapChildren()) {
                        list.add(breakToml(listNode));
                    } else {
                        list.add(listNode.getValue());
                    }
                });
                map.put(obj.toString(), list);
            } else if (node.hasMapChildren()) {
                map.put(obj.toString(), breakToml(node));
            } else if (node.getValue() instanceof Instant) {
                map.put(obj.toString(), Types.asDate(node.getValue()));
            } else {
                map.put(obj.toString(), node.getValue());
            }
        });
        return map;
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
        Map<String, Object> map = breakToml(node);
        opts.toTomlWriter().write(map, writer);
    }

    @Override
    public ConfigurationNode createEmptyNode(ConfigurationOptions options) {
        options = options.setAcceptedTypes(ImmutableSet.of(List.class, Map.class, Double.class, Instant.class, Float.class, Integer.class, Boolean.class, String.class, Long.class, Date.class));
        return SimpleConfigurationNode.root(options);
    }
}
