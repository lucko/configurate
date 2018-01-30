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
package ninja.leaping.configurate.xml;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.math.DoubleMath;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.attributed.AttributedConfigurationNode;
import ninja.leaping.configurate.attributed.SimpleAttributedConfigurationNode;
import ninja.leaping.configurate.loader.AbstractConfigurationLoader;
import ninja.leaping.configurate.loader.CommentHandler;
import ninja.leaping.configurate.loader.CommentHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;

/**
 * A loader for XML (Extensible Markup Language)
 */
public class XMLConfigurationLoader extends AbstractConfigurationLoader<AttributedConfigurationNode> {
    private static final String INDENT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";

    private final Schema schema;
    private final String defaultTagName;
    private final int indent;

    public static class Builder extends AbstractConfigurationLoader.Builder<Builder> {
        private Schema schema = null;
        private String defaultTagName = "element";
        private int indent = 2;

        protected Builder() {
        }

        public Schema getSchema() {
            return schema;
        }

        public Builder setSchema(Schema schema) {
            this.schema = schema;
            return this;
        }

        public String getDefaultTagName() {
            return defaultTagName;
        }

        public Builder setDefaultTagName(String defaultTagName) {
            this.defaultTagName = defaultTagName;
            return this;
        }

        public int getIndent() {
            return indent;
        }

        public Builder setIndent(int indent) {
            this.indent = indent;
            return this;
        }

        @Override
        public XMLConfigurationLoader build() {
            return new XMLConfigurationLoader(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private XMLConfigurationLoader(Builder build) {
        super(build, new CommentHandler[] {CommentHandlers.HASH, CommentHandlers.DOUBLE_SLASH});
        this.schema = build.getSchema();
        this.defaultTagName = build.getDefaultTagName();
        this.indent = build.getIndent();
    }

    private DocumentBuilder newDocumentBuilder() {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        if (schema != null) {
            builderFactory.setSchema(schema);
        }

        try {
            return builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private Transformer newTransformer() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            if (indent > 0) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(INDENT_PROPERTY, Integer.toString(indent));
            }
            return transformer;
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadInternal(AttributedConfigurationNode node, BufferedReader reader) throws IOException {
        DocumentBuilder documentBuilder = newDocumentBuilder();

        Document document;
        try {
            document = documentBuilder.parse(new InputSource(reader));
        } catch (SAXException e) {
            throw new IOException(e);
        }

        Element root = document.getDocumentElement();
        readElement(root, node);
    }

    private void readElement(Node from, AttributedConfigurationNode to) {
        to.setTagName(from.getNodeName());
        if (from.hasAttributes()) {
            NamedNodeMap attributes = from.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                to.addAttribute(attribute.getNodeName(), attribute.getNodeValue());
            }
        }

        if (from.hasChildNodes()) {
            NodeList childNodes = from.getChildNodes();
            Multimap<String, Node> nested = MultimapBuilder.linkedHashKeys().arrayListValues().build();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    nested.put(child.getNodeName(), child);
                }
            }

            if (!nested.isEmpty()) {
                // read to a map only if there are no duplicate keys
                boolean map = nested.keys().size() == nested.keySet().size();
                for (Map.Entry<String, Node> entry : nested.entries()) {
                    readElement(entry.getValue(), map ? to.getNode(entry.getKey()) : to.getAppendedNode());
                }
                return;
            }
        }

        to.setValue(parseValue(from.getTextContent()));
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
        DocumentBuilder documentBuilder = newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        document.appendChild(writeNode(document, node, null));

        Transformer transformer = newTransformer();
        DOMSource source = new DOMSource(document);
        try {
            transformer.transform(source, new StreamResult(writer));
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }

    private Element writeNode(Document document, ConfigurationNode node, String forcedTag) {
        String tag = defaultTagName;
        Map<String, String> attributes = ImmutableMap.of();

        if (node instanceof AttributedConfigurationNode) {
            AttributedConfigurationNode attributedNode = ((AttributedConfigurationNode) node);
            tag = attributedNode.getTagName();
            attributes = attributedNode.getAttributes();
        }

        Element element = document.createElement(forcedTag == null ? tag : forcedTag);
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            element.setAttribute(attribute.getKey(), attribute.getValue());
        }

        if (node.hasMapChildren()) {
            for (Map.Entry<Object, ? extends ConfigurationNode> child : node.getChildrenMap().entrySet()) {
                element.appendChild(writeNode(document, child.getValue(), child.getKey().toString()));
            }
        } else if (node.hasListChildren()) {
            for (ConfigurationNode child : node.getChildrenList()) {
                element.appendChild(writeNode(document, child, null));
            }
        } else {
            element.appendChild(document.createTextNode(node.getValue().toString()));
        }

        return element;
    }

    @Override
    public AttributedConfigurationNode createEmptyNode(ConfigurationOptions options) {
        options = options.setAcceptedTypes(ImmutableSet.of(Double.class, Long.class,
                Integer.class, Boolean.class, String.class, Number.class));
        return SimpleAttributedConfigurationNode.root("root", options);
    }

    private static Object parseValue(String value) {
        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        }

        try {
            double doubleValue = Double.parseDouble(value);
            if (DoubleMath.isMathematicalInteger(doubleValue)) {
                long longValue = (long) doubleValue;
                int intValue = (int) longValue;
                if (longValue == intValue) {
                    return intValue;
                } else {
                    return longValue;
                }
            }
            return doubleValue;
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
