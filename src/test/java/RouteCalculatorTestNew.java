import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class RouteCalculatorTestNew extends TestCase
{


    private static String dataFile = "src/main/resources/my_metro.json";
    private static Scanner scanner;

    private static StationIndex stationIndex;

    List<Station> route;


    @Override
    protected void setUp() throws Exception
    {
        route = new ArrayList<>();

        Line line1 = new Line(1, "ONE");
        Line line2 = new Line(2, "TWO");
        Line line3 = new Line(3, "THREE");

        Station stationCCCC = new Station("CCCC", line1);
        Station stationDDDD = new Station("DDDD", line1);

        Station stationJJJJ = new Station("JJJJ", line2);
        Station stationOOOO = new Station("OOOO", line2);

        Station stationPPPP = new Station("PPPP", line3);
        Station stationRRRR = new Station("RRRR", line3);

        route.add(stationCCCC);
        route.add(stationDDDD);

        route.add(stationJJJJ);
        route.add(stationOOOO);

        route.add(stationPPPP);
        route.add(stationRRRR);
    }

    public void testCalculateDuration()
    {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 14.5;
        assertEquals(expected, actual);
    }


    public void testGetShortestRouteOneLine()
    {
        RouteCalculator calculator = getRouteCalculator();

        Station CCCC = stationIndex.getStation("CCCC");
        Station DDDD = stationIndex.getStation("DDDD");

        List<Station> expected = new ArrayList<>();
        expected.add(CCCC);
        expected.add(DDDD);

        Station from = stationIndex.getStation("CCCC");
        Station to = stationIndex.getStation("DDDD");

        List<Station> actual = calculator.getShortestRoute(from, to);
        assertEquals(expected, actual);

        System.out.println("Expected route:");
        printRoute(expected);
        System.out.println("Actual route:");
        printRoute(actual);

    }

    public void testGetShortestRouteTwoLines()
    {
        RouteCalculator calculator = getRouteCalculator();

        Station CCCC = stationIndex.getStation("CCCC");
        Station DDDD = stationIndex.getStation("DDDD");
        Station JJJJ = stationIndex.getStation("JJJJ");
        Station KKKK = stationIndex.getStation("KKKK");
        Station LLLL = stationIndex.getStation("LLLL");
        Station MMMM = stationIndex.getStation("MMMM");

        List<Station> expected = new ArrayList<>();
        expected.add(CCCC);
        expected.add(DDDD);
        expected.add(JJJJ);
        expected.add(KKKK);
        expected.add(LLLL);
        expected.add(MMMM);

        Station from = stationIndex.getStation("CCCC");
        Station to = stationIndex.getStation("MMMM");

        List<Station> actual = calculator.getShortestRoute(from, to);
        assertEquals(expected, actual);

        System.out.println("Expected route:");
        printRoute(expected);
        System.out.println("Actual route:");
        printRoute(actual);

    }

    public void testGetShortestRouteThreeLines()
    {
        RouteCalculator calculator = getRouteCalculator();

        Station CCCC = stationIndex.getStation("CCCC");
        Station DDDD = stationIndex.getStation("DDDD");
        Station JJJJ = stationIndex.getStation("JJJJ");
        Station KKKK = stationIndex.getStation("KKKK");
        Station LLLL = stationIndex.getStation("LLLL");
        Station MMMM = stationIndex.getStation("MMMM");
        Station NNNN = stationIndex.getStation("NNNN");
        Station OOOO = stationIndex.getStation("OOOO");
        Station TTTT = stationIndex.getStation("TTTT");
        Station UUUU = stationIndex.getStation("UUUU");

        List<Station> expected = new ArrayList<>();
        expected.add(CCCC);
        expected.add(DDDD);
        expected.add(JJJJ);
        expected.add(KKKK);
        expected.add(LLLL);
        expected.add(MMMM);
        expected.add(NNNN);
        expected.add(OOOO);
        expected.add(TTTT);
        expected.add(UUUU);


        Station from = stationIndex.getStation("CCCC");
        Station to = stationIndex.getStation("UUUU");

        List<Station> actual = calculator.getShortestRoute(from, to);
        assertEquals(expected, actual);

        System.out.println("Expected route:");
        printRoute(expected);
        System.out.println("Actual route:");
        printRoute(actual);
    }


    @Override
    protected void tearDown() throws Exception
    {

    }



    private static RouteCalculator getRouteCalculator()
    {
        createStationIndex();
        return new RouteCalculator(stationIndex);
    }

    private static void printRoute(List<Station> route)
    {
        Station previousStation = null;
        for(Station station : route)
        {
            if(previousStation != null)
            {
                Line prevLine = previousStation.getLine();
                Line nextLine = station.getLine();
                if(!prevLine.equals(nextLine))
                {
                    System.out.println("\tПереход на станцию " +
                            station.getName() + " (" + nextLine.getName() + " линия)");
                }
            }
            System.out.println("\t" + station.getName());
            previousStation = station;
        }
    }

    private static Station takeStation(String message)
    {
        for(;;)
        {
            System.out.println(message);
            String line = scanner.nextLine().trim();
            Station station = stationIndex.getStation(line);
            if(station != null) {
                return station;
            }
            System.out.println("Станция не найдена :(");
        }
    }

    private static void createStationIndex()
    {
        stationIndex = new StationIndex();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

            JSONArray linesArray = (JSONArray) jsonData.get("lines");
            parseLines(linesArray);

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            parseStations(stationsObject);

            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            parseConnections(connectionsArray);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void parseConnections(JSONArray connectionsArray)
    {
        connectionsArray.forEach(connectionObject ->
        {
            JSONArray connection = (JSONArray) connectionObject;
            List<Station> connectionStations = new ArrayList<>();
            connection.forEach(item ->
            {
                JSONObject itemObject = (JSONObject) item;
                int lineNumber = ((Long) itemObject.get("line")).intValue();
                String stationName = (String) itemObject.get("station");

                Station station = stationIndex.getStation(stationName, lineNumber);
                if(station == null)
                {
                    throw new IllegalArgumentException("core.Station " +
                            stationName + " on line " + lineNumber + " not found");
                }
                connectionStations.add(station);
            });
            stationIndex.addConnection(connectionStations);
        });
    }

    private static void parseStations(JSONObject stationsObject)
    {
        stationsObject.keySet().forEach(lineNumberObject ->
        {
            int lineNumber = Integer.parseInt((String) lineNumberObject);
            Line line = stationIndex.getLine(lineNumber);
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
            stationsArray.forEach(stationObject ->
            {
                Station station = new Station((String) stationObject, line);
                stationIndex.addStation(station);
                line.addStation(station);
            });
        });
    }

    private static void parseLines(JSONArray linesArray)
    {
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            Line line = new Line(
                    ((Long) lineJsonObject.get("number")).intValue(),
                    (String) lineJsonObject.get("name")
            );
            stationIndex.addLine(line);
        });
    }

    private static String getJsonFile()
    {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            lines.forEach(line -> builder.append(line));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

}
