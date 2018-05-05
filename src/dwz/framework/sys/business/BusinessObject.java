package dwz.framework.sys.business;

import java.io.Serializable;

public interface BusinessObject extends Serializable {

	Serializable getId();
	
	boolean isNew();
	
	String getCacheKey();
	
}
