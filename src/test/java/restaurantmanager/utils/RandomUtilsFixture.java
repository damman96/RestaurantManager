package restaurantmanager.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class RandomUtilsFixture {
	
	private static final int BOUND = 999;
	
	public static Long createRandomLong() {
		return ThreadLocalRandom.current().nextLong(BOUND);
	}
	
	public static String createRandomString() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
