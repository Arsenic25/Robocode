package Miyazaki;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *移動に関する便利なメソッドをまとめたクラス
 *@author Arsenic
 */
public class Drive {

	private AdvancedRobot robot;

	Drive(AdvancedRobot r) {
		robot = r;
	}
	
	public double absoluteAngle(double r) {
		r = r % 360.0;
		if (r < 0.0) r = 360.0 + r;
		return r;
	}

	public void vector(double x, double y) {
		double targetAngle = absoluteAngle(Math.toDegrees(Math.atan2(x, y)));
		double errorAngle = targetAngle - robot.getHeading();
		double errorDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		if (errorDistance == 0.0) return;
		errorAngle = normalRelativeAngleDegrees(errorAngle);
/*		if (Math.abs(errorAngle) <= 90.0)*/ robot.setTurnRight(errorAngle);
//		else robot.setTurnLeft(errorAngle - 90.0);
		robot.setAhead(errorDistance);
	}

	public double go(double x, double y) {
		double errorX = x - robot.getX(), errorY = y - robot.getY();
		double errorDistance = Math.sqrt(Math.pow(errorX, 2) + Math.pow(errorY, 2));
		if (x < 0 || robot.getBattleFieldWidth() < x || y < 0 || robot.getBattleFieldHeight() < y) return Double.NaN;
		vector(errorX, errorY);
		return errorDistance;
	}

	public double relative(double x, double y) {
		double error = go(robot.getBattleFieldWidth() * x, robot.getBattleFieldHeight() * y);
		return error;
	}

}