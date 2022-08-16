package logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.qameta.allure.Step;

public final class LogUtil {
	private static final Logger logger = LogManager.getLogger(LogUtil.class);
	
	private static PrintStream myPrintStream;
	private LogUtil() {
	
	}
	
	@Step("{0}")
	public static void log(String msg) {
		logger.info(msg);
	}
	
	public static void logToOnlyFile(String msg) {
		logger.info(msg);
	}
	
	public static PrintStream getPrintStream()
	{

		if ( myPrintStream == null )
	    {
	        OutputStream output = new OutputStream()
	        {
	            private StringBuilder myStringBuilder = new StringBuilder();

	            @Override
	            public void write(int b) throws IOException 
	            {
	                this.myStringBuilder.append((char) b );
	            }

	            @Override
	            public void flush()
	            {
	                LogUtil.logToOnlyFile(this.myStringBuilder.toString());
	            }
	        };

	        myPrintStream = new PrintStream( output, true );  
	    }

	    return myPrintStream;
	}


}
