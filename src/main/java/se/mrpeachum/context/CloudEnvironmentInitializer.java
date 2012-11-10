package se.mrpeachum.context;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class CloudEnvironmentInitializer implements	ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		CloudEnvironment env = new CloudEnvironment();
		if (env.getInstanceInfo() != null) {
			System.out.println("cloud API detected: " + env.getCloudApiUri());
			applicationContext.getEnvironment().setActiveProfiles("cloud");
		} 
	}

}
