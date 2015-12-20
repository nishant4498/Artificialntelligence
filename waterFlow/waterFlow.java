import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;



public class waterFlow {

	public waterFlow() {
		
	}

	public static void main(String[] args) throws FileNotFoundException{
		// TODO Auto-generated method stub
		String inputFileName = "F:\\workspace\\java\\Algorithms\\graphs\\graphs\\sampleInput.txt";
		//String inputFileName = args[1];
		
		Scanner sc = new Scanner(new File(inputFileName));
		PrintWriter writer = new PrintWriter("output.txt");		
		
		@SuppressWarnings("unused")
		int numTestCases = Integer.parseInt(sc.nextLine());
		
		while(sc.hasNextLine()){
			TestCase t = new TestCase();
			t.searchType = sc.nextLine();
			t.sourceCity = sc.nextLine();
			t.destinationCities = sc.nextLine().split(" ");
			t.middleCities = sc.nextLine().split(" "); // what if no middle cities? 
			t.numPipes = Integer.parseInt(sc.nextLine());
			Graph g = new Graph();
			
			for (int i =0; i < t.numPipes ; i++){
				String path = sc.nextLine();
				g.insertCities(path);
			}
			
			//g.print();
			t.testGraph = g;
			t.startTime = Integer.parseInt(sc.nextLine());
			boolean result = false;
			if(t.numPipes == 0){
				result = false;
			}
			else
			{
				if(t.searchType.equals("DFS")){
					result = g.DFS(t);
				}
				if(t.searchType.equals("BFS")){
					result = g.BFS(t);
				}
				if(t.searchType.equals("UCS")){
					result = g.UCS(t);
				}
			}
			
			String output = "";
			if(result){
				int reachTime;
				if(t.searchType.equals("UCS")){
					reachTime = t.goalCity.pathCost % 24;
					output = t.goalCity.name + " " + reachTime;
				}
				else{
					reachTime = (t.startTime + t.goalCity.depth) % 24;
					output = t.goalCity.name + " " + reachTime;
				}
				
			}
			else{
				output = "None";
			}
			
			writer.println(output);
			if(sc.hasNextLine()){
				sc.nextLine();
			}			
		}
		
		writer.close();
		sc.close();

	}

}

class TestCase {
	
	public String searchType; // DFS BFS UCS	
	public String sourceCity;
	public String[] destinationCities;
	public String[] middleCities;
	public int numPipes;
	public int startTime;
	public City goalCity;
	public String cost;
	public Graph testGraph;	
}

class City{
	String name;
	City parent;
	int depth;
	int pathCost;
	Neighbor nbrList;	
	City(String name , Neighbor neighbors){
		this.name =  name;
		this.nbrList = neighbors;
	}	
}

class Neighbor {	
	public int vertexNum;
	public Neighbor next;
	public ArrayList<Interval> closeIntervals = new ArrayList<Interval>();
	public int edgeCost;
	public Neighbor(int vnum , Neighbor nbr){
		this.vertexNum = vnum;
		this.next = nbr;
	}
}

class Interval{
	int startTime;
	int endTime;
	
	public Interval(int sTime, int eTime){
		startTime = sTime;
		endTime = eTime;
	}
}

class Graph {
	
	ArrayList<City> cityList = new ArrayList<City>();
	
	public void insertCities(String path){
		
		String[] pathAttributes = path.split(" ");
		String sourceCityName = pathAttributes[0];
		String destCityName = pathAttributes[1];
		
		int edgeCost = Integer.parseInt(pathAttributes[2]);
		int numIntervals =  Integer.parseInt(pathAttributes[3]);
		
		if(cityIndexForName(sourceCityName) == -1){
			City sourceCity = new City( sourceCityName , null);
			cityList.add(sourceCity);
		}
		
		if(cityIndexForName(destCityName) == -1){
			City destCity = new City( destCityName , null);
			cityList.add(destCity);
		}
		
		int v1 = cityIndexForName(sourceCityName);
		int v2 = cityIndexForName(destCityName);
		Neighbor nbr = new Neighbor(v2, cityList.get(v1).nbrList);
		nbr.edgeCost = edgeCost;
 		if(numIntervals > 0){
 			for (int i = 4; i < 4 + numIntervals ; i++){
 				String interval = pathAttributes[i];
 				String[] intervalParts = interval.split("-");
 				int startTime = Integer.parseInt(intervalParts[0]);
 				int endTime = Integer.parseInt(intervalParts[1]);
 				Interval timeInterval = new Interval(startTime , endTime);
 				nbr.closeIntervals.add(timeInterval);
 			}
 		}
		cityList.get(v1).nbrList = nbr;
	}
	
