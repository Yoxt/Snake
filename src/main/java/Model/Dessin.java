package main.java.Model;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dessin {
	
	private Circle dessin;
	private Circle oldDessin;
	private static int espace = 30;
	
	
	public Dessin() {
	this.dessin = new Circle(1,1, espace/3, Color.DIMGREY);
	}
	public Circle getDessin() {
		return dessin;
	}
	public void setDessin(Circle dessin) {
		this.dessin = dessin;
	}
	public Circle getOldDessin() {
		return oldDessin;
	}
	public void setOldDessin(Circle oldDessin) {
		this.oldDessin = oldDessin;
	}

}
