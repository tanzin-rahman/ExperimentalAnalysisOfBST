package Utils;

public class Cost {
    public int accessCost;
    public int updateCost;

    public Cost(int accessCost, int updateCost) {
        this.accessCost = accessCost;
        this.updateCost = updateCost;
    }

    public int totalCost() {
        return accessCost + updateCost;
    }
}
