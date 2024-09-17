package com.github.adminfaces.starter.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class CartBean implements Serializable {

    private List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(CartItem cartItem) {
        boolean productExists = false;

    // Recorre los items del carrito para verificar si el producto ya existe
    for (CartItem item : cartItems) {
        if (item.getProduct().getProductID().equals(cartItem.getProduct().getProductID())) {
            // Si el producto ya existe, suma la cantidad
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            productExists = true;
            break;
        }
    }

    // Si el producto no existe en el carrito, lo añade como un nuevo item
    if (!productExists) {
        cartItems.add(cartItem);
    }
    }

    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public double getTotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
    
    public void clearCart() {
    if (cartItems != null) {
        cartItems.clear(); // Elimina todos los elementos de la lista
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Cart Cleared", "All items have been removed from the cart."));
    }
}


    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
