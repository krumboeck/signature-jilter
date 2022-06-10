package at.universalnet.signature.config;

import java.nio.file.Paths;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509ExtendedTrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.SSLFactory.Builder;
import nl.altindag.ssl.util.PemUtils;

@Configuration
public class SSLFactoryConfig {

	@Value("${ssl.ca.file:#{null}}")
	private String sslCAFile;

	@Value("${ssl.ignoreHostname:false}")
	private boolean ignoreHostname;

	@Bean
	public SSLSocketFactory getSSLSocketFactory() {
		Builder sslFactoryBuilder = SSLFactory.builder();
		
		if (sslCAFile != null && !sslCAFile.isEmpty()) {
			X509ExtendedTrustManager trustManager = PemUtils.loadTrustMaterial(Paths.get(sslCAFile));
			sslFactoryBuilder
					.withTrustMaterial(trustManager);
		} else {
			sslFactoryBuilder
					.withSystemTrustMaterial()
					.withDefaultTrustMaterial();
		}

		if (ignoreHostname) {
			sslFactoryBuilder.withUnsafeHostnameVerifier();
		}

		return sslFactoryBuilder.build().getSslSocketFactory(); 
	}

}
