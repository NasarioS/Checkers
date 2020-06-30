
public class CanMove {
	private MoveInfo moveInfo;
	private Piece p;
	
	public Piece getPiece() {
		return this.p;
	}
	public MoveInfo getInfo() {
		return this.moveInfo;
	}
	public CanMove(MoveInfo moveInfo) {
		this.moveInfo = moveInfo;
		this.p = null;
	}
	public CanMove(MoveInfo moveInfo, Piece p) {
		this.moveInfo = moveInfo;
		this.p = p;
	}
}
