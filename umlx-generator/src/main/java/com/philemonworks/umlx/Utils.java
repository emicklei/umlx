package com.philemonworks.umlx;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Helper class for String convertions.
 * 
 * @author E.M.Micklei
 */
public class Utils {
    /**
     * Convert a qualifiedName of a Class,Interface or Package to a name without its namespace.
     * @param qualifiedName : String
     */
    public static java.lang.String shortNameFrom(java.lang.String qualifiedName) {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(qualifiedName, ".");
        String shortName = null;
        while (tokenizer.hasMoreTokens())
            shortName = tokenizer.nextToken();
        return shortName;
    }

    /** 
     * Converts an array into a Vector
     * @param objectArray : Object[]
     */
    public static java.util.Vector vectorFromArray(Object[] objectArray) {
        java.util.Vector result = new java.util.Vector(objectArray.length);
        for (int i = 0; i < objectArray.length; i++) {
            result.add(objectArray[i]);
        }
        return result;
    }

    /**
     * Split a single String of comma separated String into a Vector
     * @param commaSeparatedNames : String
     */
    public static Vector splitStrings(String commaSeparatedNames) {
        StringTokenizer tokenizer = new StringTokenizer(commaSeparatedNames, ",");
        Vector strings = new Vector();
        while (tokenizer.hasMoreTokens()) {
            strings.add(tokenizer.nextToken().trim());
        }
        return strings;
    }

    /**
     * Merge a collection of Strings into a single String
     * separated by a comma.
     * @param names : Vector
     * @return
     */
    public static String mergeStrings(Vector names) {
        StringBuffer merged = new StringBuffer();
        for (int i = 0; i < names.size();) {
            merged.append((String) names.elementAt(i));
            if (++i != names.size())
                merged.append(',');
        }
        return merged.toString();
    }

    /**
     * Return a typical generated accessor name for a field.
     * The prefix is either "get" or "set" or "is".
     * @param prefix : String 
     * @param fieldName : String
     * @return String : the accessor name
     */
    public static String accessorFrom(String prefix, String fieldName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(Character.toUpperCase(fieldName.charAt(0)));
        buffer.append(fieldName.substring(1, fieldName.length()));
        return buffer.toString();
    }

    /**
     * Convert a qualified name (using dots as separators) into a file path (using file separators).
     * @param dotted (e.g. com.ikke.nu )
     * @return (e.g. com/ikke/nu )
     */
    public static String dotted2Path(String dotted) {
        return dotted.replace('.', File.separatorChar);
    }

    /**
     * Extract the package name part of a qualified name (for a class or interface)
     * @param qualifiedTypeName
     * @return String the package name
     */
    public static String packageNameFrom(String qualifiedTypeName) {
        int dotIndex = qualifiedTypeName.lastIndexOf('.');
        if (dotIndex == -1)
            return "default";
        return qualifiedTypeName.substring(0, dotIndex);
    }
}