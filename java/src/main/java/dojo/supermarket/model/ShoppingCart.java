package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    if (quantityAsInt >= 3) {
                        double total = (offer.numberOfDiscounts(quantityAsInt) * 2 * unitPrice) + quantityAsInt % 3 * unitPrice;
                        double discountAmount = quantity * unitPrice - total;
                        discount = new Discount(p, 3 + " for 2", discountAmount);
                    }
                }
                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    if (quantityAsInt >= 2) {
                        double total = offer.argument * offer.numberOfDiscounts(quantityAsInt) + quantityAsInt % 2 * unitPrice;
                        double discountAmount = unitPrice * quantity - total;
                        discount = new Discount(p, 2 + " for " + offer.argument, discountAmount);
                    }

                } if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    if (quantityAsInt >= 5) {
                        double total = offer.argument * offer.numberOfDiscounts(quantityAsInt) + quantityAsInt % 5 * unitPrice;
                        double discountAmount = unitPrice * quantity - total;
                        discount = new Discount(p, 5 + " for " + offer.argument, discountAmount);
                    }
                }

                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {

                    double discountAmount = unitPrice * quantity * offer.argument / 100.0;
                    discount = new Discount(p, offer.argument + "% off", discountAmount);
                }

                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }
}
