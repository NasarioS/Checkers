
import javafx.application.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Board extends Application{
	//8x8 grid with each title size being 100px
	public static final int TILE_SIZE = 75;
	public static final int WIDTH = 8;
	public static final int HIGHT = 8;
	
	private Group pGroup = new Group();
	private Group tGroup = new Group();
	
	private Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(WIDTH * TILE_SIZE, HIGHT *TILE_SIZE);
		root.getChildren().addAll(tGroup, pGroup);
		
		for(int i = 0; i < WIDTH;i++) {
			for(int j = 0; j < HIGHT; j++) {
				Tile tile = new Tile((i+j)%2 == 0, i,j);
				tGroup.getChildren().add(tile);
			}
		}
		
		return root;
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
