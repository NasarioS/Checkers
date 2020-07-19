import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

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
		//btm is the bottom of the stack
		Ellipse btm = new Ellipse(Board.TILE_SIZE * 0.314, Board.TILE_SIZE * 0.314);
		// 0.314 is the rel rad to the tile size
		
		
		move(x,y);
		
		if(piece == PieceInfo.RED) {
			btm.setFill(Color.RED);
		}
		else {
			btm.setFill(Color.WHITE);
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
	public void setKing() {
		if(!king) {
			king = true;
			this.getChildren().add(new Text("KING"));
		}
	}
}
