package dojo.supermarket.model;

import com.sun.org.apache.xml.internal.resolver.Catalog;

import java.util.List;
import java.util.Map;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    private final List<Product> productList;
    private double percentDiscount;
    private double amountDiscount;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        if (offerType == SpecialOfferType.TenPercentDiscount || offerType == SpecialOfferType.BundleDiscount) {
            this.percentDiscount = argument;
        } else {
            this.amountDiscount = argument;
        }
        this.product = product;
        this.productList = null;
    }

    public Offer(List<Product> productList, double argument) {
        this.offerType = SpecialOfferType.BundleDiscount;
        this.percentDiscount = argument;
        this.productList = productList;
        this.product = null;
    }


    Product getProduct() {
        return this.product;
    }

    public int numberOfDiscounts(int quantityAsInt) {
        switch (offerType) {
            case ThreeForTwo:
                return quantityAsInt / 3;
            case TwoForAmount:
                return quantityAsInt / 2;
            case FiveForAmount:
                return quantityAsInt / 5;
            case TenPercentDiscount:
                return 1;
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean isApplicableTo(Map<Product, Double> shoppingCart) {
        if (this.offerType == SpecialOfferType.BundleDiscount) {
            return false;
        } else {
            return shoppingCart.containsKey(this.product);
        }
    }

    public Discount handle(Map<Product, Double> shoppingCart, SupermarketCatalog catalog) {
        Discount discount = null;
        if (this.isApplicableTo(shoppingCart)) {
            double quantity = shoppingCart.get(this.product);
            double unitPrice = catalog.getUnitPrice(this.product);
            int quantityAsInt = (int) quantity;
            if (this.offerType == SpecialOfferType.ThreeForTwo) {
                if (quantityAsInt >= 3) {
                    double total = (this.numberOfDiscounts(quantityAsInt) * 2 * unitPrice) + quantityAsInt % 3 * unitPrice;
                    double discountAmount = quantity * unitPrice - total;
                    discount = new Discount(this.product, 3 + " for 2", discountAmount);
                }
            }
            if (this.offerType == SpecialOfferType.TwoForAmount) {
                if (quantityAsInt >= 2) {
                    double total = this.amountDiscount * this.numberOfDiscounts(quantityAsInt) + quantityAsInt % 2 * unitPrice;
                    double discountAmount = unitPrice * quantity - total;
                    discount = new Discount(this.product, 2 + " for " + this.amountDiscount, discountAmount);
                }

            }
            if (this.offerType == SpecialOfferType.FiveForAmount) {
                if (quantityAsInt >= 5) {
                    double total = this.amountDiscount * this.numberOfDiscounts(quantityAsInt) + quantityAsInt % 5 * unitPrice;
                    double discountAmount = unitPrice * quantity - total;
                    discount = new Discount(this.product, 5 + " for " + this.amountDiscount, discountAmount);
                }
            }

            if (this.offerType == SpecialOfferType.TenPercentDiscount) {

                double discountAmount = unitPrice * quantity * this.percentDiscount / 100.0;
                discount = new Discount(this.product, this.percentDiscount + "% off", discountAmount);
            }

            if (this.offerType == SpecialOfferType.BundleDiscount) {

                double discountAmount = unitPrice * quantity * this.percentDiscount / 100.0;
                discount = new Discount(this.product, this.percentDiscount + "% off", discountAmount);
            }

        }
        return discount;
    }
}
