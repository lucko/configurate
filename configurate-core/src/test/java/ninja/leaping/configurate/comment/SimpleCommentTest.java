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
package ninja.leaping.configurate.comment;

import org.junit.jupiter.api.Test;

import ninja.leaping.configurate.component.comment.Comment;
import ninja.leaping.configurate.component.comment.SimpleComment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SimpleCommentTest {

    @Test
    public void testCommentsTransferred() {
        Comment subject = SimpleComment.root();
        Comment firstChild = subject.getNode("first");
        firstChild.setValue("test value");
        firstChild.setComment("Such comment. Very wow.");


        Comment secondChild = subject.getNode("second");
        secondChild.setValue("test value's evil twin");

        assertFalse(secondChild.isVirtual());

        secondChild.setValue(firstChild);
        assertEquals("test value", secondChild.getValue());
        assertEquals("Such comment. Very wow.", secondChild.getComment().orElse(null));
    }

    @Test
    public void testNestedCommentsTransferred() {
        Comment subject = SimpleComment.root();
        Comment firstChild = subject.getNode("first");
        Comment firstChildChild = firstChild.getNode("child");
        firstChildChild.setValue("test value");
        firstChildChild.setComment("Such comment. Very wow.");


        Comment secondChild = subject.getNode("second");
        secondChild.setValue("test value's evil twin");

        assertFalse(secondChild.isVirtual());

        secondChild.setValue(firstChild);
        assertEquals("test value", secondChild.getNode("child").getValue());
        assertEquals("Such comment. Very wow.", secondChild.getNode("child").getComment().orElse(null));
    }

    @Test
    public void testCommentsMerged() {
        Comment source = SimpleComment.root();
        Comment target = SimpleComment.root();

        source.getNode("no-value").setValue("a").setComment("yeah");
        source.getNode("existing-value-no-comment").setValue("orig").setComment("maybe");
        source.getNode("existing-value").setValue("a").setComment("yeah");
        source.getNode("no-parent", "child").setValue("x").setComment("always");
        target.getNode("existing-value-no-comment").setValue("new");
        target.getNode("existing-value").setValue("b").setComment("nope");

        target.mergeValuesFrom(source);
        assertEquals("yeah", target.getNode("no-value").getComment().orElse(null));
        assertEquals("maybe", target.getNode("existing-value-no-comment").getComment().orElse(null));
        assertEquals("new", target.getNode("existing-value-no-comment").getString());
        assertEquals("nope", target.getNode("existing-value").getComment().orElse(null));
        assertEquals("always", target.getNode("no-parent", "child").getComment().orElse(null));
    }
}
