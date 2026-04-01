package at.universalnet.ldap.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultTlsDirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool2.factory.PooledContextSource;
import org.springframework.ldap.pool2.validation.DefaultDirContextValidator;
import org.springframework.ldap.pool2.validation.DirContextValidator;

@Configuration
@ComponentScan({
	"at.universalnet.ldap.api.main",
	"at.universalnet.ldap.impl.main",
	"at.universalnet.ldap.service"
	})
public class LdapConfig {

	@Value("${ldap.starttls:true}")
	private boolean startTlsEnabled;

	@Inject
	private SSLSocketFactory sslSocketFactory;
	
	public LdapConfig() {
		super();
	}

	@Bean
	@ConfigurationProperties(prefix = "ldap")
	public LdapContextSourceProperties ldapContextSourceProperties() {
		return new LdapContextSourceProperties();
	}

	@Bean
	public DirContextAuthenticationStrategy ldapAuthTLSStrategy() {
		DefaultTlsDirContextAuthenticationStrategy strategy = new DefaultTlsDirContextAuthenticationStrategy();
		strategy.setSslSocketFactory(sslSocketFactory);
		return strategy;
	}

	@Bean
	public PooledContextSource contextSource() {
		PooledContextSource contextSource = new PooledContextSource(ldapContextSourceProperties().getPool());
		contextSource.setContextSource(contextSourceTarget());
		contextSource.setDirContextValidator(dirContextValidator());
		return contextSource;
	}

	@Bean
	public DirContextValidator dirContextValidator() {
		return new DefaultDirContextValidator();
	}

	@Bean
	public LdapContextSource contextSourceTarget() {
	    LdapContextSource contextSource = new LdapContextSource();
	    LdapContextSourceProperties prop = ldapContextSourceProperties();
	    contextSource.setUrl(prop.getUrl());
	    contextSource.setBase(prop.getBase());
	    contextSource.setUserDn(prop.getUserDN());
	    contextSource.setPassword(prop.getPassword());

	    // Binäre Attribute explizit deklarieren
	    Map<String, Object> env = new HashMap<>();
	    env.put("java.naming.ldap.attributes.binary", "jpegPhoto photo userCertificate thumbnailPhoto");
	    contextSource.setBaseEnvironmentProperties(env);

	    if (!prop.getUrl().startsWith("ldaps") && startTlsEnabled) {
	    	contextSource.setAuthenticationStrategy(ldapAuthTLSStrategy());
	    }
	    contextSource.setReferral("follow");
	    contextSource.setPooled(false);
	    
	    return contextSource;
	}

	@Bean
	@Qualifier("mainLdapTemplate")
	public LdapTemplate ldapTemplate() {
	    return new LdapTemplate(contextSource());
	}

}
