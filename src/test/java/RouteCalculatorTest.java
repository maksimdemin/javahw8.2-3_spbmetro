import core.Line;
import core.Station;
import junit.framework.TestCase;
import java.util.*;

public class RouteCalculatorTest extends TestCase
{
    List<Station> route;
    List<Station> routeBetweenSameStation;
    List<Station> routeMetroStationsOnSameLine;
    List<Station> routeOneTransferMetroStations;
    List<Station> routeTwoTransferMetroStations;

    StationIndex stationIndex;
    RouteCalculator routeCalculator;


    @Override
    protected void setUp() throws Exception
    {
        /*
        Схема мини метро
                                                            (3)
                                                          (THREE)
                                                           PPPP
         (1)                                                ⟰
        (ONE)                                               ⟱
        AAAA                                               RRRR
         ⟰                                                 ⟰
         ⟱                                                 ⟱
        BBBB                                               QQQQ
         ⟰                                                 ⟰
         ⟱                                                 ⟱
        CCCC                                               SSSS
         ⟰                                                 ⟰
         ⟱                                                 ⟱
     DDDD/JJJJ ⤆⤇ КККК ⤆⤇ LLLL ⤆⤇ MMMM ⤆⤇ NNNN ⤆⤇ OOOO/TTTT
         ⟰                                                 ⟰
         ⟱                                                 ⟱
        EEEE                                               UUUU
         ⟰                                                 ⟰
         ⟱                                                 ⟱
        FFFF                                               XXXX

         */


        stationIndex = new StationIndex();
        route = new ArrayList<>();

        //линии
        Line line1 = new Line(1, "ONE");
        Line line2 = new Line(2, "TWO");
        Line line3 = new Line(3, "THREE");

        //станции
        Station stationAAAA = new Station("AAAA", line1);
        Station stationBBBB = new Station("BBBB", line1);
        Station stationCCCC = new Station("CCCC", line1);
        Station stationDDDD = new Station("DDDD", line1);
        Station stationEEEE = new Station("EEEE", line1);
        Station stationFFFF = new Station("FFFF", line1);

        Station stationJJJJ = new Station("JJJJ", line2);
        Station stationKKKK = new Station("KKKK", line2);
        Station stationLLLL = new Station("LLLL", line2);
        Station stationMMMM = new Station("MMMM", line2);
        Station stationNNNN = new Station("NNNN", line2);
        Station stationOOOO = new Station("OOOO", line2);

        Station stationPPPP = new Station("PPPP", line3);
        Station stationRRRR = new Station("RRRR", line3);
        Station stationQQQQ = new Station("QQQQ", line3);
        Station stationSSSS = new Station("SSSS", line3);
        Station stationTTTT = new Station("TTTT", line3);
        Station stationUUUU = new Station("UUUU", line3);
        Station stationXXXX = new Station("XXXX", line3);

        //добавляем станции в линии
        line1.addStation(stationAAAA);
        line1.addStation(stationBBBB);
        line1.addStation(stationCCCC);
        line1.addStation(stationDDDD);
        line1.addStation(stationEEEE);
        line1.addStation(stationFFFF);

        line2.addStation(stationJJJJ);
        line2.addStation(stationKKKK);
        line2.addStation(stationLLLL);
        line2.addStation(stationMMMM);
        line2.addStation(stationNNNN);
        line2.addStation(stationOOOO);

        line3.addStation(stationPPPP);
        line3.addStation(stationRRRR);
        line3.addStation(stationQQQQ);
        line3.addStation(stationSSSS);
        line3.addStation(stationTTTT);
        line3.addStation(stationUUUU);
        line3.addStation(stationXXXX);

        //добавляем линии в метро
        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        //добавляем станции в метро
        // линия 1
        stationIndex.addStation(stationAAAA);
        stationIndex.addStation(stationBBBB);
        stationIndex.addStation(stationCCCC);
        stationIndex.addStation(stationDDDD);
        stationIndex.addStation(stationEEEE);
        stationIndex.addStation(stationFFFF);
        // линия 2
        stationIndex.addStation(stationJJJJ);
        stationIndex.addStation(stationKKKK);
        stationIndex.addStation(stationLLLL);
        stationIndex.addStation(stationMMMM);
        stationIndex.addStation(stationNNNN);
        stationIndex.addStation(stationOOOO);
        // линия 3
        stationIndex.addStation(stationPPPP);
        stationIndex.addStation(stationRRRR);
        stationIndex.addStation(stationQQQQ);
        stationIndex.addStation(stationSSSS);
        stationIndex.addStation(stationTTTT);
        stationIndex.addStation(stationUUUU);
        stationIndex.addStation(stationXXXX);

        //добавляем пересадки в метро
        stationIndex.addConnection(new ArrayList<>(Arrays.asList(stationDDDD, stationJJJJ)));
        stationIndex.addConnection(new ArrayList<>(Arrays.asList(stationOOOO, stationTTTT)));

        //создаем routeCalculator
        routeCalculator = new RouteCalculator(stationIndex);

        //задаем маршрут для теста testCalculateDuration()
        route.add(stationCCCC);
        route.add(stationDDDD);

        route.add(stationJJJJ);
        route.add(stationKKKK);
        route.add(stationLLLL);
        route.add(stationMMMM);
        route.add(stationNNNN);
        route.add(stationOOOO);

        route.add(stationTTTT);
        route.add(stationUUUU);
    }

