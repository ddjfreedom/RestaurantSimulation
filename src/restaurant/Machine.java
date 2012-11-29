package restaurant;

public class Machine implements Comparable<Machine>{
	private final int type;
	private int currentTime;
	private final int unitDuration;
	public Machine(int type, int length) {
		this.type = type;
		currentTime = 0;
		unitDuration = length;
	}
	public int getType() {
		return type;
	}
	public synchronized int getCurrentTime() {
		return currentTime;
	}
	public synchronized void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}
	public int getUnitDuration() {
		return unitDuration;
	}
	@Override
	public int compareTo(Machine o) {
		if (currentTime == o.currentTime)
			return 0;
		else if (currentTime < o.currentTime)
			return -1;
		else
			return 1;
	}
}
