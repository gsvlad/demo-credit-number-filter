package demo.number.card;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LineValidator {
    private static final Pattern pattern = Pattern.compile("(?<!\\d)\\d{16}(?!\\d)");

    public static boolean hasCardNumber(String line) {
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            if (validateCreditCardNumber(matcher.group())) {
                return true;
            }
        }

        return false;
    }

    // Luhn algorithm
    private static boolean validateCreditCardNumber(String str) {
        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        return sum % 10 == 0;
    }

}
