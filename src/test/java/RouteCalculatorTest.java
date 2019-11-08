import core.Line;
import core.Station;
import junit.framework.TestCase;

import javax.swing.event.TreeSelectionEvent;
import java.util.*;

public class RouteCalculatorTest extends TestCase
{

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


    public void testGetShortestRoute()
    {
        Line line1 = new Line(1, "ONE");
        Line line2 = new Line(2, "TWO");
        Line line3 = new Line(3, "THREE");

        Station stationCCCC = new Station("CCCC", line1);
        Station stationDDDD = new Station("DDDD", line1);
        Station stationKKKK = new Station("KKKK", line1);

        Station stationJJJJ = new Station("JJJJ", line2);
        Station stationOOOO = new Station("OOOO", line2);

        Station stationPPPP = new Station("PPPP", line3);
        Station stationRRRR = new Station("RRRR", line3);

        // собираем stationIndex
        StationIndex stationIndex = new StationIndex();

        // добавляем в каждую line соответствующие стнации
        List<Station> stations = new ArrayList<>();
        stations.add(stationCCCC);
        stations.add(stationDDDD);
        stations.add(stationKKKK);
        stations.add(stationJJJJ);
        stations.add(stationOOOO);
        stations.add(stationPPPP);
        stations.add(stationRRRR);

        line1.addStation(stationCCCC);
        line1.addStation(stationDDDD);
        line1.addStation(stationKKKK);

        line2.addStation(stationJJJJ);
        line2.addStation(stationOOOO);

        line3.addStation(stationPPPP);
        line3.addStation(stationRRRR);

        // добавляем станции в stationIndex      (stations -> stationIndex)
        stationIndex.addStation(stationCCCC);
        stationIndex.addStation(stationDDDD);
        stationIndex.addStation(stationKKKK);


        stationIndex.addStation(stationJJJJ);
        stationIndex.addStation(stationOOOO);

        stationIndex.addStation(stationPPPP);
        stationIndex.addStation(stationRRRR);
        // добавлем данные в number@line         (number2line  -> stationIndex)
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        // добавляем данные в connections
        List<Station> listStations = new ArrayList<>(stationIndex.stations);
        stationIndex.addConnection(listStations);


        RouteCalculator routeCalculator = new RouteCalculator(stationIndex);
          List<Station> actual = routeCalculator.getShortestRoute(stationJJJJ, stationRRRR);

        assertEquals(listStations, actual);


    }

    @Override
    protected void tearDown() throws Exception
    {

    }
}
