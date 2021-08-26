package com.neo.parkguidance.web.user.impl.heatmap;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.utils.MathUtils;
import com.neo.parkguidance.core.api.dao.AbstractEntityDao;
import com.neo.parkguidance.parkdata.impl.service.ParkDataService;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles retrieving the Heatmap data
 */
@Stateless
public class HeatMapDataServiceFacade {

    private static final int HALF_HOURS_IN_DAY = 48;

    @Inject
    ParkDataService parkDataService;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    public List<JSONObject> generateHeatMapPoints(boolean defaultColor) {
        Map<String, List<Integer>> parkData = parkDataService.getParkData();
        List<JSONObject> heatMapPointList = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i+=2) {
            JSONObject root = generateRoot();

            JSONArray garagePoints = new JSONArray();
            root.put("parkgingGarage", garagePoints);
            for (ParkingGarage parkingGarage: parkingGarageDao.findAll()) {
                Integer occupied = parkData.get(parkingGarage.getKey()).get(i);
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
        sb.append("<a href=\"/park-guidance/data?key=").append(parkingGarage.getKey()).append("\" target=\"_blank\" class=\"ui-link ui-widget\">");
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
        sb.append("</div></p></div>");
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
            return "rgb(144, 144, 144)";
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
}
