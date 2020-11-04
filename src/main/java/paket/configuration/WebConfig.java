package paket.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class WebConfig implements WebMvcConfigurer {   
	
	@Autowired
    private MessageSource messageSource;
	
	 @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/ulogovan").setViewName("index");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/proizvodi").setViewName("proizvod");
        registry.addViewController("/user/napravikupovinu").setViewName("user/kreirajkupovinu");
        registry.addViewController("/user/kupovinastavki").setViewName("user/kupistavku");
        registry.addViewController("/user/kupitistavku/{id}/{kolicinastavke}").setViewName("user/kupistavku");
        registry.addViewController("/user/resetujStavku/{id}").setViewName("user/kupistavku");
        registry.addViewController("/admin/addproizvod").setViewName("/admin/proizvod-add");
        registry.addViewController("/admin/editproizvod/{id}").setViewName("/admin/proizvod-edit");
        registry.addViewController("/admin/users").setViewName("/admin/user");
        registry.addViewController("/admin/kupovine").setViewName("/admin/kupovine-sve");
        
	}
	
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }

    @Bean
	public SpringSecurityDialect securityDialect() {
	    return new SpringSecurityDialect();
	}

}
