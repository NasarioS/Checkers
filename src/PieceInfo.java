public enum PieceInfo {
  BLACK(1), RED(-1); 
  int move;
	//the direction of which the piece can move, 1 for up and -1 for down, btm will be at the top of the board
  PieceInfo(int move){
	  this.move = move;
  }

}

