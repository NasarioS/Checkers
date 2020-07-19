
import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Board extends Application{
	//8x8 grid with each title size being 100px
	public static final int TILE_SIZE =75;
	public static final int WIDTH = 8;
	public static final int HIGHT = 8;
	private Tile[][] board  = new Tile[WIDTH][HIGHT];
	private int turn = 1;
	private Group pGroup = new Group();
	private Group tGroup = new Group();
	
	private Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(WIDTH * TILE_SIZE, HIGHT *TILE_SIZE);
		root.getChildren().addAll(tGroup, pGroup);
		//(i,j) -> (x,y)
		for(int i = 0; i < WIDTH;i++) {
			for(int j = 0; j < HIGHT; j++) {
				Tile tile = new Tile((i+j)%2 == 0, i,j);
				board[i][j] = tile;
				tGroup.getChildren().add(tile);
				
				Piece p = null;
				//if y is less than 3 and the tile is odd place a red piece
				if(j < 3 && (i+j)%2 == 1 ) {
					p = makePiece(PieceInfo.WHITE, i, j);
					board[i][j].setPiece(p);
				}
				if(j > 4 && (i+j)%2 == 1) {
					p = makePiece(PieceInfo.RED, i, j);
					board[i][j].setPiece(p);
				}
				
				if(p != null) {
					pGroup.getChildren().add(p);
				}
				
				
			}
		}
		
		return root;
	}
	
	
	private Piece makePiece(PieceInfo i, int x, int y) {
		Piece p = new Piece(i , x , y);
		p.setOnMouseReleased(e -> {
			int newX = fromPixel(p.getLayoutX());
			int newY = fromPixel(p.getLayoutY());
			
			int xS = fromPixel(p.getOldX());
			int yS = fromPixel(p.getOldY());
			
			CanMove result = tryMove(p, newX, newY);
			
			switch(result.getInfo()) {
			case NO: 
				p.stopMove();
				break;
			case YES: 
				p.move(newX, newY);
				board[xS][yS].setPiece(null);
				board[newX][newY].setPiece(p);
				break;
			case EAT: 
				p.move(newX, newY);
				board[xS][yS].setPiece(null);
				board[newX][newY].setPiece(p);
				
				Piece other =  result.getPiece();
				board[fromPixel(other.getOldX())][fromPixel(other.getOldY())].setPiece(null);
				pGroup.getChildren().remove(other);
				break;
			}
		});
		return p;
	}
	//x and y of location to move too the check if can move there
	private CanMove tryMove(Piece p, int newX, int newY) {
		//test if it was a piece or is a white tile
		if(turn == p.getInfo().move) {
			return new CanMove(MoveInfo.NO);
		}
		if((newX + newY)%2 == 0 || board[newX][newY].hasPiece()) {
			System.out.println(newX + " " + newY);
			return new CanMove(MoveInfo.NO);
		}
		
		//board x and y of old location
		int bX = fromPixel(p.getOldX());
		int bY = fromPixel(p.getOldY());
		
		if(Math.abs(bX - newX) == 1 && (bY - newY == p.getInfo().move || p.getKing())) {
			if(turn == 1)
				turn += -2;
			else
				turn += 2;
			if(!p.getKing() && (p.getInfo()== PieceInfo.WHITE && newY == 0 || p.getInfo()== PieceInfo.RED && newY == 7))
				p.setKing();
			return new CanMove(MoveInfo.YES);
		}
		if(Math.abs(newX - bX ) == 2 && (bY - newY == (p.getInfo().move * 2) || p.getKing())){
			int middleX = bX + (newX - bX) /2;
			int middleY = bY + (newY - bY) /2;
			if(board[middleX][middleY].hasPiece() && board[middleX][middleY].getPiece().getInfo() != p.getInfo()) {
				if(turn == 1) 
					turn += -2;
				else
					turn += 2;
				if(!p.getKing() && (p.getInfo()== PieceInfo.WHITE && newY == 0 || p.getInfo()== PieceInfo.RED && newY == 7))
					p.setKing();
				return new CanMove(MoveInfo.EAT, board[middleX][middleY].getPiece());
			}
		}
		System.out.println("(x,y): (" + bX + ", " + bY + ")");
		System.out.println("(new x, new y): (" + newX + ", " + newY + ")");
		return new CanMove(MoveInfo.NO);
	}
	
	private int fromPixel(double x) {
		// by dividing the pixel by TILE_SIZE and casting to an int, will provide
		// the tile cord on the board
		return (int) ((x + TILE_SIZE/2)/TILE_SIZE);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		primaryStage.setTitle("Checkers");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
