package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NAME, PREFIX_EMAIL);

        // Throw exception if preamble is not empty or both name and email fields not specified
        if (!argumentMultimap.getPreamble().isEmpty()
            || !isNameOrEmailPrefixPresent(argumentMultimap)) {
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
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new NameOrEmailContainsKeywordsPredicate(nameKeywords, emailKeywords));
    }

    /**
     * Returns true if at least one of {@code PREFIX_NAME} or {@code PREFIX_EMAIL}
     * contains non-empty {@code Optional} values in the given {@code ArgumentMultimap}.
     */
    private static boolean isNameOrEmailPrefixPresent(ArgumentMultimap argumentMultimap) {
        return Stream.of(PREFIX_NAME, PREFIX_EMAIL)
                .anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
