package dwz.framework.sys.business;

import org.springframework.util.Assert;

import dwz.framework.spring.SpringContextHolder;

public class BusinessFactory {
	
	private static BusinessFactory bf;
	
	private BusinessFactory() {
		 
	}
	
	public static BusinessFactory getInstance() {
		if (bf == null) {
			synchronized (BusinessFactory.class) {
				bf = new BusinessFactory();
			}
		}
		return bf;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BusinessObjectServiceMgr> T getService(String serviceName) {
		Assert.hasText(serviceName);
		return (T)SpringContextHolder.getBean(serviceName);
	}
	
}
