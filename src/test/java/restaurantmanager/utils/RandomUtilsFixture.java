package restaurantmanager.utils;

import java.util.Random;
import java.util.UUID;

public abstract class RandomUtilsFixture {
	
	public static Long createRandomLong() {
		return new Random().nextLong();
	}
	
	public static String createRandomString() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
