package seedu.address.logic.commands.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTags.FLORIST;
import static seedu.address.testutil.TypicalTags.PHOTOGRAPHER;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

public class DeleteTagCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDeleteTagCommand_success() {
        Tag tagToDelete = model.getFilteredTagList().get(0);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(tagToDelete);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS,
                Messages.format(tagToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTag(tagToDelete);
        assertCommandSuccess(deleteTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validDeleteTagCommandStillInUse_failure() {
        Tag tagToDelete = model.getFilteredTagList().get(3);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(tagToDelete);
        String expectedMessage = String.format(
                DeleteTagCommand.MESSAGE_DELETE_TAG_FAILURE_STILL_TAGGED, Messages.format(tagToDelete))
                + "\n"
                + Messages.MESSAGE_FORCE_DELETE_TAG;
        assertCommandFailure(deleteTagCommand, model, expectedMessage);
    }

    @Test
    public void execute_validDeleteTagCommandStillInUseForce_success() {
        Tag tagToDelete = model.getFilteredTagList().get(3);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(tagToDelete, true);
        String expectedMessage = String.format(
                DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS, Messages.format(tagToDelete));
        assertCommandSuccess(deleteTagCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_invalidNotFoundDeleteTagCommand() {
        Tag tagToDelete = model.getFilteredTagList().get(0);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_DELETE_TAG_FAILURE_NOT_FOUND,
                Messages.format(tagToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTag(tagToDelete);

        DeleteTagCommand expectedDeleteTagCommand = new DeleteTagCommand(tagToDelete);

        assertCommandFailure(expectedDeleteTagCommand, expectedModel, expectedMessage);
    }
    @Test
    public void equals() {
        DeleteTagCommand deleteFloristTagCommand = new DeleteTagCommand(FLORIST);
        DeleteTagCommand deletePhotographerTagCommandCopy = new DeleteTagCommand(PHOTOGRAPHER);

        // same object -> returns true
        assertEquals(deleteFloristTagCommand, deleteFloristTagCommand);

        // same values -> returns true
        DeleteTagCommand deleteFloristTagCommandCopy = new DeleteTagCommand(FLORIST);
        assertEquals(deleteFloristTagCommand, deleteFloristTagCommandCopy);

        // null -> returns false
        assertNotEquals(null, deleteFloristTagCommand);

        // different tag -> returns false
        assertNotEquals(deleteFloristTagCommand, deletePhotographerTagCommandCopy);
    }

    @Test
    public void testToString() {
        // Create the CreateTagCommand with the tag
        DeleteTagCommand command = new DeleteTagCommand(FLORIST);

        // Verify the string representation of the command
        String expectedString = "seedu.address.logic.commands.tag.DeleteTagCommand{targetTag=[florist]}";
        assertEquals(expectedString, command.toString());
    }
}
