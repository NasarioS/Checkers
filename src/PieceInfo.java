public enum PieceInfo {
  BLACK(1), RED(-1); 
  int move;
	//the direction of which the piece can move, 1 for up and 0 for down, red will be at the top of the board
  PieceInfo(int move){
	  this.move = move;
  }

}

