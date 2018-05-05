package dwz.dal.common.sequence;

import java.util.UUID;

public class UUIDGenerator {
	
	public static String nextId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
