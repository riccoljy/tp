package seedu.address.logic.commands.wedding;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEDDING;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WEDDINGS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Person;
import seedu.address.model.wedding.Wedding;
import seedu.address.model.wedding.WeddingName;


/**
 * Edits the details of an existing wedding in the address book.
 */
public class EditWeddingCommand extends Command {

    public static final String COMMAND_WORD = "edit-wedding";

    public static final String COMMAND_KEYWORD = "ew";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the wedding identified "
            + "by the index number used in the displayed wedding list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters (optional parameters in square brackets): INDEX (must be a positive integer) "
            + "[" + PREFIX_WEDDING + "WEDDING]"
            + "[" + PREFIX_ADDRESS + "ADDRESS]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_WEDDING + "Mr and Mrs John Tan "
            + PREFIX_ADDRESS + "12 College Ave West";

    public static final String MESSAGE_EDIT_WEDDING_SUCCESS = "Edited Wedding: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_WEDDING = "This wedding already exists in the address book.";

    private final Index index;
    private final EditWeddingDescriptor editWeddingDescriptor;

    /**
     * @param index of the wedding in the filtered wedding list to edit
     * @param editWeddingDescriptor details to edit the wedding with
     */
    public EditWeddingCommand(Index index, EditWeddingDescriptor editWeddingDescriptor) {
        requireNonNull(index);
        requireNonNull(editWeddingDescriptor);

        this.index = index;
        this.editWeddingDescriptor = new EditWeddingDescriptor(editWeddingDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Wedding> lastShownList = model.getFilteredWeddingList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WEDDING_DISPLAYED_INDEX);
        }
        Wedding weddingToEdit = lastShownList.get(index.getZeroBased());

        editWeddingDescriptor.setPeopleCount(weddingToEdit.getPeopleCount());
        editWeddingDescriptor.setGuestList(weddingToEdit.getGuestList());
        Wedding editedWedding = createEditedWedding(weddingToEdit, editWeddingDescriptor);

        if (!weddingToEdit.isSameWedding(editedWedding) && model.hasWedding(editedWedding)) {
            throw new CommandException(MESSAGE_DUPLICATE_WEDDING);
        }

        Person partner1 = weddingToEdit.getPartner1();
        if (partner1 != null) {
            Person editedPerson = partner1.clone();
            editedPerson.removeWedding(weddingToEdit);
            editedPerson.addWedding(editedWedding);
            model.setPerson(partner1, editedPerson);
        }

        Person partner2 = weddingToEdit.getPartner2();
        if (partner2 != null) {
            Person editedPerson = partner2.clone();
            editedPerson.removeWedding(weddingToEdit);
            editedPerson.addWedding(editedWedding);
            model.setPerson(partner2, editedPerson);
        }

        for (Person guest : weddingToEdit.getGuestList()) {
            Person editedPerson = guest.clone();
            editedPerson.removeWedding(weddingToEdit);
            editedPerson.addWedding(editedWedding);
            model.setPerson(guest, editedPerson);
        }

        model.setWedding(weddingToEdit, editedWedding);
        model.updateFilteredWeddingList(PREDICATE_SHOW_ALL_WEDDINGS);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_WEDDING_SUCCESS, Messages.format(editedWedding)));
    }

    /**
     * Creates and returns a {@code Wedding} with the details of {@code weddingToEdit}
     * edited with {@code editWeddingDescriptor}.
     */
    private static Wedding createEditedWedding(Wedding weddingToEdit, EditWeddingDescriptor editWeddingDescriptor) {
        assert weddingToEdit != null;

        WeddingName updatedWeddingName = editWeddingDescriptor.getWeddingName().orElse(weddingToEdit.getWeddingName());
        int peopleCount = editWeddingDescriptor.getPeopleCount().orElse(0);
        ArrayList<Person> guestlist = editWeddingDescriptor.getGuestList().orElse(weddingToEdit.getGuestList());
        Address updatedAddress = editWeddingDescriptor.getAddress().orElse(weddingToEdit.getAddress());
        String date = editWeddingDescriptor.getDate().orElse(weddingToEdit.getDate());

        return new Wedding(updatedWeddingName, peopleCount, null, null, guestlist, updatedAddress, date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditWeddingCommand otherEditWeddingCommand)) {
            return false;
        }

        return index.equals(otherEditWeddingCommand.index)
                && editWeddingDescriptor.equals(otherEditWeddingCommand.editWeddingDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editWeddingDescriptor", editWeddingDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the wedding with. Each non-empty field value will replace the
     * corresponding field value of the wedding.
     */
    public static class EditWeddingDescriptor {
        private WeddingName weddingName;
        private int peopleCount = -1; //if -1, means no change
        private ArrayList<Person> guestList;
        private Address address;
        private String date;

        public EditWeddingDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditWeddingDescriptor(EditWeddingDescriptor toCopy) {
            setWeddingName(toCopy.weddingName);
            setAddress(toCopy.address);
            setDate(toCopy.date);
            setPeopleCount(toCopy.peopleCount);
            setGuestList(toCopy.guestList);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(weddingName, address, date)
                    || this.peopleCount != -1;
        }

        public void setWeddingName(WeddingName weddingName) {
            this.weddingName = weddingName;
        }

        public Optional<WeddingName> getWeddingName() {
            return Optional.ofNullable(weddingName);
        }

        public Optional<Integer> getPeopleCount() {
            return Optional.of(peopleCount); //doesn't need ofNullable because init count = 0
        }

        public void setPeopleCount(int peopleCount) {
            this.peopleCount = peopleCount;
        }

        public Optional<ArrayList<Person>> getGuestList() {
            return Optional.ofNullable(guestList);
        }

        public void setGuestList(ArrayList<Person> guestList) {
            this.guestList = guestList;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Optional<String> getDate() {
            return Optional.ofNullable(date);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditWeddingDescriptor otherEditWeddingDescriptor)) {
                return false;
            }

            return Objects.equals(weddingName, otherEditWeddingDescriptor.weddingName)
                    && Objects.equals(peopleCount, otherEditWeddingDescriptor.peopleCount)
                    && Objects.equals(address, otherEditWeddingDescriptor.address)
                    && Objects.equals(date, otherEditWeddingDescriptor.date);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("weddingName", weddingName)
                    .add("peopleCount", peopleCount)
                    .add("address", address)
                    .add("date", date)
                    .toString();
        }
    }
}
