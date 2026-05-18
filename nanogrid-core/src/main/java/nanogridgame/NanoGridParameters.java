package nanogridgame;

public class NanoGridParameters {
    public NanoGridParameters(){
        MaxColumnSquares =5;
        MaxRowSquares =5;
        RowBreakChance = 50;
        Columns =10;
        Rows = 10;
    }
    public int Columns;
    public int Rows;
    public int MaxColumnSquares;
    public int MaxRowSquares;
    public int RowBreakChance;
    public NanoGridParameters(NanoGridParameters p) {
        Columns = p.Columns;
        Rows = p.Rows;
        MaxColumnSquares = p.MaxColumnSquares;
        MaxRowSquares = p.MaxRowSquares;
        RowBreakChance = p.RowBreakChance;
    }
}
