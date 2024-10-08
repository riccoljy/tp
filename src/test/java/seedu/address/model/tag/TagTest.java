package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(new TagName(invalidTagName)));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void noPersonsTaggedCheck() {
        Tag florist = new Tag(new TagName("Florist"));
        assertEquals(0, florist.getNumberOfPersonsTagged());
    }

    @Test
    public void incrementTaggedCount() {
        Tag florist = new Tag(new TagName("Florist"));
        florist.increaseTaggedCount();
        assertEquals(1, florist.getNumberOfPersonsTagged());
    }

    @Test
    public void decrementTaggedCount() {
        Tag florist = new Tag(new TagName("Florist"));;
        florist.increaseTaggedCount();
        florist.decreaseTaggedCount();
        assertEquals(0, florist.getNumberOfPersonsTagged());
    }

    @Test
    public void canBeDeleted() {
        Tag florist = new Tag(new TagName("Florist"));
        assertTrue(florist.canBeDeleted());
    }
}
