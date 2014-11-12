/**
 * This is not the same G as in other projects; it is a non-standard G !
 * @author Raja
 *
 */
public class G 
{
	public static boolean silent = false;
	public static int ROWS = 10;
	public static int COLS = 10;
	public static int NUM_TRIALS = 10;
	public static float MAT_DENSITY = 0.3f;
	
	public static int MAXDEGREE = 25;      // maximum node degree
	public static boolean COL_EXPORT_MODE = true;  // save the H file in Matlab row,col format or col,row format
	public static int MAX_DEPTH = 10;  // minimum acceptable girth - needed (for large N) to terminate PEG algorithm
	public static double ROUNDOFF_ERROR = 0.001;  
	public static int PRECISION = 10000;

	public static String LOG_PREFIX = "..\\Logs\\Log-";
	public static String LOG_SUFFIX = ".csv";	
	
	public static double round (double d)
	{
		int tmp = (int) (d*PRECISION + 0.5);
		return (double) tmp/ PRECISION;
	}

	public static void trace (String str)
	{
		if (!silent)
			System.out.print(str);
	}
	public static void traceln (String str)
	{
		if (!silent)
			System.out.println(str);
	}
	public static void traceln ()
	{
		if (!silent)
			System.out.println();
	}
	
	// ignore the silent mode and print
	public static void itrace (String str)
	{
		System.out.print(str);
	}
	public static void itraceln (String str)
	{
		System.out.println(str);
	}
	public static void itraceln ()
	{
		System.out.println();
	}
}
