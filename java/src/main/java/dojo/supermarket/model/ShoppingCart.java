package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> shoppingCart = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return shoppingCart;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (shoppingCart.containsKey(product)) {
            shoppingCart.put(product, shoppingCart.get(product) + quantity);
        } else {
            shoppingCart.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, List<Offer> offers, SupermarketCatalog catalog) {
        for (Offer offer: offers) {
            Discount discount = offer.handle(shoppingCart, catalog);
            if (discount != null)
                receipt.addDiscount(discount);
        }
    }
}
