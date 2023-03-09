package tn.esprit.realestate.Utils;

import java.util.Arrays;
import java.util.List;

public class ProfanityFilter {

    private static final List<String> BANNED_WORDS = Arrays.asList("fuck", "shit", "idiot", "asshole");

    public static boolean isProfanity(String text) {
        String lowerCaseText = text.toLowerCase();
        for (String word : BANNED_WORDS) {
            if (lowerCaseText.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