    public void testCalculateDuration()
    {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 24.5;
        assertEquals(expected, actual);
    }

    public void testCalculatorRouteMetroBetweenSameStation()
    {

        routeBetweenSameStation = new ArrayList<>();
        routeBetweenSameStation.add(stationIndex.getStation("BBBB"));

        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("BBBB", 1), stationIndex.getStation("BBBB", 1));

        assertEquals(routeBetweenSameStation, actual);

        System.out.println(routeBetweenSameStation);
        System.out.println(actual);
    }


    public void testCalculatorRouteMetroStationsOnSameLine()
    {
        routeMetroStationsOnSameLine = new ArrayList<>();
        routeMetroStationsOnSameLine.add(stationIndex.getStation("AAAA"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("BBBB"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("CCCC"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("DDDD"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("EEEE"));

          List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("AAAA"), stationIndex.getStation("EEEE"));

        assertEquals(routeMetroStationsOnSameLine, actual);


        System.out.println(routeMetroStationsOnSameLine);
        System.out.println(actual);
    }

    public void testCalculatorRouteOneTransferMetroStations()
    {
        routeOneTransferMetroStations = new ArrayList<>();
        routeOneTransferMetroStations.add(stationIndex.getStation("FFFF"));
        routeOneTransferMetroStations.add(stationIndex.getStation("EEEE"));
        routeOneTransferMetroStations.add(stationIndex.getStation("DDDD"));
        routeOneTransferMetroStations.add(stationIndex.getStation("JJJJ"));
        routeOneTransferMetroStations.add(stationIndex.getStation("KKKK"));
        routeOneTransferMetroStations.add(stationIndex.getStation("LLLL"));
        routeOneTransferMetroStations.add(stationIndex.getStation("MMMM"));

        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("FFFF"), stationIndex.getStation("MMMM"));

        assertEquals(routeOneTransferMetroStations, actual);

        System.out.println(routeOneTransferMetroStations);
        System.out.println(actual);
    }


    public void testCalculatorRouteTwoTransferMetroStations()
    {
        routeTwoTransferMetroStations = new ArrayList<>();
        routeTwoTransferMetroStations.add(stationIndex.getStation("BBBB"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("CCCC"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("DDDD"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("JJJJ"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("KKKK"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("LLLL"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("MMMM"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("NNNN"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("OOOO"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("TTTT"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("UUUU"));
        routeTwoTransferMetroStations.add(stationIndex.getStation("XXXX"));


        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("BBBB"), stationIndex.getStation("XXXX"));

        assertEquals(routeTwoTransferMetroStations, actual);

        System.out.println(routeTwoTransferMetroStations);
        System.out.println(actual);
    }


    @Override
    protected void tearDown() throws Exception
    {

    }
}
