package evm.dmc.config.devel;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@Profile("devH2")
public class DbDataInitializerH2 {
private final String SAMPLE_DATA = "classpath:data-h2-devel.sql";
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	@PostConstruct
	public void loadIfInMemory() throws Exception {
		Resource resource = webApplicationContext.getResource(SAMPLE_DATA);
		ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
	}
}
