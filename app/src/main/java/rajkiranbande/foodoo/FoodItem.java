package rajkiranbande.foodoo;

public class FoodItem {
  String foodName;
  double foodPrice;

  @Override
  public String toString() {
    return "FoodItem{" +
        "foodName='" + foodName + '\'' +
        ", foodPrice=" + foodPrice +
        '}';
  }

  public double getFoodPrice() {
    return foodPrice;
  }

  public void setFoodPrice(double foodPrice) {
    this.foodPrice = foodPrice;
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public FoodItem(String foodName, double foodPrice) {
    this.foodName = foodName;
    this.foodPrice = foodPrice;
  }
}
