package Miyazaki;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *攻撃に関する便利なメソッドをまとめたクラス
 *@author Arsenic
 */
public class Scan {

	private AdvancedRobot robot;
	private int notFoundCount = 0;
	
	Scan(AdvancedRobot r) {
		robot = r;
	}
	
	public int search() {
		robot.setTurnRadarRight(45);
		notFoundCount++;
		return notFoundCount;
	}
	
	void bind(ScannedRobotEvent e) {
		double absoluteBearing = e.getBearing() + robot.getHeading();
		double bearingFromRadar = normalRelativeAngleDegrees(absoluteBearing - robot.getRadarHeading());
		notFoundCount = 0;
		if (0 < bearingFromRadar) robot.setTurnRadarRight(bearingFromRadar + 45/2);
		else if (bearingFromRadar < 0) robot.setTurnRadarRight(bearingFromRadar - 45/2);
		else robot.setTurnRadarRight(0.0);
	}
	
}
