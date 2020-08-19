package main.java.Model;
import main.java.Model.Case;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {

	private List<Point> listPoint;
	private List<Dessin> dessin;
	private List<Point> listOldPoint;
	private Point p;
	private String nickname;
	private List<Case> position;
	private int score;
	private int sizeMap;
	/**taille d'un pas*/
	private int pas;
	private Direction direction;
	/**lien vers le terrain dans lequel se trouve la  fourmi*/
	private Map terrain;
	private int taille;
	private boolean hasGrow;
	private boolean dead;
	private ObjetEnum etat;
	
	
	public Player(int i, int j, Map map) {
	
	this.position = new ArrayList<Case>();
	this.listPoint = new ArrayList<Point>();
	this.listOldPoint = new ArrayList<Point>();
	this.hasGrow = false;
	this.dessin = new ArrayList<Dessin>();
	this.dessin.add(new Dessin());
	this.dessin.add(new Dessin());
	this.dessin.add(new Dessin());


	position.add(new Case(i,j));
	position.add(new Case(i+1,j));
	position.add(new Case(i+2,j));	
	p = new Point(i,j);
	
	listPoint.add(p);
	listPoint.add(new Point(i+1,j));
	listPoint.add(new Point(i+2,j));

	this.sizeMap = map.getSize();
	this.terrain = map;
	this.dead = false;
	this.score = 0;
	}
	

	public List<Point> getListPoint() {
		return listPoint;
	}
	public void setListPoint(List<Point> listPoint) {
		this.listPoint = listPoint;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSize() {
		return sizeMap;
	}
	public void setSize(int size) {
		this.sizeMap = size;
	}
	public int getScore() {
		return score;
	}
	public void addDessin(Dessin dessin) {
		this.dessin.add(dessin);
	}
	public void setScore(int score) {
		this.score += score;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public List<Dessin> getDessin() {
		return dessin;
	}
	
	public Dessin getDessin(int index) {
		return this.dessin.get(index);
	}
	
	public boolean isDead() {
		return this.dead;
	}
	public void setDessin(Dessin dessin) {
		this.dessin.add(dessin);
	}
	public int getPas() {
		return pas;
	}
	public void setPas(int pas) {
		this.pas = pas;
	}
	public void AddPoint(Point point) {
		this.listPoint.add(point);
	}
	
	/**
	 * Met à jour toutes les positions et verifie si de la nouriture ou un objet ont été trouvé.
	 */
	public void UpdatePoint() {
		for(int i=0;i<this.dessin.size();i++) {
			if(i == 0) {
				Point oldPoint = new Point(this.listPoint.get(i).x,this.listPoint.get(i).y);
				listOldPoint.add(oldPoint);
				Point newPoint = bougerVersDirection();
				Case newCase = this.terrain.getCase(p.x, p.y);

				this.listPoint.get(i).setLocation(newPoint);
				this.position.get(i).setLocation(p.x,p.y);
				if(newCase.isNourriture() == true) {
					this.hasGrow = true;
					newCase.Changed(true); 
					newCase.setNourriture(false);
					listPoint.add(new Point(p.x,p.y));
					position.add(new Case(p.x,p.y));
					this.dessin.add(new Dessin());
				}
				if(newCase.isObjet() == true) {
					newCase.setObjetJustChanged(true); 
					newCase.setObjet(false);
					this.etat = newCase.getObjet();
				}
			}
			else {
				Point point = new Point(this.listPoint.get(i).x,this.listPoint.get(i).y);
				listOldPoint.add(point);
				
				Point tmp = new Point(this.listOldPoint.get(i-1).x,this.listOldPoint.get(i-1).y);
				Point p1 = this.listPoint.get(i);
				p1.setLocation(tmp.x,tmp.y);
				Dessin dessinActuel = this.dessin.get(i);
				double newCenterX=this.dessin.get(i-1).getOldDessin().getCenterX();
				double newCenterY = this.dessin.get(i-1).getOldDessin().getCenterY();
				double centerX = dessin.get(i).getDessin().getCenterX(); 
				double centerY = dessin.get(i).getDessin().getCenterY();
				this.position.get(i).setX(tmp.x);
				this.position.get(i).setY(tmp.y);
				dessinActuel.setOldDessin(new Circle(centerX,centerY,5,Color.CHOCOLATE));
				dessinActuel.getDessin().setCenterX(newCenterX);
				dessinActuel.getDessin().setCenterY(newCenterY);
				}
			}
			listOldPoint.clear();
			
			if(this.etat != null && this.etat != ObjetEnum.INVINCIBLE) {
				this.dead = checkPerdu();
			}
		}
	
	public void setPos(Case pos) {
	
	}
	/**
	 * Permet de verifier si le joueur a perdu ou non
	 * @return
	 */
	public boolean checkPerdu() {
			Case joueur = this.position.get(0);
				int cpt = 0;
				List<Case> toCheck = new ArrayList<Case>();
				toCheck.addAll(this.position);
				toCheck.remove(0);
				for(Case c : toCheck) {
					if(cpt != 0) {
						if(c.getX() == joueur.getX() && c.getY() == joueur.getY()) {
							return true;
						}
					}
					cpt++;

				}
				return false;
	}
	
	
	/**
	 * Permet de deplacer le point vers une direction;
	 * @return
	 */
	public Point bougerVersDirection()
	{
		Case cell = getNextCellule(direction);
		int stockX=cell.getX();
		int stockY=cell.getY();
		p.x = cell.getX();
		p.y = cell.getY();
		if(dessin != null && dessin.size()>0) {
			double centerX = dessin.get(0).getDessin().getCenterX();
			double centerY = dessin.get(0).getDessin().getCenterY();
		dessin.get(0).setOldDessin(new Circle(centerX,centerY,5,Color.CHOCOLATE));
		}
		taille = terrain.getSize();
			Point p =null;
			Case[][] grille = terrain.getTerrain();
			if(position != null) {
				 p = new Point(stockX,stockY);
				
			}

			dessin.get(0).getDessin().setCenterX((p.x+1) * pas);
			dessin.get(0).getDessin().setCenterY((p.y+2) * pas);
			return p;
	}
	
	/**
	 * Permet de recuperer la prochaine case selon la direction
	 * @param dir
	 * @return
	 */
	private Case getNextCellule(Direction dir)
	{
		Case cell = null;
		Point newPoint = Direction.getNextPoint(p, dir);
		if ((newPoint.x>=0 && newPoint.x < sizeMap) && (newPoint.y>=0 && newPoint.y<sizeMap))
		{
			Case[][] grille = terrain.getTerrain();
			cell = grille[newPoint.x][newPoint.y];
		}

		return cell;
	}
	
	
	public Point getP() {
		return p;
	}
	public void setP(Point p) {
		this.p = p;
	}
	public Map getTerrain() {
		return terrain;
	}
	public void setTerrain(Map terrain) {
		this.terrain = terrain;
	}
	public int getTaille() {
		return taille;
	}
	public void setTaille(int taille) {
		this.taille = taille;
	}
	public Direction getDirection() {
		return direction;
	}
	public boolean isHasGrow() {
		return hasGrow;
	}
	public void setHasGrow(boolean hasGrow) {
		this.hasGrow = hasGrow;
	}

	public ObjetEnum getEtat() {
		return etat;
	}

	public void setEtat(ObjetEnum etat) {
		this.etat = etat;
	}
}
