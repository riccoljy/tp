package seedu.address.logic.commands.wedding;

import static seedu.address.logic.Messages.MESSAGE_ADD_WEDDING_SUCCESS;
import static seedu.address.logic.Messages.MESSAGE_FORCE_ASSIGN_WEDDING_TO_CONTACT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_WEDDING_NOT_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEDDING;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.wedding.Wedding;

/**
 * Adds a wedding to an existing person in the Wedlinker.
 */
public class AssignWeddingCommand extends Command {

    public static final String COMMAND_WORD = "assign-wedding";

    public static final String COMMAND_KEYWORD = "asw";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds one or multiple weddings to the person identified "
            + "by the index number used in the last person listing.\n"
            + "Wedding names are case sensitive.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_WEDDING + "WEDDING... (can specify multiple weddings)\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_WEDDING + "Craig's Wedding " + PREFIX_WEDDING + "Wedding April 2025.";

    private final Index index;
    private final Map<Wedding, String> weddingsToAdd;
    private final boolean force;

    /**
     * Constructs a {@code AssignWedding} Command to add weddings to a person with the force flag.
     * @param index The index of the person in the person list.
     * @param weddingsToAdd The list of weddings to be added.
     * @param force Whether the command should force the assignment by creating the Wedding object.
     */
    public AssignWeddingCommand(Index index, Map<Wedding, String> weddingsToAdd, boolean force) {
        this.index = index;
        this.weddingsToAdd = weddingsToAdd;
        this.force = force;
    }

    /**
     * Generates a command execution success message showing the added weddings and the person.
     *
     * @param personToEdit The person to whom the weddings were added.
     * @return A success message indicating the weddings that were added and the name of the person.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String addedWeddings = weddingsToAdd.keySet().stream()
                .map(wedding -> wedding.toString().replaceAll("[\\[\\]]", ""))
                .collect(Collectors.joining(", "));
        return String.format(MESSAGE_ADD_WEDDING_SUCCESS, addedWeddings, personToEdit.getName().toString());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Set<Wedding> updatedWeddings = new HashSet<>(personToEdit.getWeddings());
        updatedWeddings.addAll(weddingsToAdd.keySet());

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                updatedWeddings,
                personToEdit.getTasks());

        for (Map.Entry<Wedding, String> entry : weddingsToAdd.entrySet()) {
            Wedding wedding = entry.getKey();
            if (!model.hasWedding(wedding)) {
                if (this.force) {
                    CreateWeddingCommand newWeddingCommand = new CreateWeddingCommand(wedding);
                    newWeddingCommand.execute(model);
                } else {
                    throw new CommandException(
                            MESSAGE_WEDDING_NOT_FOUND + "\n" + MESSAGE_FORCE_ASSIGN_WEDDING_TO_CONTACT);
                }
            }
            Wedding editedWedding = wedding.clone();
            String type = entry.getValue();
            switch (type) {
            case "p1" -> editedWedding.setPartner1(editedPerson);
            case "p2" -> editedWedding.setPartner2(editedPerson);
            //case "g" -> editedWedding.addToGuestList(personToEdit);
            default -> editedWedding.addToGuestList(editedPerson);
            }
            wedding.increasePeopleCount();
            model.setWedding(wedding, editedWedding);
        }
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AssignWeddingCommand otherCommand)) {
            return false;
        }

        return index.equals(otherCommand.index) && weddingsToAdd.keySet()
                .equals(otherCommand.weddingsToAdd.keySet())
                && this.force == otherCommand.force;
    }
}
