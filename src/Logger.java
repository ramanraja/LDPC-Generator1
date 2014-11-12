/**
 * Write comma separated numbers and strings for a CSV file
 */
import java.io.*;
import java.util.Calendar;

public class Logger 
{
	PrintWriter writer;
	
	public void init () throws Exception
	{
		StringBuilder builder = new StringBuilder (G.LOG_PREFIX);
		Calendar cal = Calendar.getInstance();
		int i=cal.get(Calendar.DATE);
		builder.append(i); builder.append("_");
		i = cal.get(Calendar.MONTH);
		builder.append((i+1)); builder.append("_");	
		i = cal.get(Calendar.YEAR);
		builder.append(i); builder.append("_");
		i = cal.get(Calendar.HOUR);
		builder.append(i); builder.append("-");
		i = cal.get(Calendar.MINUTE);
		builder.append(i); builder.append("-");
		i = cal.get(Calendar.SECOND);
		builder.append(i); 
		builder.append(G.LOG_SUFFIX);
		writer = new PrintWriter(builder.toString());
	}
	
	public void log (String str)
	{
		writer.write (str +",");
	}
	public void log (int num)
	{
		writer.write (num +",");
	}
	public void log (float num)
	{
		writer.write (num +",");
	}
	
	public void logln ()
	{
		writer.write ("\n");
	}
	public void logln (String str)
	{
		writer.write (str +"\n");
	}
	public void logn (int num)
	{
		writer.write (num +"\n");
	}
	public void logln (float num)
	{
		writer.write (num +"\n");
	}
	
	public void close()
	{
		writer.flush();
		writer.close();
	}
}
