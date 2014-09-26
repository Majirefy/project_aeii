
package com.toyknight.aeii.core.map;

import java.awt.Point;

/**
 *
 * @author toyknight
 */
public class Tomb extends Point {
	
	private int remains = 1;
	
	public Tomb(int x, int y) {
		super(x, y);
	}
	
	public Tomb(Tomb tomb) {
		this(tomb.x, tomb.y);
	}
	
	public void update() {
		if(remains >= 0) {
			remains --;
		}
	}
	
	public int getRemains() {
		return remains;
	}
	
}
