package dwz.framework.sys.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import dwz.cache.CacheManager;
import dwz.cache.CacheManagerFactory;
import dwz.cache.CacheUtils;
import dwz.framework.config.AppConfiguration;
import dwz.framework.spring.SpringContextHolder;

public abstract class AbstractBusinessObjectServiceMgr implements BusinessObjectServiceMgr {

	protected Log log = null;
	
	@Autowired(required=false)
	protected HttpServletRequest request;

	public AbstractBusinessObjectServiceMgr() {
		log = LogFactory.getLog(this.getClass());
	}
	
	public CacheManager getMemCacheManager()
	{
		CacheManager cm = CacheManagerFactory.getInstance().getMemCacheManager();
		if (cm == null) {
			throw new ExceptionInInitializerError("Cache Manager Initial Error,Please Check cache config.");
		}
		return cm;
	}

	@Override
	public <T extends BusinessObject> void setInCache(T t)
	{
		Assert.notNull(t);
		this.getMemCacheManager().set(CacheUtils.keyOfClass(t.getClass(), t.getId()),t);
	}

	@Override
	public <T extends BusinessObject> void deleteFromCache(T t)
	{
		Assert.notNull(t);
		AssertUtils.notNewBusinessObject(t);
		this.getMemCacheManager().delete(CacheUtils.keyOfObject(t, t.getId()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BusinessObject> T getFromCache(Class<T> clazz, Serializable id)
	{
		Assert.notNull(clazz);
		Assert.notNull(id);
		BusinessObject obj = (BusinessObject)this.getMemCacheManager().get(CacheUtils.keyOfClass(clazz, id));
		return (T)obj;
	}

	@Override
	public <T extends BusinessObject> Set<T> filterNewBusinessObject(List<T> ts)
	{
		if (CollectionUtils.isEmpty(ts))
		{
			return null;
		}
		Set<T> set = new HashSet<T>();
		for (T t : ts) 
		{
			if (!t.isNew())
			{
				set.add(t);
			}
		}
		return set;
	}

	@Override
	public <T extends BusinessObject> Set<T> filterNotNewBusinessObject(List<T> ts)
	{
		if (CollectionUtils.isEmpty(ts))
		{
			return null;
		}
		Set<T> set = new HashSet<T>();
		for (T t : ts) 
		{
			if (t.isNew())
			{
				set.add(t);
			}
		}
		return set;
	}

	@Override
	public <T extends BusinessObject> List<java.io.Serializable> businessObject2Ids(List<T> bos)
	{
		if (!CollectionUtils.isEmpty(bos))
		{
			List<java.io.Serializable> ids = new ArrayList<java.io.Serializable>();
			for (T bo :bos)
			{
				if (!bo.isNew())
				{
					ids.add(bo.getId());
				}
			}
			return ids;
		}
		return null;
	}
	
	protected AppConfiguration getAppConfig() {
		return AppConfiguration.getInstance();
	}
	
	protected String getMessage(String code) {
		return this.getMessage(code, new Object[] {});
	}

	protected String getMessage(String code, Object arg0) {
		return getMessage(code, new Object[] { arg0 });
	}

	protected String getMessage(String code, Object arg0, Object arg1) {
		return getMessage(code, new Object[] { arg0, arg1 });
	}

	protected String getMessage(String code, Object arg0, Object arg1,
			Object arg2) {
		return getMessage(code, new Object[] { arg0, arg1, arg2 });
	}

	protected String getMessage(String code, Object arg0, Object arg1,
			Object arg2, Object arg3) {
		return getMessage(code, new Object[] { arg0, arg1, arg2, arg3 });
	}

	protected String getMessage(String code, Object[] args) {
		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			Locale locale = localeResolver.resolveLocale(request);
	
			return SpringContextHolder.getApplicationContext().getMessage(code, args, locale);
		} catch (Exception e) {
			log.error(e);
			return code;
		}
	}
}
