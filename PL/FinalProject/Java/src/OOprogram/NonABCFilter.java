package OOprogram;

import java.util.List;

// strip out all non-alphabetic characters.
// Eliminate any “words” that are just white space
public interface NonABCFilter {

    public List process(List<String> wordList);
}
