package org.flymine.util;

/**
 * Collection of commonly used String utilities
 *
 * @author Andrew Varley
 */
public class StringUtil
{
    private StringUtil() {
    }

    /**
     * Returns the number of occurances of str in target
     *
     * @param str the String to count
     * @param target the String to look in
     * @return the number of occurances of str in target
     * @throws NullPointerException if either str or target are null
     */
    public static int countOccurances(String str, String target) {
        if ((str == null) || (target == null)) {
            throw new NullPointerException("Cannot pass null arguments to countOccurances");
        }

        int count = 0;
        int index = -1;

        while ((index = target.indexOf(str, index + 1)) >= 0) {
            count++;
        }
        return count;
    }

    /**
     * Returns a capitalised version of the given String
     *
     * @param str the String to capitalise
     * @return the capitalised version of str
     */
    public static String capitalise(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() <= 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }

    /**
     * Returns a string with the same initial letter case as the template string
     *
     * @param n the String to convert
     * @param template the String to base the conversion on
     * @return the new String, capitalised like template
     */
    public static String toSameInitialCase(String n, String template) {
        if (n == null) {
            throw new NullPointerException("String to convert cannot be null");
        }
        if (template == null) {
            return n;
        }
        Character first = new Character(template.charAt(0));
        StringBuffer sb = new StringBuffer();
        
        if (Character.isUpperCase(template.charAt(0))) {
            sb.append(Character.toUpperCase(n.charAt(0)));
        }
        if (Character.isLowerCase(template.charAt(0))) {
            sb.append(Character.toLowerCase(n.charAt(0)));
        }
        if (n.length() > 1) {
            sb.append(n.substring(1, n.length()));
        }
        
        return sb.toString();
    }
}
