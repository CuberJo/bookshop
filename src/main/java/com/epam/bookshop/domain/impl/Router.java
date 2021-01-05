package com.epam.bookshop.domain.impl;


// 13 занятие JSP, Expression Language, MVC ext, 1:06 Блинов
public class Router {
    public enum RouteType {
        FORWARD, REDIRECT;
    }

    private String pagePath;
    private RouteType route = RouteType.FORWARD;

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public RouteType getRoute() {
        return route;
    }

    public void setRoute(RouteType route) {
        if (route == null) {
            this.route = RouteType.FORWARD;
        }
        this.route = route;
    }
}
