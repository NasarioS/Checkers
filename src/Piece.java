import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends Ellipse {
	private PieceInfo piece;
	
	private double mouseX, mouseY;
	private double oldX, oldY;
	

	Piece(PieceInfo piece, int x, int y){
		this.piece = piece;
		// 0.314 is the rel rad to the tile size
		this.setRadiusX(Board.TILE_SIZE * 0.314); 
		this.setRadiusY (Board.TILE_SIZE * 0.314);
		
		move(x,y);
		
		if(piece == PieceInfo.RED) {
			this.setFill(Color.RED);
		}
		else {
			this.setFill(Color.WHITE);
		}
	
		this.setStroke(Color.GREY);
		this.setStrokeWidth(Board.TILE_SIZE * .05);
		//centering piece in square
		//trial and error dividing by 6 nearly centers the piece
		this.setTranslateX(Board.TILE_SIZE / 6);
		this.setTranslateY(Board.TILE_SIZE / 6);
		
		setOnMousePressed(e -> {
			this.mouseX = e.getScreenX();
			this.mouseY = e.getSceneY();
			
			System.out.println(this.toString() + " Piece Place on Board: (" + this.oldX/Board.TILE_SIZE + ", " + this.oldY/Board.TILE_SIZE + ").");
		});
		setOnMouseDragged( e->{
			
		});
	}
	private void move(int x, int y) {
		this.oldX = (Board.TILE_SIZE * x);
		this.oldY = (Board.TILE_SIZE * y);
		this.relocate(this.oldX, this.oldY);
	}
	public String toString() {
		return this.piece.toString();
	}
}
