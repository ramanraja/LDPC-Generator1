/****
		dd.init(new int[]{3,4,5,7}, new double[]{0.2,0.2,0.3,0.3});
		int rows = 2000;
		int cols = 8000;
		
		dd.init(new int[]{3,4}, new double[]{0.5,0.5});
		int rows = 32;
		int cols = 64;

***/
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.BitSet;

public class Controller 
{
	Logger logger;
	int rows, cols;
	int[] degrees;
	double[] distribution;
	
	public static void main(String[] args) throws Exception
	{
		new Controller().go();
		System.out.println ("Controller : Done !");
	}
	
	public void go()throws Exception
	{
		String outfileName = init ("settings.txt");
		DegreeDistribution dd = new DegreeDistribution();
		//dd.init(new int[]{3,4,5,7}, new double[]{0.2,0.2,0.3,0.3});
		dd.init(degrees, distribution);

		/* This is not the same G as in other projects; it is a non-standard G ! */
		G.ROWS = rows;
		G.COLS = cols;		
		
		Generator g = new ModifiedPeg();
		g.init(G.ROWS, G.COLS, dd);
		g.generate();
		
		BitSet[] bs = g.exportToBitSet();
		Matrix mat = new Matrix();
		mat.init (G.ROWS, G.COLS, bs);
		//mat.dump();
		mat.save (outfileName);
		//System.out.println ("H Matrix saved as " +outfileName);
	}
	
	public String init (String fileName) throws Exception
	{
		LineNumberReader reader = new LineNumberReader(new FileReader(fileName));
		String str;		
		while (true)
		{
			str=reader.readLine();
			//System.out.println(str);
			if (str.trim().length()==0) 
				continue;
			if (str.charAt(0)=='#')   // if first column has a # symbol, ignore the line; it is a comment
				continue;
			break;
		}

		String[] values = str.split("\\s++");  // any white space
		
		for (int i=0; i<values.length; i++)
			G.trace (values[i]+"__");
		G.traceln();
		
		rows = Integer.parseInt(values[0]);
		cols = Integer.parseInt(values[1]);
		String degreeStr = values[2].trim();
		String distributionStr = values[3].trim();
		String outputFileName = parse (degreeStr, distributionStr);
		return outputFileName;
	}
	
	protected String parse(String degreeStr, String distributionStr)
	{
		String[] pos = degreeStr.split(",");
		this.degrees = new int[pos.length];
		for (int i=0; i<pos.length; i++)
			degrees[i] = Integer.parseInt(pos[i]);
		
		pos = distributionStr.split(",");
		this.distribution = new double[pos.length];
		for (int i=0; i<pos.length; i++)
			distribution[i] = Double.parseDouble(pos[i]);
		
		String outFileName = "H_" +rows +"," +cols +"_" +degreeStr +"_"+distributionStr +".txt";
		return outFileName;
	}
}
