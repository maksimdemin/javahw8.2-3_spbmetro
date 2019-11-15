import core.Line;
import core.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@DisplayName("RouteCalculator Tests")
class JUnit5Test {
    List<Station> route;
    List<Station> routeMetroStationsOnSameLine;
    List<Station> routeOneTransferMetroStations;
    List<Station> routeTwoTransferMetroStations;

    StationIndex stationIndex;
    RouteCalculator routeCalculator;


    @BeforeEach
    void setUp() throws Exception
    {
        /*
        Схема мини метро
                                                           (3)
                                                           (C)
                                                           PPPP
         (1)                                                ⟰
         (A)                                                ⟱
        AAAA                                               RRRR
         ⟰                                                 ⟰
         ⟱                                                 ⟱
        BBBB                                               QQQQ
         ⟰                                                 ⟰
         ⟱                                                 ⟱
        CCCC                                               SSSS
         ⟰                       (2)                       ⟰
         ⟱                       (B)                       ⟱
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
        Line lineA = new Line(1, "A");
        Line lineB = new Line(2, "B");
        Line lineC = new Line(3, "C");

        //станции
        TreeSet<Station> stations = new TreeSet<>();

        String[] names = {"AAAA", "BBBB", "CCCC", "DDDD", "EEEE", "FFFF",
                "JJJJ", "KKKK", "LLLL", "MMMM", "NNNN", "OOOO",
                "PPPP", "RRRR", "QQQQ", "SSSS", "TTTT", "UUUU", "XXXX"};

        for (int i = 0; i < names.length; i++) {
            if (i < 6) {
                stations.add(new Station(names[i], lineA));
            } else if (i < 12) {
                stations.add(new Station(names[i], lineB));
            } else {
                stations.add(new Station(names[i], lineC));
            }
        }

        //добавляем станции в линии
        for (Station station: stations) {
            station.getLine().addStation(station);
        }

        //добавляем линии в метро
        stationIndex.addLine(lineA);
        stationIndex.addLine(lineB);
        stationIndex.addLine(lineC);

        //добавляем станции в метро
        for (Station station: stations) {
            stationIndex.addStation(station);
        }

        //добавляем пересадки в метро
        stationIndex.addConnection(new ArrayList<>(Arrays.asList(stationIndex.getStation("DDDD"), stationIndex.getStation("JJJJ"))));
        stationIndex.addConnection(new ArrayList<>(Arrays.asList(stationIndex.getStation("OOOO"), stationIndex.getStation("TTTT"))));

        //создаем routeCalculator
        routeCalculator = new RouteCalculator(stationIndex);

        //задаем маршрут для теста testCalculateDuration()
        route.add(stationIndex.getStation("CCCC"));
        route.add(stationIndex.getStation("DDDD"));

        route.add(stationIndex.getStation("JJJJ"));
        route.add(stationIndex.getStation("KKKK"));
        route.add(stationIndex.getStation("LLLL"));
        route.add(stationIndex.getStation("MMMM"));
        route.add(stationIndex.getStation("NNNN"));
        route.add(stationIndex.getStation("OOOO"));

        route.add(stationIndex.getStation("TTTT"));
        route.add(stationIndex.getStation("UUUU"));
    }

    @Test
    @DisplayName("Route duration test")
    void firstTest()
    {
        System.out.println("First test method is OK");
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 24.5;
        assertReflectionEquals(expected, actual);
    }


    @Test
    @DisplayName("Stations on same line test route")
    void twoTest()
    {
        System.out.println("Second test method is OK");
        routeMetroStationsOnSameLine = new ArrayList<>();
        routeMetroStationsOnSameLine.add(stationIndex.getStation("AAAA"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("BBBB"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("CCCC"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("DDDD"));
        routeMetroStationsOnSameLine.add(stationIndex.getStation("EEEE"));

        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("AAAA"), stationIndex.getStation("EEEE"));

        assertReflectionEquals(routeMetroStationsOnSameLine, actual);
    }


    @Test
    @DisplayName("One Connection test route")
    void threeTest()
    {
        System.out.println("Third test method is OK");
        routeOneTransferMetroStations = new ArrayList<>();
        routeOneTransferMetroStations.add(stationIndex.getStation("FFFF"));
        routeOneTransferMetroStations.add(stationIndex.getStation("EEEE"));
        routeOneTransferMetroStations.add(stationIndex.getStation("DDDD"));
        routeOneTransferMetroStations.add(stationIndex.getStation("JJJJ"));
        routeOneTransferMetroStations.add(stationIndex.getStation("KKKK"));
        routeOneTransferMetroStations.add(stationIndex.getStation("LLLL"));
        routeOneTransferMetroStations.add(stationIndex.getStation("MMMM"));

        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("FFFF"), stationIndex.getStation("MMMM"));

        assertReflectionEquals(routeOneTransferMetroStations, actual);
    }


    @Test
    @DisplayName("Two Connection test route")
    void fourTest()
    {
        System.out.println("Fourth test method is OK");
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

        assertReflectionEquals(routeTwoTransferMetroStations, actual);
    }
}
