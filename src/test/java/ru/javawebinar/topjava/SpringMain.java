package ru.javawebinar.topjava;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
//        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "postgres,jpa");
//        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "hsqldb,jpa");
//        ApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment()
//        ((ConfigurableEnvironment)context.getEnvironment())
//                .setActiveProfiles(new String[]{"hsqldb", "jpa"});
//        ApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment()..setActiveProfiles("hsqldb", "jpa");
//
        // java 7 Automatic resource management
        try (ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext()) {
//            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            appCtx.getEnvironment().setActiveProfiles("hsqldb", "jpa");
            appCtx.setConfigLocations("spring/spring-app.xml", "spring/spring-db.xml");
            appCtx.refresh();

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            User user = new User(null, "userName", "email", "password", Role.ROLE_ADMIN);
//            adminUserController.create(user);

            adminUserController.getAll().forEach(System.out::println);
            AuthorizedUser.setId(100000);

            System.out.println();

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            List<MealWithExceed> filteredMealsWithExceeded =
                    mealController.getBetween(
                            LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
            filteredMealsWithExceeded.forEach(System.out::println);
        }
    }
}
