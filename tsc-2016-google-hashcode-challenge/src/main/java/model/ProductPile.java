package model;

public class ProductPile {

    private final Product product;
    private int count;
    
    public ProductPile(Product product, int count){
        this.product = product;
        this.count = count;
    }
    
    public int getCount(){
        return count;
    }
    
    public void setCount(int count){
        this.count = count;
    }
    
    public Product getProduct(){
        return product;
    }

    public int getWeight() {
        return count * product.getWeight();
    }
    
}
