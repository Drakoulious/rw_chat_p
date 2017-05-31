package ru.ilonich.roswarcp;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication(exclude = {
        JacksonAutoConfiguration.class, DataSourceAutoConfiguration.class,
        TransactionAutoConfiguration.class, DispatcherServletAutoConfiguration.class,
        WebMvcAutoConfiguration.class, ErrorMvcAutoConfiguration.class,
        JmxAutoConfiguration.class, PersistenceExceptionTranslationAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class, SecurityAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, JdbcTemplateAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class, HttpEncodingAutoConfiguration.class,
        MultipartAutoConfiguration.class, WebClientAutoConfiguration.class,
        WebSocketAutoConfiguration.class, ConfigurationPropertiesAutoConfiguration.class,
        ProjectInfoAutoConfiguration.class, ServerPropertiesAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class
})
@ImportResource({"classpath:spring/spring.xml", "classpath:spring/spring-sec.xml", "classpath:spring/spring-mvc.xml"})
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(Application.class).bannerMode(Banner.Mode.OFF);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            container.setPort(8080);
        });
    }

    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean()
    {
        ServletRegistrationBean sb = new ServletRegistrationBean(dispatcherServlet(), "/");
        sb.setLoadOnStartup(1);
        sb.setAsyncSupported(true);
        sb.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
        return sb;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(encodingFilter());
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("encodingFilter");
        return filterRegistrationBean;
    }

    @Bean
    public DelegatingFilterProxyRegistrationBean delegatingFilterProxyRegistrationBean(){
        DelegatingFilterProxyRegistrationBean delegatingFilterProxyRegistrationBean = new DelegatingFilterProxyRegistrationBean("springSecurityFilterChain", servletRegistrationBean());
        delegatingFilterProxyRegistrationBean.setOrder(2);
        delegatingFilterProxyRegistrationBean.setAsyncSupported(true);
        delegatingFilterProxyRegistrationBean.addUrlPatterns("/*");
        return delegatingFilterProxyRegistrationBean;
    }

    @Bean
    public CharacterEncodingFilter encodingFilter(){
        return new CharacterEncodingFilter("UTF-8", true);
    }

}
