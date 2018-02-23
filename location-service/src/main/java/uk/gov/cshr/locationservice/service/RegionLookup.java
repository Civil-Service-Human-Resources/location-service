package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.geom.Path2D;
import java.io.FileInputStream;
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

    private static Map<UK_NUTS, List<Path2D.Double>> nutsMap;

    private RegionLookup() {
    }

    private static Map<UK_NUTS, List<Path2D.Double>> createNutsMap() throws IOException {

        HashMap<UK_NUTS, List<Path2D.Double>> ukNutsMap = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new FileInputStream("src/main/resources/uknuts.json"));

        Iterator<JsonNode> jsonNodeIterator = jsonNode.get("features").iterator();

        while (jsonNodeIterator.hasNext()) {

            JsonNode featureNode = jsonNodeIterator.next();

            String nutsID = featureNode.get("properties").get("NUTS_ID").asText();

            UK_NUTS ukNut;

            try {
                ukNut = UK_NUTS.valueOf(nutsID);
            }
            catch (IllegalArgumentException e) {
                log.debug("nutsID:" + nutsID + " not recognised", e);
                continue;
            }

            List<Path2D.Double> pathsList = new ArrayList<>();
            ukNutsMap.put(ukNut, pathsList);

            Iterator<JsonNode> coordinatesIterator = featureNode.get("geometry").get("coordinates").elements();

            while (coordinatesIterator.hasNext()) {

                if (featureNode.get("geometry").get("type").asText().equals("Polygon")) {

                    Path2D.Double polygon = new Path2D.Double();

                    boolean moved = false;

                    Iterator<JsonNode> valuesIterator = coordinatesIterator.next().elements();
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
                else if (featureNode.get("geometry").get("type").asText().equals("MultiPolygon")) {

                    Iterator<JsonNode> valuesIterator = coordinatesIterator.next().elements();
                    while (valuesIterator.hasNext()) {

                        Iterator<JsonNode> polygonIterator = valuesIterator.next().elements();

                        Path2D.Double polygon = new Path2D.Double();

                        boolean moved = false;

                        while (polygonIterator.hasNext()) {

                            JsonNode polyNode = polygonIterator.next();
                            Double longitude = polyNode.get(0).asDouble();
                            Double latitude = polyNode.get(1).asDouble();

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
                }
            }
        }

        return ukNutsMap;
    }

    static UK_NUTS findRegion(Double latitude, Double longitude) {

        try {

            if (nutsMap == null) {
                nutsMap = createNutsMap();
            }

            for (Map.Entry<UK_NUTS, List<Path2D.Double>> entry : nutsMap.entrySet()) {

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
        catch (IOException ioe) {
            throw new RuntimeException("Exception initialising the NUTS map", ioe);
        }
    }
}
