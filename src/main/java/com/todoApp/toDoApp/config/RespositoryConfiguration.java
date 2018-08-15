//package com.todoApp.toDoApp.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
//import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
//
//@Configuration
//public class RespositoryConfiguration extends RepositoryRestMvcConfiguration {
//
//    @Override
//    @Bean
//    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
//        HateoasPageableHandlerMethodArgumentResolver resolver = super.pageableResolver();
//        resolver.setOneIndexedParameters(true);
//        return resolver;
//    }
//}