	int cityIndexForName(String cityName){
		for (int i =0; i< cityList.size();i++)
		{
			if(cityList.get(i).name.equals(cityName))
			{
				return i;
			}
		}
		return -1;
	}
	
	public void print(){
		System.out.println("\nPrinting the Graph in the adjacency List Representation");
		for (int i =0; i< cityList.size();i++){
			System.out.println(cityList.get(i).name);
			for (Neighbor nbr = cityList.get(i).nbrList;nbr!= null;nbr = nbr.next){
				System.out.println("---->" + cityList.get(nbr.vertexNum).name);
			}
		}
	}
	
	public boolean DFS(TestCase t){
		ArrayList<City> visitedNodes = new ArrayList<City>();
		Stack<City> frontier = new Stack<City>();		
		int sourceCityIndex = cityIndexForName(t.sourceCity);
		if(sourceCityIndex == -1){
			return false;
		}
				
		City sourceCity = cityList.get(sourceCityIndex);
		
		int depth = 0;
		sourceCity.depth = depth;
		frontier.push(sourceCity);
		while (frontier.size() > 0){
			City currentCity = frontier.pop();
			visitedNodes.add(currentCity);
			if(isGoalNode(currentCity , t))
			{
				t.goalCity = currentCity;
				return true;
			}
			else
			{
				ArrayList<City> list = new ArrayList<City>();	
				for (Neighbor nbr = currentCity.nbrList;nbr!= null;nbr = nbr.next)
				{
					int cityIndex = nbr.vertexNum;  
					City city = cityList.get(cityIndex);
					if( visitedNodes.contains(city) == false){// && frontier.contains(city) == false){
						city.depth = currentCity.depth + 1;
						city.parent = currentCity;
						list.add(city);
					}
				}
				if(list.size()> 0){
					Collections.sort(list,new AlphabeticalOrderComparator());
					Iterator<City> li = list.iterator();
					while(li.hasNext()){
						frontier.push(li.next());
					}
				}
				
			}
		}
		return false;
		
	}
	
	public boolean BFS(TestCase t){
		Comparator<City> orderComparator = new StringOrderComparator();
		ArrayList<City> visitedNodes = new ArrayList<City>();
		Queue<City> frontier = new LinkedList<City>();
		int sourceCityIndex = cityIndexForName(t.sourceCity);
		if(sourceCityIndex == -1){
			return false;
		}
		City sourceCity = cityList.get(cityIndexForName(t.sourceCity));
		int depth = 0;
		sourceCity.depth = depth;
		frontier.offer(sourceCity);
		while (frontier.size() > 0){
			City currentCity = frontier.poll();
			visitedNodes.add(currentCity);
			if(isGoalNode(currentCity , t))
			{
				t.goalCity = currentCity;
				return true;
			}
			else
			{
				ArrayList<City> list = new ArrayList<City>();	
				for (Neighbor nbr = currentCity.nbrList;nbr!= null;nbr = nbr.next)
				{
					int cityIndex = nbr.vertexNum;  
					City city = cityList.get(cityIndex);						
					if( visitedNodes.contains(city) == false && frontier.contains(city) == false){
						city.depth = currentCity.depth + 1;
						city.parent = currentCity;
						list.add(city);
					}						
				}
				if(list.size()> 0){
					Collections.sort(list,orderComparator);
					Iterator<City> li = list.iterator();
					while(li.hasNext()){
						frontier.offer(li.next());
					}
				}
			}
		}
		return false;
		
	}	
	
