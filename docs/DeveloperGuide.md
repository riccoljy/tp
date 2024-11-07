---
    layout: default.md
      title: "Developer Guide"
      pageNav: 3
---

# WedLinker Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

WedLinker is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture
The ***Architecture Diagram*** given below explains the high-level design of the App, giving a quick overview of main
components and how they interact with each other.
<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

**Main components of the architecture**
The bulk of the app's work is done by the `UI`, `Logic`, `Model`, and `Storage` components.

* **`Main`**:
    * Consists of classes 
<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/Main.java" style="text-decoration: underline;">`Main`</a>
<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/MainApp.java" style="text-decoration: underline;">`MainApp`</a>
    * Responsible for app launch and shutdown.
    * At launch, it initializes the other components in the correct sequence and connects them.
    * At shutdown, it terminates the components and invokes cleanup methods where necessary.

* <a href="#ui-component" style="text-decoration: underline;"><strong><code>UI</code></strong></a>:
    * The interface of the app (WedLinker) that the user primarily interacts with.
    * Displays relevant information (e.g., wedding details) from `Model`.
    * Receives commands from user input.

* <a href="#logic-component" style="text-decoration: underline;"><strong><code>Logic</code></strong></a>:
    * The command executor.

* <a href="#model-component" style="text-decoration: underline;"><strong><code>Model</code></strong></a>:
    * Holds the data of the app in memory, which can be altered by commands.
    * Some data is displayed to the user via the UI.

* <a href="#storage-component" style="text-decoration: underline;"><strong><code>Storage</code></strong></a>:
    * Reads data from, and writes data to, the locally stored `data/addressbook.json` file.

* <a href="#common-classes" style="text-decoration: underline;"><strong><code>Commons</code></strong></a>:
    * Represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as `{Component}`.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI Component
