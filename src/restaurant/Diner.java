package restaurant;

public class Diner implements Runnable, Comparable<Diner> {
	private static final int EATING_DURATION = 30;
	private final int arrivalTime;
	private Order order;
	private Restaurant restaurant;
	
	private Table table;
	private int seatedTime;
	private int servedTime;
	
	public Diner(int arrival, Order order, Restaurant restaurant) {
		arrivalTime = arrival;
		this.order = order;
		this.restaurant = restaurant;
	}
	public int getArrivalTime() {
		return arrivalTime;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
		if (table == null) return;
		int time = this.table.getCurrentTime();
		if (time < arrivalTime) {
			seatedTime = arrivalTime;
			this.table.setCurrentTime(seatedTime);
		} else
			seatedTime = time;
		this.table.setOrder(order);
	}
	public int getFinishedTime() {
		return servedTime + EATING_DURATION;
	}
	public void printInfo() {
		System.out.printf("arrival: %d, seated: %d, table: %d, cook: %d",
				arrivalTime, seatedTime, table.getNumber(), table.getCook().getNumber());
		for (int i = 0; i < Restaurant.NUMBER_OF_FOOD_TYPES; ++i) {
			System.out.printf(", %d", (order.getTimeForDishType(i) == null ? 0 : order.getTimeForDishType(i)));
		}
		System.out.printf(", served: %d\n", servedTime);
	}
	@Override
	public void run() {
		restaurant.enter(this);
		servedTime = table.getServedTime();
		printInfo();
		restaurant.leave(this);
	}
	@Override
	public int compareTo(Diner arg0) {
		if (arrivalTime == arg0.arrivalTime)
			return 0;
		else if (arrivalTime < arg0.arrivalTime)
			return -1;
		else
			return 1;
	}
}
