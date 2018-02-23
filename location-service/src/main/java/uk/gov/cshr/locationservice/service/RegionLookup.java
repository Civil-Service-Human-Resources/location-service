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

class RegionLookup {

    private static final Logger log = LoggerFactory.getLogger(RegionLookup.class);

    private static final Map<UK_NUTS, List<Path2D.Double>> NUTS_MAP = createNutsMap();

    private RegionLookup() {
    }

    static UK_NUTS findRegion(Double latitude, Double longitude) {

        for (Map.Entry<UK_NUTS, List<Path2D.Double>> entry : NUTS_MAP.entrySet()) {

            UK_NUTS ukNuts = entry.getKey();
            List<Path2D.Double> polygons = entry.getValue();

            for (Path2D.Double polygon : polygons) {

                if (polygon.contains(latitude, longitude)) {
                    return ukNuts;
                }
            }
        }

        return null;
    }

    private static Map<UK_NUTS, List<Path2D.Double>> createNutsMap() {

        HashMap<UK_NUTS, List<Path2D.Double>> ukNutsMap = new HashMap<>();
        JsonNode jsonNode = readJsonData();

        Iterator<JsonNode> jsonNodeIterator = jsonNode.get("features").iterator();

        while (jsonNodeIterator.hasNext()) {
            readFeatures(jsonNodeIterator, ukNutsMap);
        }

        return ukNutsMap;
    }

    private static void readFeatures(Iterator<JsonNode> jsonNodeIterator, HashMap<UK_NUTS, List<Path2D.Double>> ukNutsMap) {

        JsonNode featureNode = jsonNodeIterator.next();
        String nutsID = featureNode.get("properties").get("NUTS_ID").asText();

        UK_NUTS ukNut;

        try {
            ukNut = UK_NUTS.valueOf(nutsID);

            List<Path2D.Double> pathsList = new ArrayList<>();
            ukNutsMap.put(ukNut, pathsList);
            Iterator<JsonNode> coordinatesIterator = featureNode.get("geometry").get("coordinates").elements();

            String type = featureNode.get("geometry").get("type").asText();

            while (coordinatesIterator.hasNext()) {

                if (type.equals("Polygon")) {
                    readPolygonData(coordinatesIterator, pathsList);
                }
                else if (type.equals("MultiPolygon")) {
                    readMultiPolygonData(coordinatesIterator, pathsList);
                }
            }
        }
        catch (IllegalArgumentException e) {
            log.debug("nutsID:" + nutsID + " not recognised", e);
        }

    }

    private static void readMultiPolygonData(Iterator<JsonNode> coordinatesIterator, List<Path2D.Double> pathsList) {

        Iterator<JsonNode> valuesIterator = coordinatesIterator.next().elements();
        while (valuesIterator.hasNext()) {
            readPolygonData(valuesIterator, pathsList);
        }
    }

    private static void readPolygonData(Iterator<JsonNode> polygonIterator, List<Path2D.Double> pathsList) {

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
        }

        pathsList.add(polygon);
    }

    private static JsonNode readJsonData() throws RuntimeException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(RegionLookup.class.getResourceAsStream("/uknuts.json"));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
