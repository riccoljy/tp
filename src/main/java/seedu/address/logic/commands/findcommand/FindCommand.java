package seedu.address.logic.commands.findcommand;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.person.TraitContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive and allows partial matching.
 */
public abstract class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons based on the specified keywords "
            + "(case-insensitive) after the prefix representing the field, "
            + "and displays them as a list with index numbers.\n"
            + "Use 'n/' to search by name, 'a/' to search by address etc. \n"
            + "Parameters: PREFIX/ KEYWORDS [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + " alice charlie";

    public static final String MESSAGE_FIND_PERSON_SUCCESS = "Search for \"%s\" was successful. Showing results:";

    public static final String MESSAGE_FIND_PERSON_UNSUCCESSFUL = "No contacts found.";

    protected final TraitContainsKeywordsPredicate<?> predicate;

    public FindCommand(TraitContainsKeywordsPredicate<?> predicate) {
        this.predicate = predicate;
    }

    @Override
    public abstract CommandResult execute(Model model);

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }

        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}