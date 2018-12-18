package Miyazaki;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *敵機を管理するクラス
 *@author Arsenic
 */
public class Rival {

	/**
	 *自機のインスタンスを格納
	 */
	private AdvancedRobot robot;

	/**
	 *敵機の名前
	 */
//	private String name = null;

	private double fire;
	private double lastEnergy;
	private ScannedRobotEvent rival = null;
	private double bearingFromRival = 0.0;
	private int fireCount = 0;

	/**---RivalTypes---**/
//	private int estimatedType = 0;
	/**
	 *いずれにも該当せず
	 */
//	public static final int TYPE_MISMATCH = 0;
//	private double votesOfMismatch = 0.0;
	/**
	 *敵機は動かない
	 */
//	public static final int TYPE_STILL = 1;
//	private double votesOfStill = 0.0;
	/**
	 *行ったり来たりしている
	 */
//	public static final int TYPE_BACK_AND_FORTH = 2;
//	private double votesOfBackAndForth = 0.0;
	/**
	 *突進して来る
	 */
//	public static final int TYPE_RAMFIRE = 3;
//	private double votesOfRamfire = 0.0;
	/**
	 *壁沿いを這い回っている
	 */
//	public static final int TYPE_WALLS = 4;
//	private double votesOfWalls = 0.0;
	/**
	 *惑星
	 */
//	public static final int TYPE_PLANET = 5;
//	private double votesOfPlanet = 0.0;
	/**---RivalTypes---**/

	/**
	 *敵機を登録　onScannedRobotにてコンストラクタを呼び出してください
	 *@param robot 自機のインスタンス
	 *@param e  onScannedRobotイベントの引数eをそのまま渡してください
	 */
	Rival(AdvancedRobot robot) {
		this.robot = robot;
		fireCount = 0;
	}

	/**
	 *敵機の情報を更新　onScannedRobotにて毎回このメソッドを呼び出してください
	 *このメソッドは、onScannedRobotイベント内で、Rivalクラスの他のメソッドよりも先に呼び出すべきです
	 *@param e onScannedRobotイベントの引数eをそのまま渡してください
	 */
	public void updateRivalOnScannedRobot(ScannedRobotEvent e) {
		rival = e;
		bearingFromRival = normalRelativeAngleDegrees(robot.getHeading() + rival.getBearing() - rival.getHeading() + 180.0);
		fire = lastEnergy - e.getEnergy();
		if (fire < 0.0 || 3.0 < fire) fire = 0.0;
		if (fire != 0.0) fireCount++;
		lastEnergy = e.getEnergy();
		//estimatedType = guessRivalTypeInBackground();
	}

	/**
	 *敵機の情報を更新　onBulletHitにて毎回このメソッドを呼び出してください
	 *@param e onBulletHitイベントの引数eをそのまま渡してください
	 */
	public void updateRivalOnBulletHit(BulletHitEvent e) {
		lastEnergy = e.getEnergy();
	}
	
	/**
	 *敵機の情報を更新　onHitByBulletにて毎回このメソッドを呼び出してください 
	 *@param e onHitByBulletイベントの引数eをそのまま渡してください
	 */
	public void updateRivalOnHitByBullet(HitByBulletEvent e) {
		lastEnergy += 3 * e.getPower();
	}

	/**
	 *自機を原点とした敵機のX座標を取得
	 *@return 敵機のX座標
	 */
	public double getX() {
		double relativeX;
		relativeX = rival.getDistance() * Math.sin(robot.getHeading() + rival.getBearing());
		return relativeX;
	}

	/**
	 *自機を原点とした敵機のY座標を取得
	 *@return 敵機のY座標
	 */
	public double getY() {
		double relativeY;
		relativeY = rival.getDistance() * Math.cos(robot.getHeading() + rival.getEnergy());
		return relativeY;
	}

	/**
	 *敵機のX座標を取得
	 *@return 敵機のX座標
	 */
	public double getAbsoluteX() {
	 	double absoluteX;
	 	absoluteX = robot.getX() + this.getX();
		return absoluteX;
	}

	/**
	 *敵機のY座標を取得
	 *@return 敵機のY座標
	 */
	public double getAbsoluteY() {
		double absoluteY;
		absoluteY = robot.getY() + this.getY();
		return absoluteY;
	}

	/**
	 *自機を原点とした、敵機の絶対角度を取得
	 *@return 敵機の絶対角
	 */
	public double getAbsoluteBearing() {
		double r;
		r = robot.getHeading() + rival.getBearing();
		r %= 360.0;
		if (r < 0.0) r = 360.0 + r;
		return r;
	}
	
	/**
	 *敵機の進行方向から見た自機位置の角度、つまり敵がonScannedRobot内でe.getBearing()をよびだした際の返り値
	 *@return 敵機から見た自機の角度
	 */
	public double getBearingFromRival() {
		return bearingFromRival;
	}

	/**
	 *敵機との距離を取得
	 *@return 敵機との距離
	 */
	public double getL() {
		return rival.getDistance();
	}

	/**
	 *敵の発射した弾丸のエネルギー取得
	 *@return 発射された弾丸のエネルギー、弾丸が発射されてない場合は0.0を返す
	 */
	public double getFire() {
		return this.fire;
	}

	/**
	 *敵機が近づいてくる速さを取得
	 *@return 敵の近づく速さ
	 */
	public double getCloseSpeed() {
		double closeSpeed;
		closeSpeed = rival.getVelocity() * Math.cos(Math.toRadians(bearingFromRival));
		return closeSpeed;
	}
	
	public double getSwingSpeed() {
		double swingSpeed;
		swingSpeed = rival.getVelocity() * Math.sin(Math.toRadians(bearingFromRival));
		return swingSpeed;
	}
	
	public int getFireCount() {
		return fireCount;
	}
	
	public double getFireRate() {
		return (double)(fireCount)/robot.getTime();
	}
	
	/**
	 *角速度を取得
	 *@return 自機を中心とし、時計回りをプラス方向とした敵機の角速度[degree/tick]
	 */
	public double getAngularVelocity() {
		double angularVelocity;
		angularVelocity = getSwingSpeed() / rival.getDistance();
		return Math.toDegrees(angularVelocity);
	}

	/**
	 *敵の移動パターンを解析し行動タイプを推測する
	 *@return 敵の行動タイプ
	 */
/*	public int getType() {
		return this.estimatedType;
	}*/

	/**
	 *敵機の行動を解析、updateRivalOnScannedRobotメソッド内にて呼び出される
	 *@return　推定行動パターン
	 */
/*	private int guessRivalTypeInBackground() {
		if (rival.getVelocity() == 0.0) votesOfStill++;
		if (7.5 < getCloseSpeed()) votesOfRamfire++;

		votesOfStill -= 0.1;
		votesOfBackAndForth -= 0.1;
		votesOfRamfire -= 0.1;
		votesOfWalls -= 0.1;
		votesOfPlanet -= 0.1;
		if (votesOfStill < 0) votesOfStill = 0.0;
		if (votesOfBackAndForth < 0) votesOfBackAndForth = 0.0;
		if (votesOfRamfire < 0) votesOfRamfire = 0.0;
		if (votesOfWalls < 0) votesOfWalls = 0.0;
		if (votesOfPlanet < 0) votesOfPlanet = 0.0;
		return Rival.TYPE_MISMATCH;
	}*/
}