package org.fartpig.ddssrc2db;

import org.fartpig.ddssrc2db.service.ResolverService;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.Sm2TagRuleBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "org.fartpig.ddssrc2db.repository", "org.fartpig.ddssrc2db.service",
		"org.fartpig.ddssrc2db.web", "org.fartpig.ddssrc2db.resolver" })
@EnableTransactionManagement
@EnableAsync
@EnableCaching
@EntityScan(basePackages = "org.fartpig.ddssrc2db.model")
public class App extends SpringBootServletInitializer {

	private static Logger log = LoggerFactory.getLogger(App.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public FilterRegistrationBean siteMeshFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new MySiteMeshFilter());
		return filterRegistrationBean;
	}

	public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

		@Override
		protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
			builder.addDecoratorPath("/*", "/index").addExcludedPath("/index").addExcludedPath("/static/**")
					.addTagRuleBundle(new Sm2TagRuleBundle());
		}

	}

	@Bean
	public CommandLineRunner commandLineRunner(org.springframework.context.ApplicationContext ctx) {

		return args -> {
			if (args.length > 1) {
				if (args[0].equals("resolve")) {
					ResolverService resolver = ctx.getBean(ResolverService.class);
					log.info("begin resolve file from dir:" + args[1]);
					resolver.resolve(args[1]);
				}
			}
		};

	}

}
