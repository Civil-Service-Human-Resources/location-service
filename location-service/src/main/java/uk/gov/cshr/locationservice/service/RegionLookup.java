package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegionLookup {

    private static final Logger log = LoggerFactory.getLogger(RegionLookup.class);

    private static Map<UK_NUTS, List<Path2D.Double>> NUTS_MAP;

    private static int numberOfCoordinates = 0;

    private RegionLookup() {
    }

    public static void initialise() {
        log.debug("initialise createNutsMap");
        NUTS_MAP = createNutsMap();
        log.debug("initialise createNutsMap: done");
    }

    static UK_NUTS findRegion(Double latitude, Double longitude) {

        log.debug("findRegion " + latitude + ":" + longitude);

        if ( NUTS_MAP == null ) {
            initialise();
        }

        for (Map.Entry<UK_NUTS, List<Path2D.Double>> entry : NUTS_MAP.entrySet()) {

            UK_NUTS ukNuts = entry.getKey();
            List<Path2D.Double> polygons = entry.getValue();

            for (Path2D.Double polygon : polygons) {

                if (polygon.contains(latitude, longitude)) {
                    log.debug("return  " + ukNuts);
                    return ukNuts;
                }
            }
        }

        log.debug("return  null");
        return null;
    }

    private static Map<UK_NUTS, List<Path2D.Double>> createNutsMap() {

        HashMap<UK_NUTS, List<Path2D.Double>> ukNutsMap = new HashMap<>();
        JsonNode jsonNode = readJsonData();
        System.gc();

        Iterator<JsonNode> jsonNodeIterator = jsonNode.get("features").iterator();

        while (jsonNodeIterator.hasNext()) {
            readFeatures(jsonNodeIterator, ukNutsMap);            
        }

        return ukNutsMap;
    }

//    private static void checkMemory() {
//
//        numberOfCoordinates++;
//
//        if ( numberOfCoordinates % 10000 == 0 ) {
//
//            System.out.println("numberOfCoordinates=" + numberOfCoordinates);
//
//            // Get current size of heap in bytes
//            long heapSize = Runtime.getRuntime().totalMemory();
//            System.out.println("heapSize=" + heapSize);
//            // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
//            long heapMaxSize = Runtime.getRuntime().maxMemory();
//            System.out.println("heapMaxSize=" + heapMaxSize);
//             // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
//            long heapFreeSize = Runtime.getRuntime().freeMemory();
//            System.out.println("heapFreeSize=" + heapFreeSize);
//
//            System.gc();
//        }
//
//
//
//    }

    private static void readFeatures(Iterator<JsonNode> jsonNodeIterator, HashMap<UK_NUTS, List<Path2D.Double>> ukNutsMap) {

        JsonNode featureNode = jsonNodeIterator.next();
        String nutsID = featureNode.get("properties").get("nuts118cd").asText();

        UK_NUTS ukNut;

        try {
            ukNut = UK_NUTS.valueOf(nutsID);

            List<Path2D.Double> pathsList = new ArrayList<>();
            ukNutsMap.put(ukNut, pathsList);
            Iterator<JsonNode> coordinatesIterator = featureNode.get("geometry").get("coordinates").elements();

            String type = featureNode.get("geometry").get("type").asText();

            while (coordinatesIterator.hasNext()) {

                if (type.equals("Polygon")) {
                    pathsList.add(readPolygonData(coordinatesIterator));
                }
                else if (type.equals("MultiPolygon")) {
                    readMultiPolygonData(coordinatesIterator, pathsList);
                }
            }
        }
        catch (IllegalArgumentException e) {
            log.debug("nutsID:" + nutsID + " not recognised in UK_NUTS so skipped", e);
        }

    }

    private static void readMultiPolygonData(Iterator<JsonNode> coordinatesIterator, List<Path2D.Double> pathsList) {

        Iterator<JsonNode> valuesIterator = coordinatesIterator.next().elements();
        while (valuesIterator.hasNext()) {
            pathsList.add(readPolygonData(valuesIterator));
        }
    }

    private static Path2D.Double readPolygonData(Iterator<JsonNode> polygonIterator) {

        Path2D.Double polygon = new Path2D.Double();

        boolean moved = false;

        Iterator<JsonNode> valuesIterator = polygonIterator.next().elements();
        while (valuesIterator.hasNext()) {

            JsonNode coordinates = valuesIterator.next();
            Double longitude = coordinates.get(0).asDouble();
            Double latitude = coordinates.get(1).asDouble();

            if (!moved) {                
                polygon.moveTo(latitude, longitude);
                moved = true;
            }
            else {
                polygon.lineTo(latitude, longitude);
            }
//            checkMemory();
        }

        return polygon;
    }

    private static JsonNode readJsonData() throws RuntimeException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(RegionLookup.class.getResourceAsStream(
                    "/NUTS_Level_1_January_2018_Full_Extent_Boundaries_in_the_United_Kingdom.geojson"));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
