package com.neo.parkguidance.web.user.pages.heatmap;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.MathUtils;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.event.ParkDataChangeEvent;
import com.neo.parkguidance.elastic.impl.ElasticSearchProvider;
import com.neo.parkguidance.elastic.impl.query.ElasticSearchLowLevelQuery;
import com.neo.parkguidance.web.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles storing the Heatmap
 */
@ApplicationScoped
public class HeatMapDataService {

    private static final Logger LOGGER = LogManager.getLogger(HeatMapDataService.class);

    private static final int HALF_HOURS_IN_DAY = 48;
    public static final String ELASTIC_SORTED_INDEX = "/sorted-parking-data";

    private List<JSONObject> heatMapGradiantNormal;
    private List<JSONObject> heatMapGradiantColorBlind;

    @Inject
    ElasticSearchProvider elasticSearchProvider;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    @PostConstruct
    public void init() {
        heatMapGradiantNormal = generateHeatMapPoints(true);
        heatMapGradiantColorBlind = generateHeatMapPoints(false);
    }

    public List<JSONObject> getHeatMapGradiantColorBlind() {
        return heatMapGradiantColorBlind;
    }

    public List<JSONObject> getHeatMapGradiantNormal() {
        return heatMapGradiantNormal;
    }

    public void changeEvent(@Observes ParkDataChangeEvent changeEvent) {
        if (changeEvent.getStatus().equals(ParkDataChangeEvent.SORTED_RESPONSE)) {
            init();
        }
    }

    private List<JSONObject> generateHeatMapPoints(boolean defaultColor) {
        List<JSONObject> heatMapPointList = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i+=2) {
            JSONObject root = generateRoot();

            JSONArray garagePoints = new JSONArray();
            root.put("parkgingGarage", garagePoints);
            for (ParkingGarage parkingGarage: parkingGarageDao.findAll()) {
                Integer occupied = getAverageOccupation(parkingGarage.getKey(), i);
                garagePoints.put(generateGaragePoint(parkingGarage, occupied, defaultColor));
            }

            heatMapPointList.add(root);
        }

        return heatMapPointList;
    }

    private String stringToDisplay(ParkingGarage parkingGarage, int occupied) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div id=\"content\">");
        sb.append("<div id=\"siteNotice\">");
        sb.append("</div>");
        sb.append("<h1 id=\"firstHeading\" class=\"firstHeading\">").append(Utils.formatted(parkingGarage.getName())).append("</h1>");
        sb.append("<div id=\"bodyContent\">");
        sb.append("<p>");
        sb.append("<a href=\"/park-guidance/data?key=").append(parkingGarage.getKey()).append("\" class=\"ui-link ui-widget\">");
        sb.append("Info</a>");
        sb.append("<br>");
        if (occupied != 0) {
            sb.append("<b>").append("Free Spaces: ").append("</b>");
            sb.append(occupied).append("/").append(parkingGarage.getSpaces());
        } else {
            sb.append("<b>").append("Spaces: ").append("</b>");
            sb.append(parkingGarage.getSpaces());
        }

        sb.append("<br>");
        sb.append(Utils.formatted(parkingGarage.getAddress().getToAddressString()));
        sb.append("</div>");
        sb.append("</p>");
        sb.append("</div>");
        return sb.toString();
    }

    private JSONObject generateRoot() {
        JSONObject root = new JSONObject();
        root.put("mapTypeId", "roadMap");
        root.put("zoom",13);
        root.put("lat", 47.36667);
        root.put("lng", 8.55);
        root.put("mapTypeId", "roadmap");
        root.put("mapId", "154ee332b92aacff");
        return root;
    }

    private String getColor(int spaces, int occupied, boolean defaultColor) {
        if (occupied == 0) {
            return "rgb(255, 255, 255)";
        }
        int clampOccupied = MathUtils.clamp(occupied, 0, spaces);
        double emptyPercent = 100d * clampOccupied / spaces;

        int r;
        int g;
        int b;

        if (defaultColor) {
            r = (int) (255 * ( 100 - emptyPercent) / 100);
            g = (int) (255 * emptyPercent / 100);
            b = 0;
        } else {
            r = (int) (255 * ( 100 - emptyPercent) / 100);
            g = 0;
            b = (int) (255 * emptyPercent / 100);
        }


        return "rgb("+ r +","+ g +","+ b +")";
    }

    private JSONObject generateGaragePoint(ParkingGarage parkingGarage, int occupied, boolean defaultColor) {
        JSONObject garage = new JSONObject();
        garage.put("lng", parkingGarage.getAddress().getLongitude());
        garage.put("lat", parkingGarage.getAddress().getLatitude());
        garage.put("color", getColor(parkingGarage.getSpaces(), occupied, defaultColor));
        garage.put("strokeOpacity",0.8);
        garage.put("strokeWeight",2);
        garage.put("fillOpacity", 0.35);
        garage.put("radius", getRadius(parkingGarage.getSpaces()));
        garage.put("content", stringToDisplay(parkingGarage, occupied));

        return garage;
    }

    /**
     * Gets the garage on a scale from 0 to 500 and returns a range between 50 and 150
     * @param spaces
     * @return
     */
    protected double getRadius(int spaces) {
        double normalized = spaces / 500d;
        return MathUtils.clamp((normalized * 100 + 50), 50, 150);
    }

    private Integer getAverageOccupation(String key, int halfHour) {
        try {
            String result = elasticSearchProvider.sendLowLevelRequest(
                    "GET",
                    ELASTIC_SORTED_INDEX + "/_search?size=0&filter_path=aggregations",
                    getAverageOccupationBody(key,halfHour));
            JSONObject jsonObject = new JSONObject(result);
            Object o = jsonObject.getJSONObject("aggregations").getJSONObject("avg_occupation").get("value");

            if (o.equals(JSONObject.NULL)) {
                return 0;
            }

            return (int) Math.round((Double) o);
        }catch (IOException e) {
            LOGGER.warn("Unable to get Average Occupation at halfhour [{}] in [{}] {}", halfHour, key, e);
        }

        return 0;
    }

    private String getAverageOccupationBody(String key, int halfHour) {
        JSONArray must = ElasticSearchLowLevelQuery.combineToArray(
                ElasticSearchLowLevelQuery.match("garage", key),
                ElasticSearchLowLevelQuery.match("halfHour",halfHour));

        JSONObject bool = ElasticSearchLowLevelQuery.combineToJSONObject("must",must);
        JSONObject query = ElasticSearchLowLevelQuery.combineToJSONObject("bool",bool);

        JSONObject avg = ElasticSearchLowLevelQuery.averageAggregation("occupied");
        JSONObject avgOccupation = ElasticSearchLowLevelQuery.combineToJSONObject("avg",avg);
        JSONObject aggs = ElasticSearchLowLevelQuery.combineToJSONObject("avg_occupation",avgOccupation);


        JSONObject root = ElasticSearchLowLevelQuery.combineToJSONObject(
                new ElasticSearchLowLevelQuery.Entry("query", query),
                new ElasticSearchLowLevelQuery.Entry("aggs", aggs)
        );

        return root.toString();
    }

}
