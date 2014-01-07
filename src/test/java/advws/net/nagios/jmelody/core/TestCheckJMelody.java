package advws.net.nagios.jmelody.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCheckJMelody {
	
	private final static String activeConnections ="activeConnections - 0 OK  | activeConnections=0;0;0;\n";
	private final static String activeThreads ="activeThreads - 0 OK  | activeThreads=0;0;0;\n";
	private final static String gc ="gc - 0 OK  | gc=0;0;0;\n";
	private final static String httpHitsRate ="httpHitsRate - 28 OK  | httpHitsRate=28;0;0;\n";
	private final static String httpMeanTimes ="httpMeanTimes - 48 OK  | httpMeanTimes=48;0;0;\n";
	private final static String httpSessions ="httpSessions - 34 OK  | httpSessions=34;0;0;\n";
	private final static String httpSystemErrors ="httpSystemErrors - 0 OK  | httpSystemErrors=0;0;0;\n";
	private final static String loadedClassesCount ="loadedClassesCount - 18955 OK  | loadedClassesCount=18955;0;0;\n";
	private final static String sqlHitsRate ="sqlHitsRate - 294 OK  | sqlHitsRate=294;0;0;\n";
	private final static String sqlMeanTimes ="sqlMeanTimes - 1 OK  | sqlMeanTimes=1;0;0;\n";
	private final static String sqlSystemErrors ="sqlSystemErrors - 0 OK  | sqlSystemErrors=0;0;0;\n";
	private final static String threadCount ="threadCount - 163 OK  | threadCount=163;0;0;\n";
	private final static String usedConnections ="usedConnections - 0 OK  | usedConnections=0;0;0;\n";
	private final static String usedMemory ="usedMemory - 2276822000 OK  | usedMemory=2276822000;0;0;\n";
	private final static String usedNonHeapMemory ="usedNonHeapMemory - 182845768 OK  | usedNonHeapMemory=182845768;0;0;\n";
	private final static String usedNonHeapMemoryWarning ="usedNonHeapMemory - 182845768 WARNING  | usedNonHeapMemory=182845768;182845760;282845768;\n";
	private final static String usedNonHeapMemoryCritical ="usedNonHeapMemory - 182845768 CRITICAL  | usedNonHeapMemory=182845768;182845700;182845760;\n";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		System.setOut(new PrintStream(outContent, true, "UTF-8"));
	}

	@Test
	public void testActiveConnections() {
		
	    int retVal = getRetVal(new String[] {"-r","rrd", "-ac", "-s"});
        assertEquals(activeConnections, outContent.toString());
        assertEquals(0, retVal);
    }
	
	@Test
	public void testActiveThreads() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-at", "-s"});
		assertEquals(activeThreads, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testGarbageCollection() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-gc", "-s"});
		assertEquals(gc, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testHttpHitRate() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-hr", "-s"});
		assertEquals(httpHitsRate, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testHttpMeanTime() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-hmt", "-s"});
		assertEquals(httpMeanTimes, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testHttpSessions() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-hs", "-s"});
		assertEquals(httpSessions, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testHttpSystemErrors() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-he", "-s"});
		assertEquals(httpSystemErrors, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testLoadedClassCount() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-lcc", "-s"});
		assertEquals(loadedClassesCount, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testSqlHitRate() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-shr", "-s"});
		assertEquals(sqlHitsRate, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testSqlMeanTime() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-smt", "-s"});
		assertEquals(sqlMeanTimes, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testSqlError() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-sse", "-s"});
		assertEquals(sqlSystemErrors, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testThradCount() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-tc", "-s"});
		assertEquals(threadCount, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testUsedConnections() {
			
	    int retVal = getRetVal(new String [] {"-r","rrd", "-uc", "-s"});
		assertEquals(usedConnections, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testUsedHeap() {
			
	    int retVal = getRetVal(new String [] {"-r","rrd", "-uh", "-s"});
		assertEquals(usedMemory, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testUsedNonHeap() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-unh", "-s" });
		assertEquals(usedNonHeapMemory, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testUsedNonHeapWarning() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-unh", "-w", "182845760", "-c", "282845768", "-s"});
		assertEquals(usedNonHeapMemoryWarning, outContent.toString());
		assertEquals(1, retVal);
	}
	
	@Test
	public void testUsedNonHeapCritical() {
			
	    int retVal = getRetVal(new String[] {"-r","rrd", "-unh", "-w", "182845700", "-c", "182845760", "-s" });
		assertEquals(usedNonHeapMemoryCritical, outContent.toString());
		assertEquals(2, retVal);
	}
	
	@Test
	public void testFileAgeOld() {
			
	    String[] args = new String[] {"-r","rrd", "-unh"};
	    CheckJMelody cm = new CheckJMelody(args);
	    
	    // Artificially set the max file age
	    cm.setMaxFileAge(10);
	    int result = cm.checkRRD();
	    
		assertEquals(CheckJMelody.FILE_AGE_MESSAGE + "\n", outContent.toString());
		assertEquals(3, result);
	}
	
	@Test
	public void testFileAgeNew() {
			
		File f = new File("rrd/threadCount.rrd");
		f.setLastModified(new Date().getTime());
				
		int retVal = getRetVal(new String[] {"-r","rrd", "-tc"});
		assertEquals(threadCount, outContent.toString());
		assertEquals(0, retVal);
	}
	
	@Test
	public void testNoRRDFile() {
			
		assertEquals(3, getRetVal(new String[] {"-r","rrd", "-nrd"}));
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	public int getRetVal(String[] args) {
	    CheckJMelody cm = new CheckJMelody(args);
        return cm.checkRRD();
	}

}
