import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends Ellipse {
	private PieceInfo piece;
	//x,y cords on the board
	
	
	Piece(PieceInfo piece, int x, int y){
		this.piece = piece;
		// 0.314 is the rel rad to the tile size
		this.setRadiusX(Board.TILE_SIZE * 0.314); 
		this.setRadiusY (Board.TILE_SIZE * 0.314);
		
		this.relocate(Board.TILE_SIZE * x, Board.TILE_SIZE * y);
		
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
	}
}
