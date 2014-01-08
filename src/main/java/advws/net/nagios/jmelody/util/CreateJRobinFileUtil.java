package advws.net.nagios.jmelody.util;

import java.io.IOException;
import java.io.PrintStream;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;


public class CreateJRobinFileUtil {

    public static void main(String[] args) throws RrdException, IOException {
        
        String filePath = "/tmp/testrrd";
        
        RrdDef rrdDef = new RrdDef(filePath);
        rrdDef.setStartTime(920804400L);
        rrdDef.addDatasource("speed", "COUNTER", 600, Double.NaN, Double.NaN);
        rrdDef.addArchive("AVERAGE", 0.5, 1, 24);
        rrdDef.addArchive("AVERAGE", 0.5, 6, 10);
        
        RrdDb rrdDb = new RrdDb(rrdDef);
        rrdDb.close();
        
        System.out.println("Wrote: " + filePath);
        
        System.out.println("Contents: ");
        
        RrdDb db = new RrdDb(rrdDef);
        db.dumpXml(new PrintStream(System.out));
        
    }
}
