package restaurant;

public class Cook implements Runnable, Comparable<Cook> {
	private final int number;
	private final Restaurant restaurant;

	private int currentTime;
	private Table table;
	private Order order;
	public Cook(int number, Restaurant restaurant) {
		this.number = number;
		this.restaurant = restaurant;
		currentTime = 0;
		table = null;
		order = null;
	}
	public int getNumber() {
		return number;
	}
	public Order getOrder() {
		return order;
	}
	public int getCurrentTime() {
		return currentTime;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
		if (table == null) return;
		this.table.setCook(this);
		int time = this.table.getCurrentTime();
		if (time < currentTime) {
			this.table.setCurrentTime(currentTime);
		} else
			currentTime = time;
		order = table.getOrder();
	}
	public void prepare(Machine machine) {
		int time = (currentTime < machine.getCurrentTime() ? machine.getCurrentTime() : currentTime);
		order.setTimeForDishType(time, machine.getType());
		time += machine.getUnitDuration() * order.getOrderAmount(machine.getType());
		currentTime = time;
		machine.setCurrentTime(time);
		order.setOrderAmount(machine.getType(), 0);
	}
	@Override
	public void run() {
		while (true) {
			restaurant.assignCook(this);
			restaurant.preparingDish(this);
			table.setServedTime(currentTime);
		}
	}
	@Override
	public String toString() {
		return "(" + String.valueOf(number) + ": " + String.valueOf(currentTime) + ")";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Cook) {
			if (currentTime == ((Cook) obj).currentTime)
				return true;
		}
		return false;
	}
	@Override
	public int compareTo(Cook o) {
		if (currentTime < o.currentTime)
			return -1;
		else if (currentTime > o.currentTime)
			return 1;
		return 0;
	}
}
