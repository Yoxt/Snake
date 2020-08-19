package main.java.Application;
import java.util.ArrayList;
import java.math.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.Model.*;
public class Game extends Application {

		private Map terrain;
		/**vitesse de simulation*/
		double tempo = 100;
		/**taille du terrain*/
		private int taille;
		/**taille d'une cellule en pixel*/
		private int espace = 25;
		
		//Terrain graphique
		private  static Rectangle [][] environnement; 
		
		public static List<Circle> Snake;
		public static boolean defaite =false;
		
		public Timeline littleCycle;
		//Compteur de cycle selon les cas
		public int counterCycle;
		public int counterFast;
		public int counterSlow;
		public int counterInvincible;
		
		@Override
		/**initialisation de l'application graphique*/
		public void start(Stage primaryStage) {
			this.taille = 25;
			Snake = new ArrayList<Circle>();
			terrain = new Map(taille);
			taille = terrain.getSize();
			construireScene(primaryStage);
		
		}



		/**
		 * Construction de la scene configuration de la simulation
		 * @param primaryStage
		 */
		void construireScene(Stage primaryStage) 
		{
			//definir la scene principale
			Group root = new Group();
			Scene scene = new Scene(root, 2*espace + taille*espace, 2*espace + taille*espace, Color.BLACK);
			
			primaryStage.setTitle("Snake");
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("design/snake.png"));

			//definir les acteurs et les habiller
			Game.environnement = new Rectangle[taille][taille];
			
