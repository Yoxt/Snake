package main.java.Model;
import java.util.concurrent.ThreadLocalRandom;

import main.java.Model.Case;

public class Map {

	private Case[][] terrain;
	private Player snake;
	private int size;


	public Map()
	{
	}
	
	public Map(int size)
	{
		this.size= size;
		terrain = new Case[size][size];
		for(int i=0;i<terrain.length;i++) {
			for(int j=0;j<terrain.length;j++) {
				terrain[i][j] = new Case(i,j,terrain);
			}
		}
		initSnake();
	
	}

	private void initSnake()
	{
		
	snake = new Player(10,10 ,this);
		
	}
	
	public int getSize() {
		return size;
	}
	
	public void setNourriture(int x,int y) {
		this.terrain[x][y].setNourriture(true);
	}
	
	public void setObjet(int x,int y,ObjetEnum objet) {
		this.terrain[x][y].setObjet(true);
		int isObjet = ThreadLocalRandom.current().nextInt(0, 3);
		this.terrain[x][y].setObjet(objet);
	}
	
	
	/**
	 * Verifie si une case à été modifié , la retourne si oui
	 * @return
	 */
	public Case checkTerrain() {
	
		for(int i = 0;i<this.size;i++) {
			for(int j=0;j<this.size;j++){
				if(this.terrain[i][j].HasJustChanged()  && !this.terrain[i][j].isNourriture()
					|| this.terrain[i][j].ObjetJustChanged() && !this.terrain[i][j].isObjet()) {
					return this.terrain[i][j];
				}
			}
		}
		
		return null;
	}
	
	
	public Case[][] getTerrain() {
		return terrain;
	}
	public Case getCase(int x,int y) {
		return terrain[x][y];
	}
	public Player getSnake() {
		return snake;
	}

	public void setSnake(Player snake) {
		this.snake = snake;
	}
	
}
