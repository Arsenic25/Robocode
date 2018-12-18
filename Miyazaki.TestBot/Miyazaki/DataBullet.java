package Miyazaki;

public class DataBullet {
	
	public static double injection(double bulletPower, byte injectionData) {
		double injectedBulletPower;
		if (3.0 <= bulletPower) bulletPower = 2.999999999000;
		bulletPower = bulletPower - (bulletPower%0.000000001);
		injectedBulletPower = bulletPower + ((double)injectionData*0.000000000001);
		return injectedBulletPower;
	}
	
	public static byte extract(double bulletPower) {
		double extractedData;
		extractedData = (bulletPower*1000000000000.0)%1000;
		return (byte)Math.round(extractedData);
	}
	
}
