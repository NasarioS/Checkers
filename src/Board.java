
import javax.swing.GroupLayout.Alignment;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
public class Board extends Application{
	//8x8 grid with each title size being 75px
	public static final int TILE_SIZE = 75;
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	private Tile[][] board  = new Tile[WIDTH][HEIGHT];
	private int turn = 1;
	private Group pGroup = new Group();
	private Group tGroup = new Group();
	private Pane root = new Pane();
	private int redP, blackP;
	private Text playerColor;
	private Scene menu, scene, endGame;
	private Stage stage;
	
	private Button createContent() {
	root.setPrefSize(WIDTH * TILE_SIZE + TILE_SIZE, HEIGHT * TILE_SIZE);
	Button b = new Button("This Is a 2 Player Cherckers Game\nClick Me to Begin!");
	b.setTextAlignment(TextAlignment.CENTER);
	b.setOnAction(e -> {
		root.getChildren().clear();
		createGame();
	});
	root.getChildren().add(b);
	return b;
}

	private Parent createGame() {
		redP = 12;
		blackP = 12;
		Text turnp = new Text(WIDTH * TILE_SIZE, 15, "Turn:");
		playerColor = new Text(WIDTH * TILE_SIZE+30, 15, "RED");
		playerColor.setFont(Font.font("Impact", FontWeight.BOLD, 12));
		playerColor.setFill(Color.RED);
		Button btn = new Button("New Game");
		btn.setLayoutX(WIDTH*TILE_SIZE);
		btn.setLayoutY(playerColor.getLayoutBounds().getHeight() + 5);
		
		btn.setOnAction(e -> {
			GameMenu();
			
		});
		root.getChildren().addAll(tGroup, pGroup, turnp, playerColor, btn);
		
		//(i,j) -> (x,y)
		for(int i = 0; i < WIDTH;i++) {
			for(int j = 0; j < HEIGHT; j++) {
				Tile tile = new Tile((i+j)%2 == 0, i,j);
				board[i][j] = tile;
				tGroup.getChildren().add(tile);
				
				Piece p = null;
				//if y is less than 3 and the tile is odd place a red piece
				if(j < 3 && (i+j)%2 == 1 ) {
					p = makePiece(PieceInfo.BLACK, i, j);
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
	
	private void GameMenu() {
		Button yes = new Button("Yes");
		Button no = new Button ("No");
		Text newGameText = new Text("Are You Sure \nYou Want To Start a New Game?");
		

		Pane menuPane = new Pane();
		menu = new Scene(menuPane);
		menuPane.getChildren().addAll(yes,no, newGameText);
		menuPane.setPrefSize(WIDTH*TILE_SIZE/2, HEIGHT*TILE_SIZE/4);
		stage.setScene(menu);
		stage.show();
		newGameText.setTextAlignment(TextAlignment.CENTER);
		
		newGameText.setLayoutX(WIDTH*TILE_SIZE/4 - newGameText.getLayoutBounds().getWidth()/2);
		
		yes.setLayoutX(WIDTH*TILE_SIZE/4 - yes.getWidth());
		yes.setLayoutY(TILE_SIZE/2);
		
		no.setLayoutX(WIDTH*TILE_SIZE/4 + 5);
		no.setLayoutY(TILE_SIZE/2);
		
		yes.setOnAction(e->{
			root.getChildren().removeAll();
			root.getChildren().clear();
			tGroup.getChildren().removeAll();
			tGroup.getChildren().clear();
			pGroup.getChildren().removeAll();
			pGroup.getChildren().clear();
			menuPane.getChildren().removeAll();
			menuPane.getChildren().clear();

			board = new Tile[WIDTH][HEIGHT];
			createGame();
			stage.setScene(scene);
		});
		
		no.setOnAction( e -> {
			stage.setScene(scene);
		});
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
				
				if(other.getInfo() == PieceInfo.RED)
					redP--;
				else
					blackP--;
				if(redP == 0 || blackP == 0)
					gameOver();
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
			return new CanMove(MoveInfo.NO);
		}
		
		//board x and y of old location
		int bX = fromPixel(p.getOldX());
		int bY = fromPixel(p.getOldY());
		if(Math.abs(bX - newX) == 1 && (newY -bY == p.getInfo().move || p.getKing())) {
			if(turn == 1) {
				playerColor.setText("BLACK");
				playerColor.setFill(Color.BLACK);
				turn += -2;
			}
			else {
				playerColor.setText("RED");
				playerColor.setFill(Color.RED);
				turn += 2;
			}
			if(!p.getKing() && (p.getInfo() == PieceInfo.BLACK && newY == 7 || p.getInfo()== PieceInfo.RED && newY == 0))
				p.setKing();
			return new CanMove(MoveInfo.YES);
		}
		if(Math.abs(newX - bX ) == 2 && (newY - bY == (p.getInfo().move * 2) || p.getKing())){
			int middleX = bX + (newX - bX) /2;
			int middleY = bY + (newY - bY) /2;
			if(board[middleX][middleY].hasPiece() && board[middleX][middleY].getPiece().getInfo() != p.getInfo()) {
				if(turn == 1) { 
					playerColor.setText("BLACK");
					playerColor.setFill(Color.BLACK);
					turn += -2;
				}
				else {
					playerColor.setText("RED");
					playerColor.setFill(Color.RED);
					turn += 2;
				}
				if(!p.getKing() && (p.getInfo()== PieceInfo.BLACK && newY == 7 || p.getInfo()== PieceInfo.RED && newY == 0))
					p.setKing();
				return new CanMove(MoveInfo.EAT, board[middleX][middleY].getPiece());
			}
		}
		return new CanMove(MoveInfo.NO);
	}
	
	private void gameOver() {
		Pane p = new Pane();
		p.setPrefSize(WIDTH*TILE_SIZE/2, HEIGHT*TILE_SIZE/4);
		endGame = new Scene(p);
		stage.setScene(endGame);
		
		Text win = new Text("WINS!!");
		Text red = new Text("RED Player");
		red.setFill(Color.RED);
		
		Text black =  new Text("BLACK Player");
		Button startNew = new Button("New Game");
		Button exit =  new Button ("Exit");
		p.getChildren().addAll(win, startNew, exit);
		stage.show();
		
		red.setLayoutY(TILE_SIZE/3);
		black.setLayoutY(TILE_SIZE / 3);
		
		win.setLayoutX(WIDTH * TILE_SIZE /4 - win.getLayoutBounds().getWidth()/2);
		win.setLayoutY(TILE_SIZE * 2 /3);
		
		startNew.setLayoutX(WIDTH * TILE_SIZE /4 - win.getLayoutBounds().getWidth()*2);
		startNew.setLayoutY(TILE_SIZE);
		
		exit.setLayoutX(WIDTH * TILE_SIZE /4 + win.getLayoutBounds().getWidth()/2);
		exit.setLayoutY(TILE_SIZE);
		
		
		startNew.setOnAction(e -> {
			root.getChildren().removeAll();
			root.getChildren().clear();
			tGroup.getChildren().removeAll();
			tGroup.getChildren().clear();
			pGroup.getChildren().removeAll();
			pGroup.getChildren().clear();
			board = new Tile[WIDTH][HEIGHT];
			createGame();
			stage.setScene(scene);
		});
		
		exit.setOnAction(e-> {
			Stage stage = (Stage) exit.getScene().getWindow();
		    stage.close();
		});
		
		if(redP == 0) {
			p.getChildren().add(black);
			black.setLayoutX(WIDTH * TILE_SIZE / 4 - black.getLayoutBounds().getWidth() / 2);
		}
		else {
			p.getChildren().add(red);
			red.setLayoutX(WIDTH * TILE_SIZE/4 - red.getLayoutBounds().getWidth()/ 2);
		}
	}
	
	private int fromPixel(double x) {
		// by dividing the pixel by TILE_SIZE and casting to an int, will provide
		// the tile cord on the board
		return (int) ((x + TILE_SIZE/2)/TILE_SIZE);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		scene = new Scene(root);
		Button b = createContent();
		stage = primaryStage;
		primaryStage.setTitle("Checkers");
		primaryStage.setScene(scene);
		primaryStage.show();
		b.setLayoutX((WIDTH*TILE_SIZE/2) - b.getWidth()/2+(TILE_SIZE/2));
		b.setLayoutY((HEIGHT*TILE_SIZE/2) - b.getHeight()/2);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
