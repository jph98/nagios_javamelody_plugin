package advws.net.nagios.jmelody.core;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.jrobin.core.*;
import advws.net.nagios.jmelody.util.SimpleCommandLineParser;

/**
 * Command line check for JavaMelody.
 */
public class CheckJMelody {
        
    private SimpleCommandLineParser parser;
    
    public final static String FILE_AGE_MESSAGE = "File is more than 30 minutes old";
    private final static long THIRTY_MINS_DEFAULT = 1000 * 60 * 30;
    private long maxFileAge;
    
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

        if (parser.containsKey("w")) {
            warning = Double.parseDouble(parser.getValue("w"));
        }
        if (parser.containsKey("c")) {
            critical = Double.parseDouble(parser.getValue("c"));
        }

        try {
             
            String dsName = getDataSourceName();

            File f = new File(rrdPath + File.separator + dsName + ".rrd");

            if (!getKey("suppress", "s")) {
                long lastModified = f.lastModified();
                long now = new Date().getTime();

                if (now - lastModified > getMaxFileAge()) {
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
        if (getKey("activeconnections", "ac")) {
            dsName = "activeConnections";
        } else if (getKey("activethreads", "at")) {
            dsName = "activeThreads";
        } else if (getKey("gc", "g")) {
            dsName = "gc";
        } else if (getKey("hitrate", "hr")) {
            dsName = "httpHitsRate";
        } else if (getKey("httpmeantime", "hmt")) {
            dsName = "httpMeanTimes";
        } else if (getKey("httpsessions", "hs")) {
            dsName = "httpSessions";
        } else if (getKey("httperrors", "he")) {
            dsName = "httpSystemErrors";
        } else if (getKey("loadedclasscount", "lcc")) {
            dsName = "loadedClassesCount";
        } else if (getKey("sqlhitrate", "shr")) {
            dsName = "sqlHitsRate";
        } else if (getKey("sqlmeantime", "smt")) {
            dsName = "sqlMeanTimes";
        } else if (getKey("sqlerror", "sse")) {
            dsName = "sqlSystemErrors";
        } else if (getKey("threadcount", "tc")) {
            dsName = "threadCount";
        } else if (getKey("usedconnections", "uc")) {
            dsName = "usedConnections";
        } else if (getKey("heap", "uh")) {
            dsName = "usedMemory";
        } else if (getKey("nonheap", "unh")) {
            dsName = "usedNonHeapMemory";
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
