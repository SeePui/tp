package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.testutil.TypicalPersons.*;

public class SortCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SortCommand sortByNameAsc = new SortCommand("name", false);
        SortCommand sortByNameDesc = new SortCommand("name", true);

        // same object -> returns true
        assertEquals(sortByNameAsc, sortByNameAsc);

        // same order and reverse -> returns true
        SortCommand sortByNameAscCopy = new SortCommand("name", false);
        assertEquals(sortByNameAsc, sortByNameAscCopy);

        // different reverse -> returns false
        assertNotEquals(sortByNameAsc, sortByNameDesc);

        // different type -> returns false
        assertNotEquals(1, sortByNameAsc);

        // null -> returns false
        assertNotEquals(null, sortByNameAsc);
    }

    @Test
    public void execute_sortByNameAscending_success() throws CommandException {
        SortCommand command = new SortCommand("name", false);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.MESSAGE_SUCCESS), result);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_sortByNameDescending_success() throws CommandException {
        SortCommand command = new SortCommand("name", true);
        CommandResult result = command.execute(model);

        assertEquals(new CommandResult(SortCommand.MESSAGE_SUCCESS), result);
        assertEquals(Arrays.asList(GEORGE, FIONA, ELLE, DANIEL, CARL, BENSON, ALICE),
                model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        SortCommand command = new SortCommand("name", false);
        String str = command.toString();
        assertTrue(str.contains("order=name"));
        assertTrue(str.contains("reverse=false"));
    }
}
