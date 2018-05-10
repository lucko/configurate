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
package ninja.leaping.configurate.xml;

import com.google.common.io.Resources;
import ninja.leaping.configurate.attributed.AttributedConfigurationNode;
import ninja.leaping.configurate.loader.AtomicFiles;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

/**
 * Basic sanity checks for the loader
 */
public class XMLConfigurationLoaderTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testSimpleLoading() throws IOException {
        URL url = getClass().getResource("/example.xml");
        final Path saveTest = folder.newFile().toPath();

        XMLConfigurationLoader loader = XMLConfigurationLoader.builder()
                .setWriteExplicitType(false)
                .setIndent(4)
                .setSource(() -> new BufferedReader(new InputStreamReader(url.openStream(), UTF_8)))
                .setSink(AtomicFiles.createAtomicWriterFactory(saveTest, UTF_8)).build();

        AttributedConfigurationNode node = loader.load();

        assertEquals("messages", node.getTagName());
        assertEquals("false", node.getAttribute("secret"));
        assertTrue(node.hasListChildren());

        List<? extends AttributedConfigurationNode> notes = node.getChildrenList();
        assertEquals(2, notes.size());

        AttributedConfigurationNode firstNote = notes.get(0);
        assertEquals("501", firstNote.getAttribute("id"));
        assertTrue(firstNote.hasMapChildren());
        assertFalse(firstNote.hasListChildren());

        Map<Object, ? extends AttributedConfigurationNode> properties = firstNote.getChildrenMap();
        assertEquals("Tove", properties.get("to").getValue());
        assertEquals("Jani", properties.get("from").getValue());
        assertEquals("Don't forget me this weekend!", properties.get("body").getValue());
        assertEquals("heading", properties.get("heading").getTagName());

        AttributedConfigurationNode secondNode = notes.get(1);
        assertEquals("502", secondNode.getAttribute("id"));
        assertFalse(secondNode.hasMapChildren());
        assertTrue(secondNode.hasListChildren());

        List<? extends AttributedConfigurationNode> subNodes = secondNode.getChildrenList();
        for (AttributedConfigurationNode subNode : subNodes) {
            if (subNode.getTagName().equals("heading")) {
                assertEquals("true", subNode.getAttribute("bold"));
            }
        }

        // roundtrip!
        loader.save(node);
        assertEquals(Resources.readLines(url, UTF_8), Files.readAllLines(saveTest));
    }

    @Test
    public void testExplicitTypes() throws IOException {
        URL url = getClass().getResource("/example2.xml");
        final Path saveTest = folder.newFile().toPath();

        XMLConfigurationLoader loader = XMLConfigurationLoader.builder()
                .setWriteExplicitType(true)
                .setIndent(4)
                .setSource(() -> new BufferedReader(new InputStreamReader(url.openStream(), UTF_8)))
                .setSink(AtomicFiles.createAtomicWriterFactory(saveTest, UTF_8)).build();

        AttributedConfigurationNode node = loader.load();

        AttributedConfigurationNode list1 = node.getNode("list1");
        assertTrue(list1.hasListChildren());

        AttributedConfigurationNode list2 = node.getNode("list2");
        assertTrue(list2.hasListChildren());

        AttributedConfigurationNode map1 = node.getNode("map1");
        assertTrue(map1.hasMapChildren());

        AttributedConfigurationNode map2 = node.getNode("map2");
        assertTrue(map2.hasMapChildren());

        // roundtrip!
        loader.save(node);
        assertEquals(Resources.readLines(url, UTF_8), Files.readAllLines(saveTest));
    }
}