			dessinEnvironnement(root);
			dessinSnake(root);
			//afficher le theatre
			primaryStage.show();
			Player Snake = terrain.getSnake();
			Snake.setDirection(Direction.SUD);
			this.counterCycle = 0;
			this.counterFast = 0;
			this.counterSlow = 0;
			//-----lancer le timer pour animer l'environement
			this.littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo), new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
				if(defaite != true) {
					if(Snake.getEtat() != null) {
						//Effets des différents objet à gerer
						switch(Snake.getEtat()) {
							case INVINCIBLE:
								counterInvincible++;
								if(counterInvincible == 60) {
									Snake.getDessin(0).getDessin().setFill(Color.DIMGREY);
									counterInvincible = 0;
									Snake.setEtat(null);
								}
								break;
							case SLOW:
								counterSlow++;
								if(counterSlow == 50) {
									counterSlow=0;
									Snake.getDessin(0).getDessin().setFill(Color.DIMGREY);
								//	tempo = 100;
									Snake.setEtat(null);
								}

								break;
							case FAST:
								counterFast++;
								if(counterFast == 50) {
								counterFast = 0;
								Snake.getDessin(0).getDessin().setFill(Color.DIMGREY);
							//	tempo = 100;
								Snake.setEtat(null);
								}
								break;
						}
					}
					counterCycle++;
					//On met à jour le terrain
					Snake.UpdatePoint();
					updateTerrain(root);
					
					//On gere les temps des differents objets ajouté sur le terrain
					if(counterCycle % 50 == 0) {
						addNouriture(root);
						Snake.setScore(10);
					}
					if(counterCycle % 80 == 0) {
						addObjet(root);
						Snake.setScore(1);
					}
					
					if(counterCycle==10000) {
						counterCycle = 0;
					}
					
					if(Snake.isDead()){
						defaite = true;
					}
					System.out.println("Score : " +Snake.getScore());
				}	
			}
			}));
			
			Circle dessinSnake = Snake.getDessin(0).getDessin();
			dessinSnake.requestFocus();
			dessinSnake.setOnKeyPressed(e->{
				
			  System.err.println(e.getCode());
			  //Permet de définir la direction du joueur à l'aide des touches
			  switch(e.getCode()) {
			    case UP: if(Snake.getDirection() != Direction.SUD)
			    	Snake.setDirection(Direction.NORD);break;
			    case LEFT: if(Snake.getDirection() != Direction.EST)
			    	Snake.setDirection(Direction.OUEST);break;
			    case DOWN: if(Snake.getDirection() != Direction.NORD)
			    	Snake.setDirection(Direction.SUD);break;
			    case RIGHT:if(Snake.getDirection()!= Direction.OUEST)
			    	Snake.setDirection(Direction.EST); break;
			  }
			});
			
			littleCycle.setCycleCount(Timeline.INDEFINITE);
		
			littleCycle.play();
			}

	
		/** 
		 *creation des cellules 
		 */
		void dessinEnvironnement(Group root)
		{
			Case[][] grille = terrain.getTerrain();
			// chaque parcelle de l'environnement est verte
			for(int i=0; i<taille; i++)
				for(int j=0; j<taille; j++)
				{
					Game.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);
					Game.environnement[i][j].setFill(Color.YELLOWGREEN);
					
					root.getChildren().add(Game.environnement[i][j]);
					
					}	

		
		}
		
		/**
		 * Créé graphiqeument le dessin du personnage
		 * @param root
		 */
		void dessinSnake(Group root) {
			
			for(Dessin dessin : terrain.getSnake().getDessin()) {
				terrain.getSnake().setPas(espace);
				root.getChildren().add(dessin.getDessin());
			}
			//petit effet de flou général
			root.setEffect(new BoxBlur(2, 2, 5));
		}
		
		/**
		 * Met à jour le terrain dans le cas ou un objet à été ramassé
		 * @param root
		 */
		public void updateTerrain(Group root) {
			
			Case c = this.terrain.checkTerrain();
			if(c != null) {
				this.terrain.getSnake().setScore(100);
				//On ajoute le nouveau dessin a la vue dans le cas de la nourriture a été mangé
				if(c.HasJustChanged() == true && c.ObjetJustChanged() == false) {
					
					int indexNewDessin = terrain.getSnake().getDessin().size() - 1;
					
					Dessin newDessin = terrain.getSnake().getDessin(indexNewDessin);
					root.getChildren().add(newDessin.getDessin());
					Game.environnement[c.getX()][c.getY()].setFill(Color.YELLOWGREEN);
					c.Changed(false);

				}
				//Dans le cas d'un objet ramassé , on assigne l'état désiré selon l'objet
				if(c.ObjetJustChanged() == true) {
					ObjetEnum objet = c.getObjet();
					switch(objet) {
					case INVINCIBLE:
						this.terrain.getSnake().setEtat(objet);
						Dessin d = terrain.getSnake().getDessin(0);
						d.getDessin().setFill(Color.CADETBLUE);
						c.setObjetJustChanged(false);
						c.setObjet(false);
						break;
					case FAST:
						this.terrain.getSnake().setEtat(objet);
					    Dessin d1 = terrain.getSnake().getDessin(0);
						d1.getDessin().setFill(Color.DARKRED);
						c.setObjetJustChanged(false);
						c.setObjet(false);
						break;
					case SLOW:			
						this.terrain.getSnake().setEtat(objet);
						tempo = tempo/2;
					    Dessin d2 = terrain.getSnake().getDessin(0);
						d2.getDessin().setFill(Color.DARKBLUE);
						c.setObjetJustChanged(false);
						c.setObjet(false);				
						break;			
					}
					Game.environnement[c.getX()][c.getY()].setFill(Color.YELLOWGREEN);

					}
				}
			
			}
				
		
		  /**
		   *  Colore une case aleatoire sur le terrain et y ajoute la nouriture
		   * @param root
		   */
		  public void addNouriture(Group root) {
				int min = 0;
				int max = taille - 2;
				int x = ThreadLocalRandom.current().nextInt(min, max);
				int y = ThreadLocalRandom.current().nextInt(min, max);			
				this.terrain.setNourriture(x, y);
				Color food = Color.BLACK;
				Game.environnement[x][y] = new Rectangle((x+1)*(espace), (y+2)*(espace), espace/4, espace/4);
				Game.environnement[x][y].setFill(food);
				root.getChildren().add(Game.environnement[x][y]);
		
		}
		
		/** Colore une case sur le terrain et y ajoute un objet aléatoire**/
		public void addObjet(Group root) {
			int min = 0;
			int max = taille - 2;
			int x = ThreadLocalRandom.current().nextInt(min, max);
			int y = ThreadLocalRandom.current().nextInt(min, max);			
			int rand = ThreadLocalRandom.current().nextInt(1,4);
			Color food = null;

			switch(rand) {
			case 1 : this.terrain.setObjet(x, y, ObjetEnum.FAST);
				food = Color.DARKRED;
				break;
			case 2 : this.terrain.setObjet(x, y, ObjetEnum.INVINCIBLE);
			 	food = Color.AQUAMARINE;
				break;
			case 3 : this.terrain.setObjet(x, y, ObjetEnum.SLOW);
				food = Color.BLUE;
				break;		
			}
			Game.environnement[x][y] = new Rectangle((x+1)*(espace), (y+2)*(espace), espace/3, espace/3);
			Game.environnement[x][y].setFill(food);
			root.getChildren().add(Game.environnement[x][y]);
		
		}


	    
		/**lancement de l'application*/
		public static void main(String[] args) {
			launch(args);
		}
		
	}


