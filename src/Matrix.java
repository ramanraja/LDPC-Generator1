
/**
Modified 1 july 2011 : it can save itself in a file.

This class represents a parity check matrix; 
It can find the rank of itself, or a selected subset of its columns
NOTE : Finding the rank DESTROYS the original matrix (this is done
for memory efficiency). 
Pass a clone of it if you want to preserve the original matrix.
**/

import java.util.BitSet;
import java.util.Random;
import java.io.FileWriter;

public class Matrix implements Cloneable
{
    protected int rows;
    protected int cols;
    public BitSet[] rowSet;
	protected float density = 0.5f;
	
    public void init (int _rows, int _cols, BitSet[] _rowSet) 
    {
    	this.rows = _rows;  // to enable re-initialization with a different matrix
    	this.cols = _cols;
    	this.rowSet = _rowSet;
    }
    
    public void setDensity (float _density)
    {
    	if (_density < 0.0f && _density > 1.0f)
    		throw new RuntimeException ("Matrix density must be between 0 and 1");
    	density = _density;
    	G.traceln ("density = "+density);
    }
    
    public float getDensity ()
    {
    	return density;
    }
    
	/**
	 * reduce this matrix to triangular row echelon form
	 * Note : The original matrix is DESTROYED
	 *
	 */
	public void triangulate()
	{
		for (int c=0; c<cols; c++)
		{
			if (cols > rows && c==rows) break; // we have run out of rows
			if (!rowSet[c].get(c))
				if (!swapRows (c,c)) 
					if (!swapCols(c,c))
						continue;  
			makePivotColumZero(c,c);
		}
		//G.traceln ("triangulate() completed:");
		//dump();
	}

	/**
	 * Swaps the candidate pivot row with another row of matrix
	 * @param pivotRow
	 * @param pivotCol
	 * @return true if a suitable row was found and swapped
	 * @return false if all the column elements below the pivot element are zeros
	 */
	private boolean swapRows (int pivotRow, int pivotCol)
	{
		for (int r=pivotRow+1; r<rows; r++)
		{
			if (rowSet[r].get(pivotCol)) 
			{
				//G.traceln ("swapping row "+pivotRow +" with " +r);
				rowSet[r].xor(rowSet[pivotRow]);
				rowSet[pivotRow].xor(rowSet[r]);
				rowSet[r].xor(rowSet[pivotRow]);
				return true;
			}
		}
		return false;
	}

