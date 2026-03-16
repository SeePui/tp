package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameOrEmailContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // ArgumentTokenizer requires a space before the first prefix to recognize it.
        // Add a leading space if input starts with a prefix (e.g., "n/alice" -> " n/alice").
        // The preamble check below will validate no unprefixed content exists.
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs,
                PREFIX_NAME, PREFIX_EMAIL);

        if (!argumentMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = argumentMultimap.getAllValues(PREFIX_NAME)
                .stream()
                .flatMap(keyword -> Arrays.stream(keyword.split("\\s+")))
                .filter(keyword -> !keyword.isBlank())
                .toList();

        List<String> emailKeywords = argumentMultimap.getAllValues(PREFIX_EMAIL)
                .stream()
                .flatMap(keyword -> Arrays.stream(keyword.split("\\s+")))
                .filter(keyword -> !keyword.isBlank())
                .toList();

        // If no name or email keywords specified
        if (nameKeywords.isEmpty() && emailKeywords.isEmpty()) {
            System.out.println("Name and email are both empty");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new NameOrEmailContainsKeywordsPredicate(nameKeywords, emailKeywords));
    }

}
