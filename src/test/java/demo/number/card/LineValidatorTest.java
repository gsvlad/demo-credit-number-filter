package demo.number.card;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class LineValidatorTest {

    private List<Match> parameters = asList(
            new Match("test,  4500440904611147, test", true),
            new Match("  4500440904611147, test", true),
            new Match("test, 4500440904611147", true),
            new Match("test, 4500440904611147  4500440904611147", true),
            new Match("test, 4500440904611141  4500440904611147", true),
            new Match("test, 4500440904611141  4500440904611141", false),
            new Match("test, 4500440904611141", false),
            new Match("test, 5283762411420823", true),
            new Match("test, 5283762411420823    ,,,,,,", true),
            new Match("5283762411420823", true),
            new Match("test, number is 5283762411420823", true),
            new Match("test, number is5283762411420823", true),
            new Match("test, number is5283762411420823(test)", true),
            new Match("test, number 52837624114208231", false),
            new Match("5283762411420823", true),
            new Match("52837624 11420823", false)
    );

    @Test
    public void hasCardNumber() {
        parameters.forEach(it -> assertEquals(it.expected, LineValidator.hasCardNumber(it.string)));
    }

    class Match {
        String string;
        Boolean expected;

        public Match(String string, Boolean expected) {
            this.string = string;
            this.expected = expected;
        }
    }
}