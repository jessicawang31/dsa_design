package autocomplete;

import java.util.*;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        // TODO: Replace with your code
        elements.addAll(terms);
        Collections.sort(elements, CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        // TODO: Replace with your code
        List<CharSequence> result = new ArrayList<>();
        int i = Collections.binarySearch(elements, prefix, CharSequence::compare);

        int start = i;
        if (i < 0) {
            start = -(start + 1);
        } else {
            while (i > 0 && elements.get(i - 1).toString().startsWith(prefix.toString())) {
                i--;
            }
            start = i;
        }

        for (int j = start; j < elements.size() && elements.get(j).toString().startsWith(prefix.toString()); j++) {
            result.add(elements.get(j));
        }

        return result;
    }
}
