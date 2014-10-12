
package com.toyknight.aeii.core.player;

import com.toyknight.aeii.core.robot.Robot;

/**
 *
 * @author toyknight
 */
public class RobotPlayer extends Player {
	
	private final Robot robot;
	
	public RobotPlayer(Robot robot) {
		this.robot = robot;
	}
	
	public void start() {
		new Thread(robot).start();
	}
	
}
