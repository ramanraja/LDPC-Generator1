import java.util.BitSet;

public class CodeFactory 
{
	protected String codeName = "Code : UNKNOWN";
	
	public String getName()
	{
		return codeName;
	}
	
	public Matrix getCode()  throws Exception
	{
		//return getRandomCode (10,20);
		//return getIdentityCode (100);
		//return getHammingCode (7,4);
		//return getHammingCode (15,11);
		//return getHammingCode (31,26);
		//return getGolay24();
		//return getGolay23();
		//return getPeg (2000,3000,8);
		return getModifiedPeg ();
	}
	
	// random matrix
	protected Matrix getRandomCode (int rows, int cols)
	{
		codeName = "Code : Random " +rows +" x " +cols;
		G.ROWS = rows;
		G.COLS = cols;
		Matrix mat = new Matrix();
		mat.setDensity (G.MAT_DENSITY);
		mat.fillRandom(G.ROWS, G.COLS);
		return mat;
	}
	
	// identity matrix
	protected Matrix getIdentityCode (int rows)
	{
		codeName = "Code : Identity " +rows +" x " +rows;
		G.ROWS = rows;
		G.COLS = rows;
		Matrix mat = new Matrix();
		mat.getIdentity(G.ROWS);
		return mat;
	}

	// (n,k) Hamming code
	// columns of H are the vectors 0001 to 1111
	protected Matrix getHammingCode (int n, int k)
	{
		codeName = "Code : Hamming (" +n +"; " +k +")";
		G.ROWS = n-k;
		G.COLS = n;
		BitSet[] bs = new BitSet[G.ROWS];
		for (int i=0; i<G.ROWS; i++)
			bs[i] = new BitSet(G.COLS);
		int num;
		for (int i=1; i<=G.COLS; i++)
		{
			num = i;
			for (int j=0; j<G.ROWS; j++)
			{
				if (num%2 != 0) 
					bs[j].set(i-1);
				num = num/2;
			}
		}
		Matrix mat = new Matrix();
		mat.init (G.ROWS,G.COLS,bs);
		//mat.dump();
		return mat;
	}
	
	protected Matrix getGolay24 ()
	{
		codeName = "Code : Golay (24;12)";
		G.ROWS = 12;
		G.COLS = 24;
		BitSet[] bs = new BitSet[G.ROWS];
		for (int i=0; i<G.ROWS; i++)
			bs[i] = new BitSet(G.COLS);
		boolean[] seed = {true,true,false,true,true,true,false,false,false,true,false,true};
		int i, j; 
		boolean tmp;
		for (j=0; j<12; j++)
			bs[0].set(j,seed[j]);
		//G.traceln (bs[0].toString());
		for (i=1; i<11; i++)
		{
			tmp = seed[0];
			for (j=0; j<11; j++)
				seed[j] = seed[j+1];
			seed[j] = tmp;	
			for (j=0; j<12; j++)
				bs[i].set(j,seed[j]);
			//G.traceln (bs[i].toString());
		}
		// last row
		for (j=0; j<11; j++)
			bs[i].set(j);		
		//G.traceln (bs[i].toString());
		for (i=0; i<12; i++)
			bs[i].set(12+i);
		Matrix mat = new Matrix();
		mat.init (G.ROWS, G.COLS, bs);
		//mat.dump();
		return mat;
	}
	
	protected Matrix getGolay23 ()
	{
		Matrix mat = getGolay24(); // first get the matrix and then change the params !
		codeName = "Code : Golay (23;12)";
		G.ROWS = 12;
		G.COLS = 23;		
		BitSet mask = new BitSet(G.COLS);
		mask.set(23);
		mat.eraseColumns(mask);
		return mat;
	}
	
	protected Matrix getPeg (int rows, int cols, int colwt) throws Exception
	{
		codeName = "Code : LDPC- Rgular PEG ("+ cols +";" +rows + "). Col wt= "+ colwt;
		G.ROWS = rows;
		G.COLS = cols;		
		Generator g = new Peg();
		g.init(G.ROWS, G.COLS, colwt);
		g.generate();
		BitSet[] bs = g.exportToBitSet();
		Matrix mat = new Matrix();
		mat.init (G.ROWS, G.COLS, bs);
		//mat.dump();
		return mat;
	}
	
	protected Matrix getModifiedPeg()  throws Exception
	{
		DegreeDistribution dd = new DegreeDistribution();
		dd.init(new int[]{3,4,5,7}, new double[]{0.2,0.2,0.3,0.3});
		int rows = 250;
		int cols = 400;
		codeName = "Code : LDPC- Irregular PEG( "+ cols +";" +rows + "). Distribution: " +dd.toString();
		G.ROWS = rows;
		G.COLS = cols;		
		Generator g = new ModifiedPeg();
		g.init(G.ROWS, G.COLS, dd);
		g.generate();
		BitSet[] bs = g.exportToBitSet();
		Matrix mat = new Matrix();
		mat.init (G.ROWS, G.COLS, bs);
		mat.dump();
		return mat;
	}
}


