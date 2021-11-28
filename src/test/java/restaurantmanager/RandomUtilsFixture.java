package restaurantmanager;

import java.util.Random;
import java.util.UUID;

public class RandomUtilsFixture {
	
	public static String createRandomString() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static Long createRandomLong() {
		return new Random().nextLong();
	}
}
