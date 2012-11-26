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
	private int getReadyToServeTime() { //TODO: change after adding machine
		int time = currentTime;
		time += order.getBurgers() * 5;
		time += order.getFries() * 3;
		time += order.getCoke() * 1;
		return time;
	}
	@Override
	public void run() {
		while (true) {
			restaurant.assignCook(this);
			currentTime = getReadyToServeTime();
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