	public boolean UCS(TestCase t){
		Comparator<City> costComparator = new PathCostComparator();
		ArrayList<City> visitedNodes = new ArrayList<City>();
		PriorityQueue<City> frontier = new PriorityQueue<City>(cityList.size(), costComparator);
		int sourceCityIndex = cityIndexForName(t.sourceCity);
		if(sourceCityIndex == -1){
			return false;
		}
		City sourceCity = cityList.get(cityIndexForName(t.sourceCity));
		sourceCity.pathCost = t.startTime;
		boolean result = false;
		frontier.add(sourceCity);
		while (frontier.size() > 0){
			City currentCity = frontier.remove();
			visitedNodes.add(currentCity);
			if(isGoalNode(currentCity , t))
			{
				t.goalCity = currentCity;
				result = true;
				return result;
			}
			else
			{
					for (Neighbor nbr = currentCity.nbrList;nbr!= null;nbr = nbr.next)
					{
						int cityIndex = nbr.vertexNum;  
						City city = cityList.get(cityIndex);						
						if( visitedNodes.contains(city) == false){
							int currTime = currentCity.pathCost;
							int currPathCost = currentCity.pathCost + nbr.edgeCost;
							if(isNodeReachable(currTime, nbr)){
								if( frontier.contains(city) == true){
									if(currPathCost < city.pathCost){
										city.pathCost = currPathCost;
										city.parent = currentCity;
										frontier.remove(city);
										frontier.add(city);
									}
								}
								else{
									city.pathCost = currPathCost;
									city.parent = currentCity;
									frontier.add( cityList.get(cityIndex));								
								}
						}
							
						}
						
					}
			}
		}
		return false;
	}
	
	// Use destinationCities as a collection.
	public boolean isGoalNode(City city, TestCase t)
	{
		for(int i=0; i < t.destinationCities.length ; i++)
		{
			if(t.destinationCities[i].equals(city.name)){
				return true;
			}
		}		
		return false;
	}
	
	public boolean isNodeReachable(int currTime , Neighbor nbr){
		int currentTime = currTime % 24;
		ArrayList<Interval> closeIntervals = nbr.closeIntervals;
		Iterator<Interval> itr = closeIntervals.iterator();
		while (itr.hasNext()){
			Interval interval = itr.next();
			if(currentTime >= interval.startTime && currentTime <= interval.endTime){
				return false;
			}
		}
		return true;
	}
}


class StringOrderComparator implements Comparator<City> 
{
	
	public int compare(City A , City B)
	{
		String nameA = A.name;
		String nameB = B.name;
		
		//basically when you return -1 is the condition on which you want to sort; so here we want to sort by ascending 
		//alphabetical order the below condition will return -1
		// This means that nameA is alphabetically before nameB
		if(nameA.compareTo(nameB) < 0 ){
			return -1;
		}
		
		if(nameA.compareTo(nameB) > 0 ){
			return 1;
		}		
		return 0;
	}
}

class PathCostComparator implements Comparator<City> 
{
	
	public int compare(City A , City B)
	{
		String nameA = A.name;
		String nameB = B.name;
		
		
		if(A.pathCost < B.pathCost){
			return -1;
		}
		if (A.pathCost == B.pathCost){
			if(nameA.compareTo(nameB) < 0 ){
				return -1;
			}
			
			if(nameA.compareTo(nameB) > 0 ){
				return 1;
			}
		}
		if(A.pathCost > B.pathCost){
			return 1;
		}
		return 0;
	}
}

class AlphabeticalOrderComparator implements Comparator<City> 
{
	
	public int compare(City A , City B)
	{
		String nameA = A.name;
		String nameB = B.name;
		
		if(nameA.compareTo(nameB) < 0 ){
			return 1;
		}
		
		if(nameA.compareTo(nameB) > 0 ){
			return -1;
		}
		
		return 0;
	}
}
