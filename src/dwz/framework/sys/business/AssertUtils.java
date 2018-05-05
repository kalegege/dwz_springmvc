package dwz.framework.sys.business;

public class AssertUtils {
	
	/**
	 * Assert为非新业务对象
	 * @param businessObject
	 */
	public static void notNewBusinessObject(BusinessObject businessObject){
		if (businessObject.isNew()){
			throw new java.lang.IllegalArgumentException();
		}
	}
	
	/**
	 * Assert为新业务对象
	 * @param businessObject
	 */
	public static void newBusinessObject(BusinessObject businessObject){
		if (!businessObject.isNew()){
			throw new java.lang.IllegalArgumentException();
		}
	}
	
}
