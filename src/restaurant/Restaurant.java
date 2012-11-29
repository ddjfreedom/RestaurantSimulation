package restaurant;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.*;

public class Restaurant {
	private PriorityQueue<Integer> arrivalTimes;
	private SynchronousQueue<Table> emptyTable;
	private PriorityQueue<Table> tables;
	private BlockingQueue<Table> unassignedTables;
	private AtomicInteger numberOfRemainDiners;
	private final int numberOfTables;
	private PriorityQueue<Cook> availableCooks;
	private final int numberOfCooks;
	
	public Restaurant(int numTables, int numCooks, List<Integer> arrival) {
		arrivalTimes = new PriorityQueue<Integer>(arrival);
		emptyTable = new SynchronousQueue<Table>(true);
		tables = new PriorityQueue<Table>(numTables);
		for (int i = 0; i < numTables; ++i)
			tables.add(new Table(i));
		unassignedTables = new ArrayBlockingQueue<Table>(numTables, true);
		numberOfRemainDiners = new AtomicInteger(arrival.size());
		availableCooks = new PriorityQueue<Cook>();
		numberOfCooks = numCooks;
		numberOfTables = numTables;
	}
	public void enter(Diner diner) {
		synchronized (arrivalTimes) {
			try {
				while (arrivalTimes.peek() != diner.getArrivalTime())
					arrivalTimes.wait();
				Table table = emptyTable.take();
				diner.setTable(table);
				unassignedTables.put(table);
			} catch (InterruptedException e) {}
			arrivalTimes.poll();
			arrivalTimes.notifyAll();
		}
	}
	public void leave(Diner diner) {
		Table table = diner.getTable();
		diner.setTable(null);
		table.setCurrentTime(diner.getFinishedTime());
		if (numberOfRemainDiners.decrementAndGet() == 0)
			System.exit(0);
		
		/*
		 * ensure that emptyTables is ordered according to the timestamps
		 * prevents tables with larger timestamps from getting inserted too early
		 */
		synchronized (tables) {
			tables.add(table);
			if (tables.size() == numberOfTables) {
				try {
					emptyTable.put(tables.poll());
				} catch (InterruptedException e) {}
			}
		}
	}
	public void assignCook(Cook cook) {
		try {
			/*
			 * always choose the cook with the smallest timestamp
			 */
			synchronized (availableCooks) {
				availableCooks.add(cook);
				availableCooks.notifyAll();
				while (availableCooks.size() < numberOfCooks || availableCooks.peek() != cook)
					availableCooks.wait();
				availableCooks.poll();
			}
			cook.setTable(unassignedTables.take());
		} catch (InterruptedException e) {}
	}
	public void startSimulation() {
		try {
			emptyTable.put(tables.poll());
		} catch (InterruptedException e) {}
	}
	
	public static int readNonWhitespaceChar(BufferedReader reader) {
		int r = -1;
		try {
			r = reader.read();
			while (Character.isWhitespace(r))
				r = reader.read();
		} catch (IOException e) {
			System.exit(-1);
		}
		return r;
	}
	public static int readInt(BufferedReader reader) {
		char ch = (char) readNonWhitespaceChar(reader);
		int result = 0;
		int r = -1;
		if (ch != '-')
			result = Character.digit(ch, 10);
		try {
			r = reader.read();
			while (Character.isDigit(r)) {
				result = result*10 + Character.digit(r, 10);
				r = reader.read();
			}
			if (ch == '-')
				result *= -1;
		} catch (IOException e) {
			System.exit(-1);
		}
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("input"));
		} catch (FileNotFoundException e) {
			System.exit(-1);
		}
		
		int numDiners = readInt(reader);
		int numTables = readInt(reader);
		int numCooks = readInt(reader);
		List<Integer> arrival = new ArrayList<Integer>();
		List<Integer> burgers = new ArrayList<Integer>();
		List<Integer> fries = new ArrayList<Integer>();
		List<Integer> coke = new ArrayList<Integer>();
		for (int i = 0; i < numDiners; ++i) {
			arrival.add(readInt(reader));
			burgers.add(readInt(reader));
			fries.add(readInt(reader));
			coke.add(readInt(reader));
		}
		Restaurant restaurant = new Restaurant(numTables, numCooks, arrival);
		for (int i = 0; i < numCooks; ++i)
			(new Thread(new Cook(i, restaurant))).start();
		for (int i = 0; i < numDiners; ++i) {
			Order order = new Order(burgers.get(i), fries.get(i), coke.get(i));
			(new Thread(new Diner(arrival.get(i), order, restaurant))).start();
		}
		restaurant.startSimulation();
	}
}
