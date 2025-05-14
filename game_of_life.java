public class game_of_life {
    private int [][] grid;
    private int rows,columns;
    public game_of_life(int rows,int columns){
        this.rows=rows;
        this.columns=columns;
        grid=new int[rows][columns];
    }
    private void place(){
        for(int i=0;i<20;i++){
            int position=(int)(Math.random()*(this.columns*this.rows));
            int x=position/this.columns,y=position%this.columns;
            grid[x][y]=1;
        }
    }
    private int NeihborsCount(int x,int y){
        int count=0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) continue; // Skip the cell itself
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < this.rows && ny >= 0 && ny < this.columns) {
                    count += this.grid[nx][ny];
                }
            }
        }return count;
    }
    private void newGrid(){
        int [][] newGrid = new int[rows][columns];
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                int count = NeihborsCount(i, j);
    
                if (grid[i][j] == 1) {newGrid[i][j] = (count == 2 || count == 3) ? 1 : 0;} 
                else {newGrid[i][j] = (count == 3) ? 1 : 0;}
            }
        }
        this.grid = newGrid;
    }
    
    public void game(){
        place();
        while (true) { 
            newGrid();
            print();
            try {
                Thread.sleep(2000); // Sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }                      

    }
    private void print(){
        for(int i=0;i<this.rows;i++){
            for (int j = 0; j < this.columns; j++) {
                System.err.print(grid[i][j]+" ");
            }System.err.println();
        }System.err.println("------------------");
    }
    public static void main(String[] args) {
        game_of_life board=new game_of_life(10, 10);
        board.game();
    }
}
