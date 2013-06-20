package advws.net.nagios.jmelody.util;

/**
 * @author Shawn Bower
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A simple class that provides utilities to ease command line parsing.
 */
public class SimpleCommandLineParser {

    private Map<String, String> argMap;

    /**
     * Initializes the command line parser by parsing the command line args
     * using simple rules.
     * <p>
     * The arguments are parsed into keys and values and are saved into a
     * HashMap. Any argument that begins with a '--' or '-' is assumed to be a
     * key. If the following argument doesn't have a '--'/'-' it is assumed to
     * be a value of the preceding argument.
     */
    public SimpleCommandLineParser(String[] arg) {
        
        argMap = new HashMap<String, String>();
        for (int i = 0; i < arg.length; i++) {
            String key;
            if (arg[i].startsWith("--")) {
                key = arg[i].substring(2);
            } else if (arg[i].startsWith("-")) {
                key = arg[i].substring(1);
            } else {
                argMap.put(arg[i], null);
                continue;
            }
            String value;
            int index = key.indexOf('=');
            if (index == -1) {
                if (((i + 1) < arg.length) &&
                        (arg[i + 1].charAt(0) != '-')) {
                    argMap.put(key, arg[i + 1]);
                    i++;
                } else {
                    argMap.put(key, null);
                }
            } else {
                value = key.substring(index + 1);
                key = key.substring(0, index);
                argMap.put(key, value);
            }
        }
    }

    /**
     * Returns the value of the first key found in the map.
     */
    public String getValue(String... keys) {
        for (int key_i = 0; key_i < keys.length; key_i++) {
            if (argMap.get(keys[key_i]) != null) {
                return argMap.get(keys[key_i]);
            }
        }
        return null;
    }

    /**
     * Returns true if any of the given keys are present in the map.
     */
    public boolean containsKey(String... keys) {
        Set<String> keySet = argMap.keySet();
        for (Iterator<String> keysIter = keySet.iterator(); keysIter.hasNext();) {
            String key = keysIter.next();
            for (int key_i = 0; key_i < keys.length; key_i++) {
                if (key.equals(keys[key_i])) {
                    return true;
                }
            }
        }
        return false;
    }
}