(<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/ui/Ui.java" style="text-decoration: underline;"><strong><code>Ui.java</strong></code></a>)

The `UI` component handles interactions between the user and the app's graphical interface, leveraging JavaFX to define and display various UI elements.

1. **Structure**:
    * The `UI` consists of a `MainWindow` composed of several parts like `CommandBox`, `StatusBarFooter`, `ResultDisplay`, and various `*ListPanel`s.
    * Each UI part, including `MainWindow`, inherits from the abstract class
      (<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/ui/UiPart.java#L15" style="text-decoration: underline;"><strong><code>UiPart</strong></code></a>),
   capturing commonalities among visible GUI components.

2. **Class Diagram**:
    * Diagram showing the `UI` structure:

      <puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

3. **JavaFX and Layout Files**:
    * The layout of each UI part is defined using `.fxml` files in the
      <a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/resources/view" style="text-decoration: underline;"><strong><code>src/main/resources/view</strong></code></a>,
    * For example, the structure of
      <a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java" style="text-decoration: underline;"><strong><code>MainWindow</strong></code></a>,
   is specified in
      <a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/resources/view/MainWindow.fxml" style="text-decoration: underline;"><strong><code>MainWindow.fxml</strong></code></a>,

4. **Responsibilities**:
    * Executes user commands by interfacing with the `Logic` component.
    * Listens for updates to `Model` data to refresh the UI with modified information.
    * Maintains a reference to `Logic`, relying on it for command execution.
    * Accesses specific `Model` classes to display `Person` objects managed within `Model`.

### Logic Component
(<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/logic/Logic.java" style="text-decoration: underline;"><strong><code>Logic.java</strong></code></a>)


The `Logic` component processes user inputs passed through the UI, utilizing a series of parsers to generate `Command` instances, which are then executed. Here’s an overview:

1. **User Input Processing**:
    * User input is received from the UI and passed to `Logic`.
    * The input is parsed by `AddressBookParser`, which delegates to the appropriate parser (e.g., `DeleteCommandParser`), creating the relevant `Command` instance.
    * `LogicManager` executes the `Command`, interacting with the `Model` to make necessary updates (e.g., deleting a person).
    * Execution results are encapsulated in a `CommandResult` and returned to `Logic`, which relays the outcome back to the UI.

2. **Class Diagram**:
    * Partial diagram of the `Logic` component:

      <puml src="diagrams/LogicClassDiagram.puml" width="550"/>

3. **Sequence Diagram**:
    * Example interaction in the `Logic` component for `execute("delete 1")` (from user inputting delete 1 on UI):

      <puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

      <box type="info" seamless>
      **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a PlantUML limitation, it continues to the end of the diagram.
      </box>

4. **Parser Classes**:
    * Other classes in `Logic` handle command parsing:

      <puml src="diagrams/ParserClasses.puml" width="600"/>

    * Parsing flow:
        * `AddressBookParser` identifies the specific command parser needed (e.g., `AddCommandParser`, `DeleteCommandParser`).
        * Each `XYZCommandParser` (e.g., `AddCommandParser`) implements the `Parser` interface, allowing for consistent handling and testing.
        * The parser processes the user command, creating the relevant `Command` object (e.g., `AddCommand`), which `AddressBookParser` returns to `Logic`.

### Model Component
(<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/model/Model.java" style="text-decoration: underline;"><strong><code>Model.java</strong></code></a>)


The `Model` component handles the data and state management for the app, storing all entities and providing an interface for accessing and updating them.

1. **Structure**:
    * The `Model` stores various types of data:
        * `Person` objects within a `UniquePersonList`.
        * `Wedding` objects within a `UniqueWeddingList`.
        * `Task` objects within a `UniqueTaskList`.

2. **Class Diagram**:
    * Diagram of the `Model` structure:

      <puml src="diagrams/ModelClassDiagram.puml" width="700" />

3. **Responsibilities**:
    * **Data Storage**:
        * Maintains address book data, including lists of `Person`, `Wedding`, and `Task` objects.
    * **Filtered Views**:
        * Provides filtered lists of `Person`, `Wedding`, and `Task` objects (e.g., search results), exposing these as unmodifiable `ObservableList`s. This allows the UI to automatically update in response to changes.
    * **User Preferences**:
        * Stores user preferences in a `UserPref` object, which is accessible externally as `ReadOnlyUserPref`.
    * **Independence**:
        * The `Model` does not rely on other components, as it represents the app’s core data entities, which should be logically self-contained.


### Storage component
(<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/storage/Storage.java" style="text-decoration: underline;"><strong><code>Storage.java</strong></code></a>)

1. **Functionality**:
    * Supports saving and loading both address book data and user preferences as JSON files.
    * Reads JSON data into corresponding in-memory objects for the app's use.

2. **Inheritance**:
    * Inherits from `AddressBookStorage` and `UserPrefStorage`, allowing it to be used interchangeably with either interface, depending on the functionality needed.

3. **Dependencies**:
    * Depends on classes within the `Model` component, as `Storage` is tasked with saving and retrieving `Model`-related objects.

4. **Class Diagram**:
    * Diagram illustrating `Storage` structure:

      <puml src="diagrams/StorageClassDiagram.puml" width="900" />
    
### Common classes

Classes used by multiple components are in the 
<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/commons" style="text-decoration: underline;"><strong><code>seedu.address.commons</strong></code></a>
package.

Notable classes include `StringUtil` and `ToStringBuilder` which help to perform operations like checking whether a string
includes non-zero unsigned integer, or whether there is a partial match for a word. These can be found in
<a href="https://github.com/AY2425S1-CS2103T-F15-4/tp/tree/master/src/main/java/seedu/address/commons/util" style="text-decoration: underline;"><strong><code>seedu.address.commons.util</strong></code></a>.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Professional Wedding Planners

* has a need to manage a significant number of contacts
* has a need to manage multiple weddings happening at differing times
* has a need to manage contacts across multiple weddings
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: compilation of all contacts necessary in weddings in one location making management easier


### User stories

Priorities:
High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

Those without any stars are user stories that were considered but will not be implemented at this time.

| Priority | As a …​                   | I want to …​                                                                                                         | So that…​                                                                                                                                                           |
|:--------:|---------------------------|----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `* * *`  | user                      | add tag(s) to each contact based on created tags such as florist, musician etc.                                      | I can easily understand the group this person belongs to.                                                                                                           |
| `* * *`  | user                      | add a phone number associated with each contact                                                                      | I can easily find the contact information for each contact.                                                                                                         |
| `* * *`  | user                      | add the address associated with each contact                                                                         | I can easily find the address of each contact.                                                                                                                      |
| `* * *`  | user                      | filter contacts by tag                                                                                               | I can quickly see all the groups under the same tag, and find the right vendor based on the type of services provided.                                              |
| `* * *`  | user                      | add new contacts into WedLinker                                                                                      | I can store the contact details of new contacts.                                                                                                                    |
| `* * *`  | user                      | delete contacts that are no longer needed                                                                            | I can remove unnecessary contacts and have a more organised address book.                                                                                           |
| `* * *`  | user                      | search for contact by name                                                                                           | I can find specific contacts that I am looking for.                                                                                                                 |
| `* * *`  | user                      | create tags                                                                                                          | I can have special categories for non traditional vendors.                                                                                                          |
|  `* *`   | user                      | edit information such as the contact number and address of each contact                                              | all contacts have the most updated information.                                                                                                                     |
|  `* *`   | user                      | clear all the contacts in the system                                                                                 | I can clear all my contacts quickly without having to individually delete them if I want to add in a completely new set of contacts.                                |
|  `* *`   | careless user             | receive a prompt that requires me to key in a confirmation that I want to delete a contact or clear the address book | I will not lose all my contacts when I accidentally type delete/ clear.                                                                                             |
|  `* *`   | user                      | assign each guest contact its dietary requirements status                                                            | I can track the dietary requirement of each guest.                                                                                                                  |
|  `* *`   | user                      | sort contacts by alphabetical order                                                                                  | I can easily find the contacts required in a large address book.                                                                                                    |
|  `* *`   | user                      | assign additional information for each contact                                                                       | I can include important notes that may not fit into other categories, such as reminders for what the contact might need.                                            |
|  `* *`   | first-time user           | see some sample contacts already available in the app                                                                | I can try out the different features without needing to add my own data (e.g allocating people to wedding, allocating task to contacts).                            |
|  `* *`   | careless, first-time user | reload the sample contacts into the app                                                                              | I can continue trying out different features without needing to add my own data in case I accidentally cleared the contacts.                                        |
|  `* *`   | first-time user           | see a help message showing all the commands/feature I can use                                                        | I can try out all the different features by referring to the message.                                                                                               |
|   `*`    | user                      | assign tasks to contacts                                                                                             | I can track which tasks have been assigned to each contact.                                                                                                         |
|   `*`    | user                      | update the status of tasks of contacts                                                                               | I can track the status of completion of the tasks assigned to contacts.                                                                                             |
|   `*`    | user                      | add a tag to each guest indicating their table number                                                                | track the table each guest is seated at.                                                                                                                            |
|   `*`    | user                      | key in the table number and get the list of guests seated at that table                                              | I can quickly identify all the groups seated at one table.                                                                                                          |
|   `*`    | user                      | assign a rating out of 5 to each vendor                                                                              | I can track the experience with this vendor for future reference.                                                                                                   |
|   `*`    | busy user                 | add multiple wedding events                                                                                          | I can track contacts for multiple weddings at once.                                                                                                                 |
|   `*`    | busy user                 | tag each contact to a wedding                                                                                        | I can easily see which contacts are relevant to which wedding.                                                                                                      |
|   `*`    | user                      | assign dates to a wedding                                                                                            | I can keep track of when different weddings are scheduled.                                                                                                          |
|   `*`    | user                      | filter contacts by wedding                                                                                           | I can keep track of which contacts are relevant for each wedding.                                                                                                   |
|   `*`    | user                      | send out (standardised formatted) information (text/email) from the application                                      | I can efficiently send out information without any mistakes.                                                                                                        |
|   `*`    | user                      | share the contact details to relevant third-parties for bookings (eg: venue bookings, suit/dress rental, etc.)       | I can easily send out all relevant information (including dietary restriction, and other tags) to all the third-parties.                                            | 
|   `*`    | user                      | exclude tags from search and filter                                                                                  | I can focus on contacts that are relevant to certain events or requirements without being overwhelmed by unnecessary information.                                   |
|   `*`    | busy user                 | autocomplete existing tags when user is inputting tag information                                                    | I can quickly assign roles for people that might be working with others I have already input into the system and not have to type the same roles in multiple times. |
|   `*`    | user                      | assign availability to vendors                                                                                       | I can check who will be available for a particular wedding.                                                                                                         |
|   `*`    | user                      | filter availability of vendors                                                                                       | I can easily find vendors that can cater to a wedding.                                                                                                              |
|   `*`    | user                      | store multiple contact methods                                                                                       | I can contact the vendors through different means.                                                                                                                  |
|   `*`    | user                      | re-assign tasks to another contact                                                                                   | I can account for vendors suddenly being unavailable.                                                                                                               |
|   `*`    | user                      | set reminders for tasks to different contacts                                                                        | I can easily track and follow up with clients and vendors for deliverables.                                                                                         |
|   `*`    | user                      | see a list of all tasks and reminders I have assigned to contacts in its own window                                  | I can quickly and easily see what my earliest priorities are and act on them quickly.                                                                               |
|   `*`    | user                      | see a calendar view of tasks, reminders, and weddings I have assigned                                                | I can see the whole timelines of my planned weddings and see how much time there is between tasks.                                                                  |
|   `*`    | user                      | set privacy setting for different contacts                                                                           | I can keep personal and sensitive information private when sharing address book.                                                                                    |
|   `*`    | forgetful user            | create links between different contacts, such as assigning a vendor to a bride or groom in a wedding                 | I can easily navigate from key stakeholders in the wedding that I remember better to vendors who I might not remember as well.                                      |
|   `*`    | user                      | add certain vendors as favorites                                                                                     | I can remember which vendors performed well and see if they are favorites.                                                                                          |
|   `*`    | user                      | access a list of all my favorite vendors                                                                             | I can easily check who the best vendors were that I previously engaged with.                                                                                        |
|          | user                      | generate a checklist of all the contacts for a particular wedding, grouped by roles                                  | I can keep track of who is meant to be present at the wedding.                                                                                                      |
|          | user                      | assign a time for each contact for when they are meant to arrive                                                     | I can easily keep track of which people are on time and check who to contact in case they have not arrived yet.                                                     |
|          | user                      | attach extra documents as a file to various contacts                                                                 | I can store all the information in one place, eg. Invoices from a vendor.                                                                                           |
|          | user                      | categorize tasks based on its nature (e.g. procurement, arrangement)                                                 | I can view tasks in a more organised manner.                                                                                                                        |

### Use cases

(For all use cases below, the **System** is the `WedLinker` and the **Actor** is the `user`, unless specified otherwise)

> Use Cases beginning with 'UC' cover core Wedlinker functionalities.
>
> Use Cases beginning with 'UCSH' cover non-core Wedlinker functionalities.
---
### **Use case: UC01 — List all Contacts**

**MSS**

1.  User requests to view all contacts.
2.  The system retrieves and displays the list of all contacts to the user.

    Use case ends.

### **Use case: UC02 — List all Weddings**
Similar to [<ins>UC01](#use-case-uc01-list-all-contacts) except to view weddings instead of contacts.

### **Use case: UC03 — List all Tasks**
Similar to [<ins>UC01](#use-case-uc01-list-all-contacts) except to view tasks instead of contacts.

---

### **Use case: UC04 — Create a Contact**

**MSS**

1.  User requests to create a contact with the corresponding details.
2.  The system adds the contact and displays a success message.
3.  The system shows the new contact in WedLinker.

    Use case ends.

**Extensions**

* 1a. The contact already exists.
    * 1a1. System does not create a new contact.
    * 1a2. System informs the user the contact already exists.

      Use case ends.

### **Use case: UC05 — Create a Wedding**
Similar to [<ins>UC04](#use-case-uc04-create-a-contact) except adding a wedding to WedLinker instead of a Contact

### **Use case: UC06 — Create a Task**
Similar to [<ins>UC04](#use-case-uc04-create-a-contact) except adding a task to WedLinker instead of a Contact

### **Use case: UC07 — Add Phone Number to Contact**

**Guarantees:**
* No duplicate phone numbers will be stored in two different contacts.

**MSS**

1. User <ins>lists all contacts [(UC01)](#use-case-uc01-list-all-contacts)</ins>.
2. User requests to add phone number for a contact with the corresponding details.
3. The system adds the phone number to the contact and displays a success message.
4. The system displays the updated contact information in the address book.

   Use case ends.

**Extensions**

* 1a. The list is empty.
  Use case ends.

* 2a. The system detects an error in the entered data.
    * 2a1. The system displays an error message

      Use case resumes at step 1.

### **Use case: UC08 — Add Address to Contact** 
Similar to [<ins>UC07](#use-case-uc07-add-phone-number-to-contact) except duplicated addresses are allowed

### **Use case: UC09 — Add Email to Contact**
Similar to [<ins>UC07](#use-case-uc07-add-phone-number-to-contact) except duplicated email addresses are allowed

---

### **Use case: UC10 — Search for Contacts by Name**

**MSS**

1.  User searches for the contact by name.
2.  System shows a list of contacts containing the name.

    Use case ends.

### **Use case: UC11 — Search for Contacts by Tag**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for contacts by tag.

### **Use case: UC12 — Search for Contacts by Wedding**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for contacts by wedding.

### **Use case: UC13 — Search for Contacts by Phone Number**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for contacts by phone number.

### **Use case: UC14 — Search for Contacts by Address**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for contacts by address.

### **Use case: UC15 — Search for Contacts by Email Address**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for contacts by email address.

---

### **Use case: UC16 — Search for Wedding by Wedding Name**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for wedding by wedding name.

### **Use case: UC17 — Search for Task by Task Name**
Similar to [<ins>UC10](#use-case-uc10-search-for-contacts-by-name) except searching for task by task name.

---

### **Use case: UC18 — Delete Contact**

**MSS**

1. User <ins>lists all contacts (UC01)</ins>.
2. User requests to delete a specific person in the list.
3. System deletes the contact.

   Use case ends.

**Extensions**

* 1a. The list is empty.
  Use case ends.

* 2a. The to-be-deleted contact is invalid.
    * 2a1. System shows an error message prompting the user to delete a valid contact.
      Use case resumes at step 1.

### **Use case: UC19 — Delete Wedding**
Similar to [<ins>UC18](#use-case-uc18-delete-contact) except deleting wedding.

### **Use case: UC20 — Delete Task**
Similar to [<ins>UC18](#use-case-uc18-delete-contact) except deleting task.

### **Use case: UC21 — Delete Tag**
Similar to [<ins>UC18](#use-case-uc18-delete-contact) except deleting tag.

---

### **Use case: UCSH01 — Edit details for a Contact**

**MSS**

1. User <ins>lists all contacts (UC01)</ins>.
2. User requests to edit the details of a person and specifies what they want to change the details to.
3. AddressBook changes the existing details to the specified details and shows list of persons with new details.

**Extensions**

* 1a. The list is empty.
  Use case ends.

* 2a. The given index is invalid.
    * 2a1. System shows an error message prompting the user to put in a valid index.

      Use case resumes at step 1.

* 2b. The user does not specify what type of details they want to change.
    * 2b1. System shows an error message prompting the user to put in the type of details they want to edit.

      Use case resumes at step 1.

* 2c. The user does not specify what the new details should be.
    * 2c1. System shows an error message prompting the user to put in the new details.

      Use case resumes at step 1.

* 2d. The user specifies details that do not meet the requirements of the detail type.
    * 2d1. System shows an error message prompting the user with the correct detail type format and requirements.

      Use case resumes at step 1.

### **Use case: UCSH02 — Edit details for a Wedding**
Similar to [<ins>USCH01](#use-case-ucsh01-edit-details-for-a-contact) except editing for wedding.

### **Use case: UCSH03 — Clear all contacts**

**Guarantees:**
* No persons will be left in the system.

**MSS**

1. User requests to clear all contact.
2. System deletes all contacts and shows a blank list of persons.

   Use case ends.

### **Use case: UCSH04 — See sample contacts in the system before starting to modify it**

Preconditions: User has not added or edited contacts previously.

**MSS**

1.  User opens the application.
2.  System shows a list of sample contacts.

    Use case ends.

### **Use case: UCSH05 — Reload sample contacts in the system**

**MSS**

1.  User deletes save file of WedLinker.
2.  System shows a list of sample contacts upon restarting application.

    Use case ends.



### **Use case: UCSH06 — See a list of all possible commands**

**MSS**

1.  User requests to see a list of all possible commands they can use in the system.
2.  System shows a link to the list of commands with their corresponding input format.

    Use case ends.

---

### Non-Functional Requirements

**Performance Requirements**
- P1: The system should be able to hold up to 1000 persons without noticeable sluggishness in performance for typical usage.
- P2: The application should respond within two seconds for most user operations.

**Usability Requirements**
- U1: A user with above-average typing speed for regular English text (not code, not system admin commands) should be able to accomplish most tasks faster using keyboard commands than using the mouse.
- U2: The application should target users who can type fast and prefer typing over other means of input, such as clicking buttons, selecting dropdowns and drag-and-drop means.
- U3: The GUI should provide clear and user-friendly error messages when operations fail to assist users in correcting mistakes.
- U4: The GUI should not cause any resolution-related inconveniences for standard screen resolutions of 1920x1080 and higher, and for screen scales of 100% and 125%.
- U5: The GUI should be usable for resolutions of 1280x720 and higher, and for screen scales of 150%.

**Compatibility Requirements**
- C1: The application should work on any _mainstream OS_ as long as it has Java `17` or above installed.
- C2: The application should be platform-independent and work on Windows, Linux, and OS-X without relying on OS-dependent libraries or features.
- C3: The application should not depend on the developer’s own remote server.

**Data Requirements**
- D1: The application should not use a Database Management System (DBMS) for data storage.
- D2: Data stored in the address book should be stored locally, in a human-editable text file.
- D2: The entire application should be packaged into a single JAR file.
- D3: The JAR file size should be within 100 MB and should not be unnecessarily bloated.
- D4: PDF documents generated for documentation should have a file size within 15 MB and should not be unnecessarily bloated.

**Documentation Requirements**
- Doc1: Documentation should be saved in PDF format using Chrome, not any other browser, and should be PDF-friendly (no expandable panels, embedded videos, or animated GIFs).
- Doc2: The Developer Guide and User Guide should be well-structured and easily navigable in PDF format. A new user should be able to quickly locate relevant information for using the product.

**Development Process Requirements**
- DP1: The software should be developed in a breadth-first incremental manner over the project duration.
- DP2: The project is expected to adhere to the milestone deadlines set for every week.

**Reliability and Stability Requirements**
- R1: The application should not crash under normal operations and should handle errors gracefully without data loss.
- R2: The application should maintain a stable performance over extended usage periods.

**Maintainability Requirements**
- M1: The software should primarily follow the Object-Oriented programming paradigm. Having a modular structure allows for the addition of new features with minimal disruption to existing functionality.

**Quality Requirements**
- Q1: The software should be usable by a novice who has never used it before.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Contact**: Contact details of an entry which includes minimally a name. Phone number, address, email address and tags are optional.
* **Tag**: An custom-made field that can be associated to contacts for ease of grouping/filtering
* **Vendor**: Businesses or persons who offer wedding services, like florists, musicians, venue in-charge, etc.
* **User**: The wedding planner who is using WedLinker

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the [jar file](https://github.com/AY2425S1-CS2103T-F15-4/tp/releases) and copy into an empty folder

    2. Open terminal and change into the directory where the jar file is stored.

    3. Enter `java -jar WedLinker.jar` into the terminal to run the WedLinker application.<br>
       Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by enter `java -jar WedLinker.jar` into the terminal again.<br>
       Expected: The most recent window size and location is retained.

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    2. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    3. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files

    1. Upon booting up WedLinker and the contact/address/task list is not as per expected, open the `docs` folder where `WedLinker.jar` is stored.
   
    2. Within the folder, open `AddressBook.json` and identify any mistakes with stored data.<br>
       The terminal from where `WedLinker.jar` is launched should log where the file is corrupted.

    3. If the data is beyond repair, delete the entire `docs` folder and the `AddressBook.json` file to start afresh.
