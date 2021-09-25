package View.Utilities;

import java.util.ArrayList;

public class Utilities {

    public static String[] searchFunction(String searchQuery, String[] entryNames)
    {
        /**Gets a set of strings and returns matching strings with the search query*/

        ArrayList<String> results = new ArrayList<>();
        int searchQueryLength = searchQuery.length();

        for(int i=0; i<entryNames.length; i++)
        {
            if(entryNames[i].length() >= searchQueryLength && searchQuery.equalsIgnoreCase(entryNames[i].substring(0,searchQueryLength)))
            {
                results.add(entryNames[i]);
            }
        }

        if(results.size() == 0 && searchQueryLength > 2)
        {
            return searchFunction(searchQuery.substring(0,searchQueryLength-1),entryNames);
        }
        else
        {
            return results.toArray(new String[results.size()]);
        }
    }

    public static String escapeIllegalCharacters(String s)
    {   //Escape double quotes and backslash

        StringBuffer str = new StringBuffer(s);
        for (int i = 0; i < str.length(); i++)
        {
            if(str.charAt(i) == '\"' || str.charAt(i) == '\\')
            {
                str.insert(i,"\\");
                i++;
            }
        }
        return str.toString();
    }
}
