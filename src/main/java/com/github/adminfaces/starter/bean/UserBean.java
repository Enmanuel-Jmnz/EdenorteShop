/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.UserFacade;
import com.github.adminfaces.starter.entities.User;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;

import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Named(value = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    @Getter
    private User user = new User();
    @Getter
    private User selectedUser;
    @Getter
    private User selectedUsers;

    @EJB
    private UserFacade userFacade;
    private List<User> users;
   
    private List<String> fieldNames;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    private boolean closed;
    
    @PostConstruct
    public void init() {
        users = userFacade.findAll();
        fieldNames = getFieldNames(User.class);
    }
      public List<String> getFieldNames(Class<?> clazz) {
    List<String> names = new ArrayList<>();
    for (Field field : clazz.getDeclaredFields()) {
        names.add(field.getName());
    }
    return names;
}
    public void register() {
        FacesContext context = FacesContext.getCurrentInstance();

        userFacade.create(user);

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Success", "Data saved"));

        try {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/pages/user-list.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser() {
        if (selectedUser != null) {
            userFacade.edit(selectedUser);
            users = userFacade.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated"));
        }
    }

    public void deleteUser() {
        if (selectedUser != null) {
            userFacade.remove(selectedUser);
            users = userFacade.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Deleted", "User successfully deleted"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No user selected"));
        }

    }

    public void error() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El email o la contrasena es incorrecto."));
    }

    public void onClose() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message is closed", null));
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
