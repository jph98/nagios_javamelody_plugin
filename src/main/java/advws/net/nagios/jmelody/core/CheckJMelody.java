package advws.net.nagios.jmelody.core;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdException;

import advws.net.nagios.jmelody.util.SimpleCommandLineParser;

/**
 *  CheckJMelody provides a RRD file reader and warning/critical alerts based on thresholds.
 */
public class CheckJMelody {                
           
    public final static String FILE_AGE_MESSAGE = "File is more than 30 minutes old";
    public final static long THIRTY_MINS_DEFAULT = 1000 * 60 * 30;
    
    private long maxFileAge;
    
    private static final String AC = "activeConnections";
    private static final String AT = "activeThreads";
    private static final String GC = "gc";    
    private static final String HR = "httpHitsRate";    
    private static final String HMT = "httpMeanTimes";
    private static final String HS = "httpSessions";
    private static final String HE = "httpSystemErrors";
    private static final String LCC = "loadedClassesCount";
    private static final String SHR = "sqlHitsRate";
    private static final String SMT = "sqlMeanTimes";
    private static final String SSE = "sqlSystemErrors";
    private static final String TC = "threadCount";
    private static final String UC = "usedConnections";
    private static final String UH = "usedMemory";
    private static final String UNH = "usedNonHeapMemory";
        
    private static final String SUPPRESS_SHORTKEY = "s";
    private static final String SUPPRESS_LONG_KEY = "suppress";
    
    private static final String CRITICAL = "c";
    private static final String WARNING = "w";
    
    @SuppressWarnings({ "unchecked", "serial" })
    private Map<MultiKey, String> keys = new MultiKeyMap() {{
    
        put("ac", "activeconnections", AC);
        put("at", "activethreads", AT);
        put("g", "gc", GC);
        put("hr", "hitrate", HR);
        put("hmt", "httpmeantime", HMT);
        put("hs", "httpsessions", HS);
        put("he", "httperrors", HE);
        put("lcc", "loadedclasscount", LCC);
        put("shr", "sqlhitrate", SHR);
        put("smt", "sqlmeantime", SMT);
        put("sse", "sqlerror", SSE);
        put("tc", "threadcount", TC);
        put("uc", "usedconnections", UC);
        put("uh", "heap", UH);
        put("unh", "nonheap", UNH);
    }};
    
    private SimpleCommandLineParser parser;
    
    public CheckJMelody(String[] args) {
        parser = new SimpleCommandLineParser(args);
        maxFileAge = THIRTY_MINS_DEFAULT;
    }

    public boolean getKey(String key, String shortKey) {
        return parser.containsKey(key, shortKey);
    }
    
    /**
     * Check the RRD database file returning an integer status.
     */
    public int checkRRD() {

        String rrdPath = parser.getValue("rrdpath", "rrd", "r");
        
        double warning = 0;
        double critical = 0;

        if (parser.containsKey(WARNING)) {
            warning = Double.parseDouble(parser.getValue(WARNING));
        }
        if (parser.containsKey(CRITICAL)) {
            critical = Double.parseDouble(parser.getValue(CRITICAL));
        }

        try {
             
            String dsName = getDataSourceName();

            File f = new File(rrdPath + File.separator + dsName + ".rrd");

            if (!getKey(SUPPRESS_LONG_KEY, SUPPRESS_SHORTKEY)) {
                
                long now = new Date().getTime();
                long lastModified = f.lastModified();                
                long time = now - lastModified;
                               
                if (time > getMaxFileAge() ) {
                    System.out.println(FILE_AGE_MESSAGE);
                    return 3;
                }
            }

            RrdDb db = new RrdDb(f);
            double value = db.getLastDatasourceValue(dsName);

            String textOutput = dsName + " - " + convertDoubleToString(value);
            
            String perfData = " | " + dsName + "=" + convertDoubleToString(value) + ";"
                    + convertDoubleToString(warning) + ";" + convertDoubleToString(critical) + ";";

            if (warning != 0 && critical != 0) {

                if (value > critical) {
                    System.out.println(textOutput + " CRITICAL " + perfData);
                    return 2;
                } else if (value > warning) {
                    System.out.println(textOutput + " WARNING " + perfData);
                    return 1;
                }
            }

            System.out.println(textOutput + " OK " + perfData);
            return 0;

        } catch (IOException e) {
            System.out.println(e.toString());
            return 3;
        } catch (RrdException e) {
            System.out.println(e.toString());
            return 3;
        }
    }

    long getMaxFileAge() {
        return maxFileAge;
    }
        
    void setMaxFileAge(long maxFileAge) {               
        this.maxFileAge = maxFileAge;
    }

    private String getDataSourceName() {
        
        String dsName = "";

        for (MultiKey key: keys.keySet()) {
        
            if (getKey((String) key.getKeys()[0], (String) key.getKeys()[1])) {
                return keys.get(key);
            }
        }
               
        return dsName;
    }
    
    private static String convertDoubleToString(double d) {
        return new BigDecimal(d).toString();
    }
    
    public static void main(String[] args) {

        CheckJMelody cm = new CheckJMelody(args);
        int retVal = cm.checkRRD();
        System.exit(retVal);
    }

}
