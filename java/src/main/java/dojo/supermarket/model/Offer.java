package dojo.supermarket.model;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
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
}
