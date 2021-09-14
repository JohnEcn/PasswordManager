package tests.Utilities;

import View.Utilities.Utilities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UtilitiesTests {

    @Test
    @DisplayName("Search test")
    public void searchTest()
    {
        String[] searchedStrings = {"Revolut","Steam","Reveri","Stream","Gmail","Grander","University","Kraken","Test","Test2"};
        String[] searchQueries = {"T","Fax","Stta","Grrr","Kark","Revol"};
        ArrayList<String[]> results = new ArrayList<>();

        for(int i = 0; i<searchQueries.length;i++)
        {
            results.add(Utilities.searchFunction(searchQueries[i],searchedStrings));
        }
    }
}
