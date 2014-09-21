package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author toyknight
 */
public class UnitAttackAnimation extends UnitAnimation {

	private final int ts;
	private final int damage;
	private final Unit attacker;
	private final Unit defender;
	private final Random random;

	private int current_frame;
	private int unit_dx;
	private int unit_dy;

	public UnitAttackAnimation(Unit attacker, Unit defender, int damage, int ts) {
		super(null, defender.getX(), defender.getY());
		addLocation(attacker.getX(), attacker.getY());
		this.ts = ts;
		this.current_frame = 0;
		this.damage = damage;
		this.attacker = new Unit(attacker);
		this.defender = new Unit(defender);
		random = new Random(System.currentTimeMillis());
	}

	@Override
	protected void doUpdate() {
		if (current_frame < 5) {
			current_frame++;
		} else {
			complete();
		}
	}

	@Override
	public void update() {
		unit_dx = random.nextInt(ts / 12) - ts / 24;
		unit_dy = random.nextInt(ts / 12) - ts / 24;
		super.update();
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int offset = (ts - ts / 24 * 20) / 2;
		int sx_attacker = canvas.getXOnCanvas(attacker.getX());
		int sy_attacker = canvas.getYOnCanvas(attacker.getY());
		int sx_defender = canvas.getXOnCanvas(defender.getX());
		int sy_defender = canvas.getYOnCanvas(defender.getY());
		//paint attacker and defender
		UnitPainter.paint(g, attacker, sx_attacker, sy_attacker, ts);
		UnitPainter.paint(g, defender, sx_defender + unit_dx, sy_defender + unit_dy, ts);
		//paint spark
		g.drawImage(ResourceManager.getAttackSparkImage(current_frame), sx_defender + offset, sy_defender + offset, null);
		//paint damage
		int damage_dx = (ts - CharPainter.getLNumberWidth(damage, true)) / 2;
		int damage_dy = ts - CharPainter.getLCharHeight();
		CharPainter.paintNegativeLNumber(g, damage, sx_defender + damage_dx, sy_defender + damage_dy);
	}

}
