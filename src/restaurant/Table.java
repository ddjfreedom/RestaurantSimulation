package restaurant;

public class Table {
	private int currentTime;
	private final int number;
	private Order order;
	
	public Table(int number) {
		this.number = number;
		currentTime = 0;
	}
	public int getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public int getNumber() {
		return number;
	}
	public int getServedTime() { //TODO: change after adding Cook
		int time = currentTime;
		time += order.getBurgers() * 5;
		time += order.getFries() * 3;
		time += order.getCoke() * 1;
		return time;
	}
}
