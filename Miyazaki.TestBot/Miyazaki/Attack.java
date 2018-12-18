package Miyazaki;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *攻撃に関する便利なメソッドをまとめたクラス
 *@author Arsenic
 */
public class Attack {

	private AdvancedRobot robot;
	public static final byte TYPE_FIXED = 0;
	public static final byte TYPE_LINEAR = 1;
	
	Attack(AdvancedRobot r) {
		robot = r;
	}
	
	public double absoluteAngle(double r) {
		r = r % 360.0;
		if (r < 0.0) r = 360.0 + r;
		return r;
	}
	
	public double setGunVector(double x, double y) {
		double targetAngle = absoluteAngle(Math.toDegrees(Math.atan2(x, y)));
		double errorAngle = targetAngle - robot.getGunHeading();
		errorAngle = normalRelativeAngleDegrees(errorAngle);
		robot.setTurnGunRight(errorAngle);
		return errorAngle;
	}
	
	public double setFireVector(double x, double y) {
		double bulletSpeed = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		if (setGunVector(x, y) < 5.0) {
			double bulletPower = (20.0 - bulletSpeed) / 3.0;
			if (bulletPower < 0.1 || 3.0 < bulletPower) return Double.NaN;
			try {
				robot.setFireBullet(bulletPower);
			} catch (NullPointerException e){}
			return bulletPower;
		}
		return 0.0;
	}
	
	public double setFixedAim(ScannedRobotEvent e) {
		double absoluteBearing = e.getBearing()+robot.getHeading();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - robot.getGunHeading());		
		robot.setTurnGunRight(bearingFromGun);
		return -(29.0/13000.0)*e.getDistance() + (224.0/65.0);
	}
	
	public double setLinearAim(ScannedRobotEvent e) {
		double s, c, Eb, Vb, r, x, y;
		Eb = -(29.0/13000.0)*e.getDistance() + (224.0/65.0);
		s = getSwingSpeed(e);
		if (3.0 < Eb) Eb = 3.0;
		else if (Eb < 0.1) Eb = 0.1;
		Vb = -3.0*Eb + 20.0;
		c = Math.sqrt((Vb*Vb)-(s*s));
		r = Math.toRadians(-getAbsoluteBearing(e));
		x = s*Math.cos(r) - c*Math.sin(r);
		y = s*Math.sin(r) + c*Math.cos(r);
		setGunVector(x, y);
		return Eb;
	}
	
	public double getSwingSpeed(ScannedRobotEvent e) {
		double bearingFromRival = normalRelativeAngleDegrees(robot.getHeading() + e.getBearing() - e.getHeading() + 180.0);
		double swingSpeed = e.getVelocity() * Math.sin(Math.toRadians(bearingFromRival));
		return swingSpeed;
	}

	public double getAbsoluteBearing(ScannedRobotEvent e) {
		double r;
		r = robot.getHeading() + e.getBearing();
		r %= 360.0;
		if (r < 0.0) r = 360.0 + r;
		return r;
	}

}
