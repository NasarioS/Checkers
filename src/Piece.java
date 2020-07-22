import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
//knows mouse press and the old x and Y pos (current before move positions)
public class Piece extends StackPane {
	private PieceInfo piece;
	private boolean king;
	private double mouseX, mouseY;
	private double oldX, oldY;
	
	public PieceInfo getInfo() { return this.piece; } 
	public double getOldX() { return oldX; }
	public double getOldY() { return oldY; }
	public boolean getKing() { return king; }
	
	Piece(PieceInfo piece, int x, int y){
		king = false;
		this.piece = piece;
		//btm is the bottom of the stack, "KING" will be on top, checkers pieces will be on the btm of the stack
		Ellipse btm = new Ellipse(Board.TILE_SIZE * 0.314, Board.TILE_SIZE * 0.314);
		// 0.314 is the rel rad to the tile size
		
		
		move(x,y);
		
		if(piece == PieceInfo.RED) {
			btm.setFill(Color.RED);
		}
		else {
			btm.setFill(Color.BLACK);
		}
	
		btm.setStroke(Color.GREY);
		btm.setStrokeWidth(Board.TILE_SIZE * .05);
		//centering piece in square
		//trial and error dividing by 6 nearly centers the piece
		this.setTranslateX(Board.TILE_SIZE / 6);
		this.setTranslateY(Board.TILE_SIZE / 6);
		
		setOnMousePressed(e -> {
			this.mouseX = e.getScreenX();
			this.mouseY = e.getScreenY();
			
		}); 
		setOnMouseDragged( e->{
			relocate(e.getScreenX() - this.mouseX + this.oldX, e.getScreenY() - this.mouseY + this.oldY);
		});
		
		this.getChildren().add(btm);
	}
	// moving a piece without eating it, it has to be removed from the board so the action is done in the Borad.java 
	public void move(int x, int y) {
		this.oldX = (Board.TILE_SIZE * x);
		this.oldY = (Board.TILE_SIZE * y);
		this.relocate(this.oldX, this.oldY);
	}
	public String toString() {
		return this.piece.toString();
	}
	
	public void stopMove() {
		relocate(oldX, oldY);
	}
	// checks to see if the piece is a king if not make it a king, if do nothing
	public void setKing() {
		if(!king) {
			king = true;
			Text text = new Text("KING");
			//black text on a black piece -> can not see the word "king" so changed its color on black pieces
			if(this.getInfo() == PieceInfo.BLACK)
				text.setFill(Color.RED);
			this.getChildren().add(text);
		}
	}
}
