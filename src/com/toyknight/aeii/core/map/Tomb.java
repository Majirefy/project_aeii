
package com.toyknight.aeii.core.map;

import java.awt.Point;

/**
 *
 * @author toyknight
 */
public class Tomb extends Point {
	
	public Tomb(int x, int y) {
		super(x, y);
	}
	
	public Tomb(Tomb tomb) {
		this(tomb.x, tomb.y);
	}
	
}
