package main.java.Model;

import java.awt.Point;

/**type enumere evolue codant les directions des 4 points cardinaux et des 4 points sous forme de vecteur*/
public enum Direction {
	NORD(0, 0, -1), EST(2,1,0), SUD(4,0,1), OUEST(6,-1,0);
	/**no d'indice de la direction*/
	int no;
	/**vecteur directeur*/
	Point v;
	
	/**
	 * @param _no no d'indice de la direction
	 * @param _x coordonnee x du vecteur de la direction
	 * @param _y coordonnee y du vecteur de la direction
	 */
	Direction(int _no,int _x, int _y){no = _no; v = new Point (_x, _y);}
	

	
	/**
	 * @param a coordonnee x du vecteur dont on souhaite la direction
	 * @param b coordonnee y du vecteur dont on souhaite la direction
	 * @return la direction correspondante au vecteur (a,b)*/
	static Point getNextPoint(Point p, Direction d)
	{
		Point result = new Point( p.x + d.v.x, p.y + d.v.y);
		return result;
	}
	
	
} 