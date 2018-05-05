package dwz.framework.junit;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dwz.framework.config.AppConfiguration;

@ContextConfiguration(locations = { "classpath*:application-context.xml", "classpath*:META-INF/dwz-*-components.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseJunitCase {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private static boolean inited = false;

	@BeforeClass
	public static void init() {
		try {
			if (!inited) {
				System.out.println("Initializing running ...");
				System.setProperty("sun.jnu.encoding", "UTF-8");
				System.setProperty("file.encoding", "UTF-8");

				String configFilePath = System.getProperty("user.dir") + "/WebContent/WEB-INF/etc/conf/app-config.xml";
				AppConfiguration.init(configFilePath);

				inited = true;
			} else {
				System.out.println("inited: " + inited);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}