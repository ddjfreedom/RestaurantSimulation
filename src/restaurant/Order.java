package restaurant;

public final class Order {
	private final int burgers;
	private final int fries;
	private final int coke;
	public Order(int numBurgers, int numFries, int numCoke) {
		burgers = numBurgers;
		fries = numFries;
		coke = numCoke;
	}
	public int getBurgers() {
		return burgers;
	}
	public int getFries() {
		return fries;
	}
	public int getCoke() {
		return coke;
	}
}
