package restaurant;

import java.util.*;

public final class Order {
	private int burgers;
	private int fries;
	private int coke;
	private Map<Integer, Integer> times; // record the time each machine is used
	public Order(int numBurgers, int numFries, int numCoke) {
		burgers = numBurgers;
		fries = numFries;
		coke = numCoke;
		times = new HashMap<Integer, Integer>();
	}
	public int getOrderAmount(int i) {
		if (i == 0)
			return burgers;
		else if (i == 1)
			return fries;
		else if (i == 2)
			return coke;
		throw new IllegalArgumentException();
	}
	public void setOrderAmount(int i, int amount) {
		if (i == 0)
			burgers = amount;
		else if (i == 1)
			fries = amount;
		else if (i == 2)
			coke = amount;
		else
			throw new IllegalArgumentException();
	}
	public void setTimeForDishType(int time, int type) {
		if (type < 0 || type > 2)
			throw new IllegalArgumentException();
		times.put(type, time);
	}
	public Integer getTimeForDishType(int type) {
		return times.get(type);
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
