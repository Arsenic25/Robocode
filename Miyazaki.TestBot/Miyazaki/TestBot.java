package Miyazaki;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class TestBot extends AdvancedRobot {
	private Attack attack;
	private Drive drive;
	private Scan scan;
	private Rival rival;
	private byte aimType = Attack.TYPE_FIXED;
	private double hitCount[] = {1, 1}, missCount[] = {1, 1};
	private double hitRate[] = {0.0, 0.0};
	private boolean dir = true;
	boolean lastIsSide = false;
	public void run() {
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setColors(Color.black,Color.black,Color.black); 
		setBulletColor(Color.gray);
		setScanColor(Color.gray);
		attack = new Attack(this);
		drive = new Drive(this);
		scan = new Scan(this);
		rival = new Rival(this);
		while(true) {
			double randomX = Math.random(), randomY = Math.random();
			while(16 < scan.search()) {
				if (drive.relative(randomX, randomY) < 80.0) {
					randomX = Math.random();
					randomY = Math.random();
					execute();
				}
				execute();
			}
			execute();
		}
	}


	public void onScannedRobot(ScannedRobotEvent e) {
		double Eb = 0.0;
		rival.updateRivalOnScannedRobot(e);
		switch (aimType) {
			case Attack.TYPE_FIXED:
				setBulletColor(Color.white);
				Eb = DataBullet.injection(attack.setFixedAim(e), Attack.TYPE_FIXED);
				break;
			case Attack.TYPE_LINEAR:
				setBulletColor(Color.red);
				Eb = DataBullet.injection(attack.setLinearAim(e), Attack.TYPE_LINEAR);
				break;
		}
		
		if (getGunHeat() == 0.0) {
			setFire(Eb);
			for (int i = 0; i < 2; i++) hitRate[i] = hitCount[i] / (hitCount[i]+missCount[i]);
			if (Math.random() < 0.5) {
				if (hitRate[0] < hitRate[1]) aimType = Attack.TYPE_LINEAR;
				else aimType = Attack.TYPE_FIXED;
			} else {
				if (Math.random() < 0.5) aimType = Attack.TYPE_LINEAR;
				else aimType = Attack.TYPE_FIXED;
			}
		}
		
		if (!lastIsSide && isSide()) dir = !dir;
		lastIsSide = isSide();

		if ((e.getDistance() < 250 || (rival.getFireRate() < 0.025 && 100 < getTime())) || getOthers() != 1) {
			if (dir) {
				if (120 < e.getDistance()) setTurnRight(e.getBearing()+45);
				else setTurnRight(e.getBearing()+135);
				setAhead(100);
			} else {
				if (120 < e.getDistance()) setTurnRight(e.getBearing()+135);
				else setTurnRight(e.getBearing()+45);
				setBack(100);
			}
		} else if (rival.getFire() != 0.0) {
			if (Math.random() < 0.5) {
				setTurnRight(e.getBearing()+60-(Math.random()*40));
				setAhead(60+Math.random()*70);
			} else {
				setTurnRight(e.getBearing()+120+(Math.random()*40));
				setBack(60+Math.random()*70);		
			}
		}
		scan.bind(e);
		execute();
	}

	public void onBulletHit(BulletHitEvent e) {
		rival.updateRivalOnBulletHit(e);
		hitCount[DataBullet.extract(e.getBullet().getPower())]++;
	}
	
	public void onBulletMissed(BulletMissedEvent e) {
		missCount[DataBullet.extract(e.getBullet().getPower())]++;
	}

	public void onHitByBullet(HitByBulletEvent e) {
		rival.updateRivalOnHitByBullet(e);
	}
	
	public void onHitWall(HitWallEvent e) {
		dir = !dir;
		clearAllEvents();
	}
	
	public void onHitRobot(HitRobotEvent e) {
		double absoluteBearing = e.getBearing()+getHeading();
		double bearingFromRadar = normalRelativeAngleDegrees(absoluteBearing - getRadarHeading());
		setTurnRadarRight(bearingFromRadar);
		execute();
	}
	
	double absoluteAngle(double r) {
		r = r % 360.0;
		if (r < 0.0) r = 360.0 + r;
		return r;
	}
	
	boolean isSide() {
		if ((getBattleFieldWidth()-80) < getX() || getX() < 80 || (getBattleFieldHeight()-80) < getY() || getY() < 80) return true;
		else return false;
	}
}
