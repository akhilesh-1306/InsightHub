// This solution didnt work -------


// package com.project.InsightHub.config;

// import java.util.HashMap;
// import java.util.Map;

// import javax.sql.DataSource;

// import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

// @Configuration
// public class HibernateConfig {
//     @Bean
//     public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//             EntityManagerFactoryBuilder builder,
//             DataSource dataSource) {

//         Map<String, Object> properties = new HashMap<>();
//         properties.put("hibernate.jdbc.lob.non_contextual_creation", true); 

//         return builder
//                 .dataSource(dataSource)
//                 .packages("com.project.InsightHub")
//                 .properties(properties)
//                 .build();
//     }
// }
