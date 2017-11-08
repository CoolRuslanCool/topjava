package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm", Locale.ENGLISH);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("do get getAll delete");

        String param = request.getParameter("action");
        if (param != null) {
            String[] action = param.split("-");
            if (action[0].equals("delete")) {

                log.debug("do delete");
                MealsUtil.deleteMeal(Long.valueOf(action[1]));

                log.debug("do delete redirect meals");
                response.sendRedirect("meals");
                return;
            }
        }
        request.setAttribute("formatter", formatter);
        request.setAttribute("meals", MealsUtil.getFilteredWithExceeded(LocalTime.MIN, LocalTime.MAX, 2000));

        log.debug("forward meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("do post add || edit");

        req.setCharacterEncoding("UTF-8");

        if (req.getParameter("add") != null) {
            log.debug("do post add");

            Meal meal = new Meal(LocalDateTime.from(formatter.parse(req.getParameter("date"))), req.getParameter("description"), Integer.valueOf(req.getParameter("calorie")));
            MealsUtil.addMeal(meal);
        }

        if (req.getParameter("action") != null) {
            String[] actions = req.getParameter("action").split("-");
            if (actions[0].equals("edit")) {
                log.debug("do post edit");

                Meal meal = new Meal(Long.valueOf(actions[1]), LocalDateTime.from(formatter.parse(req.getParameter("date"))), req.getParameter("description"), Integer.valueOf(req.getParameter("calorie")));
                MealsUtil.editMeal(meal);
            }
        }
        req.setAttribute("formatter", formatter);
        req.setAttribute("meals", MealsUtil.getFilteredWithExceeded(LocalTime.MIN, LocalTime.MAX, 2000));

        log.debug("forward meals.jsp");
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}