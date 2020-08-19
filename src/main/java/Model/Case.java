package main.java.Model;

public class Case {

	private int X;
	private int Y;
	private boolean isPlayer;
	private boolean isNourriture;
	private boolean isObjet;


	private ObjetEnum Objet;
	private boolean hasJustChanged;
	private boolean objetJustChanged;
	static Case [][]grille;

	public Case() {
		
	}
	public Case(int x, int y) {
		X=x;
		Y=y;
		hasJustChanged = false;
		objetJustChanged =false;
		isNourriture =false;
	}
	
	public Case(int x, int y,Case[][] grille) {
		super();
		X = x;
		Y = y;
		Case.grille = grille;
		this.hasJustChanged = false;
		isNourriture = false;
		objetJustChanged =false;
		}
	

	public int getX() {
		return X;
	}
	
	public void setLocation(int x , int y){
		this.X = x;
		this.Y = y;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}

	public boolean isPlayer() {
		return isPlayer;
	}
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	public boolean HasJustChanged() {
		return hasJustChanged;
	}

	public void Changed(boolean value) {
		this.hasJustChanged = value;
	}

	public boolean isNourriture() {
		return isNourriture;
	}

	public void setNourriture(boolean isNourriture) {
		this.isNourriture = isNourriture;
	}
	public boolean isObjet() {
		return isObjet;
	}
	public void setObjet(boolean isObjet) {
		this.isObjet = isObjet;
	}
	public ObjetEnum getObjet() {
		return Objet;
	}
	public void setObjet(ObjetEnum objet) {
		Objet = objet;
	}
	public boolean ObjetJustChanged() {
		return objetJustChanged;
	}
	public void setObjetJustChanged(boolean objetJustChanged) {
		this.objetJustChanged = objetJustChanged;
	}
	
	
}
