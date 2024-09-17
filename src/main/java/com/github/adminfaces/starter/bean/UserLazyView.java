package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.UserFacade;
import com.github.adminfaces.starter.entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class UserLazyView implements Serializable {

    private LazyDataModel<User> lazyModel;
    private List<Header> headers;

    @EJB
    private UserFacade userFacade;

    @PostConstruct
    public void init() {
        lazyModel = new UserLazyDataModel(userFacade);

        headers = new ArrayList<>();
        headers.add(new Header("ID", "userID", "userID", "userID"));
        headers.add(new Header("DNI", "dni", "dni", "dni"));
        headers.add(new Header("Name", "name", "name", "name"));
        headers.add(new Header("Email", "email", "email", "email"));
        headers.add(new Header("Category", "category", "category", "category"));
        headers.add(new Header("Date", "date", "date", "date"));
    }

    public LazyDataModel<User> getLazyModel() {
        return lazyModel;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    //class for headers
    public static class Header {
        @Getter @Setter
        private String label;
        @Getter @Setter
        private String sortBy;
        @Getter @Setter
        private String filterBy;
        @Getter @Setter
        private String property;

        public Header(String label, String sortBy, String filterBy, String property) {
            this.label = label;
            this.sortBy = sortBy;
            this.filterBy = filterBy;
            this.property = property;
        }
    }
}
