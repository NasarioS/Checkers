import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
// visual for tiles on the board
public class Tile extends Rectangle {
	private Piece p;
	public boolean hasPiece() {
		if (p==null)
			return false;
		return true;
	}
	public Piece getPiece() { return p; }
	public void setPiece(Piece p) {
		this.p = p;
	}
	public Tile(boolean white, int x, int y ) {
		this.setHeight(Board.TILE_SIZE);
		this.setWidth(Board.TILE_SIZE);
		
		relocate(x * Board.TILE_SIZE, y * Board.TILE_SIZE);
		
		if(white) {
			this.setFill(Color.BLANCHEDALMOND);
		}
		else {
			this.setFill(Color.BLACK);
		}
	}

}