	/**
	 * Swaps the candidate pivot column with another column of the matrix
	 * @param pivotRow
	 * @param pivotCol
	 * @return true if a suitable column was found and swapped
	 * @return false if all the elements to the right of the pivot element in that row are zero
	 */
	private boolean swapCols (int pivotRow, int pivotCol)
	{
		for (int c=pivotCol+1; c<cols; c++)
		{
			if (!rowSet[pivotRow].get(c)) continue;
			//G.traceln ("swapping col "+pivotCol +" with " +c);
			boolean tmp;
			for (int r=0; r<rows; r++)  
			{
				tmp = rowSet[r].get(c);
				rowSet[r].set (c, rowSet[r].get(pivotCol));
				rowSet[r].set(pivotCol, tmp);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Makes all elements of the pivot column, below the pivot element, to be zero
	 */
	private void makePivotColumZero(int pivotRow, int pivotCol)
	{
		for (int r=pivotRow+1; r<rows; r++)
		{
			if (rowSet[r].get(pivotCol))
				rowSet[r].xor(rowSet[pivotRow]);
		}
		//G.traceln ("makePivotColumZero(): " +pivotRow+ ","+pivotCol);
		//dump();
	}

	/**
	 * Note : The original matrix is DESTROYED
	 * @return rank of this matrix
	 */
	public int getRank()
	{
		triangulate();
		int rank=0;
		for (int r=0; r<rows; r++)
		{
			if (rowSet[r].cardinality() != 0)
				rank++;
		}
		return rank;
	}
	
	/**
	 * Creates new row sets and fills them with random bits
	 * The density of the matrix is arbitrarily fixed at approximately 25%
	 * @param _rows
	 * @param _cols
	 */
	public void fillRandom (int _rows, int _cols)
	{
		if (_rows <=0 || _cols <=0) throw new RuntimeException ("Invalid matrix dimensions");
		
		rows = _rows;
		cols = _cols;
		int weight = (int)(cols * density);
		Random rand = new Random();
		
		this.rowSet = new BitSet[rows];
		for (int r=0; r<rows; r++)
		{
			rowSet[r]= new BitSet(cols);
			for (int i=0; i<weight; i++)
				rowSet[r].set(rand.nextInt(cols));
		}
		//G.traceln ("fillRandom():");
		//dump();
	}
	
	/**
	 * Creates an identity matrix
	 * @param _rows
	 */
	public void getIdentity (int _rows)
	{
		if (_rows <=0) throw new RuntimeException ("Invalid matrix dimension");
		
		rows = _rows;
		cols = _rows;
		this.rowSet = new BitSet[rows];
		for (int r=0; r<rows; r++)
		{
			rowSet[r]= new BitSet(cols);
			rowSet[r].set(r);
		}
		//G.traceln ("getIdentity():");
		//dump();
	}
	
	/*
	 * Keep only a subset of the columns, and zero out the rest.
	 * The columns to be retained are given by the bitset mask.
	 * Note : The original matrix is DESTROYED
	 */
	public void selectColumns (BitSet mask)
	{
		for (int r=0; r<rows; r++)
			rowSet[r].and(mask);
	}

	/*
	 * Keep only a subset of the columns, and zero out the rest.
	 * The columns to be erased are given by the bitset mask.
	 * Note : The original matrix is DESTROYED
	 */
	public void eraseColumns (BitSet mask)
	{
		mask.flip (0,mask.size()-1);
		for (int r=0; r<rows; r++)
			rowSet[r].and(mask);
	}
	
	public Object clone()
	{
		BitSet[] bs = new BitSet[rows];
		for (int r=0; r<rows; r++)
			bs[r] = (BitSet)rowSet[r].clone();
		Matrix mat = new Matrix();
		mat.init(rows, cols, bs);
		return mat;
	}
	
	public void dump()
	{
		if (G.silent) return;
		for (int r=0; r<rows; r++)
		{
			for (int c=0; c<cols; c++)
			{
				if (rowSet[r].get(c))
					G.trace ("1 ");
				else
					G.trace ("0 ");
			}
			G.traceln ();
		}
		G.traceln ("-------------------------------------");
	}
	
	public String toString()
	{
		return ("Matrix : "+rows +" rows, " +cols +" cols");
	}
	
    public void save (String fileName) throws Exception
    {
        G.traceln ("Saving to File: "+fileName);
        G.traceln ("\t"+rows +" Rows, "+  cols +" Cols");
        FileWriter writer = new FileWriter (fileName);
        
        String str;
        for (int r=0;  r<rows; r++)
        {
        	for (int c=rowSet[r].nextSetBit(0); c>=0; c=rowSet[r].nextSetBit(c+1))
        	{	
        		str = "("+(r+1) +", " +(c+1) +") 1\r\n";
        		writer.write(str);
        	}	
        }
        writer.flush();
        writer.close();
    }	
//=============================================================================================
	public static void main (String[] args) throws Exception
	{
		new Matrix().unitTest2();
	}
	private void unitTest1()
	{
		Matrix m = new Matrix();
		m.fillRandom(10, 10);
		m.dump();
		Matrix newMat = (Matrix)m.clone();
		int rank = newMat.getRank();
		G.traceln("Rank= "+ rank +"\n");
		newMat.dump();
		m.dump();
	}
	private void unitTest2()
	{
		Matrix m = new Matrix();
		m.fillRandom(5, 10);
		m.dump();
		BitSet bs = new BitSet();
		bs.set(0); bs.set(1); bs.set(2); bs.set(3);bs.set(4);
		Matrix m1 = (Matrix)m.clone();
		m1.selectColumns(bs);
		m1.dump();
		Matrix m2 = (Matrix)m.clone();
		m2.eraseColumns(bs);
		m2.dump();
	}
}
