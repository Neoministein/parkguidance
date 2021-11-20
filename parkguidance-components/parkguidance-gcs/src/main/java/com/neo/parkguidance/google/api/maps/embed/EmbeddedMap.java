package com.neo.parkguidance.google.api.maps.embed;

import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.google.api.constants.GoogleConstants;

import java.util.List;

/**
 * This class is used for creating urls for embedded google maps map
 * https://developers.google.com/maps/documentation/embed/embedding-map#view_mode
 */
public class EmbeddedMap {

    //IMPORTANT keep enums lower case due to api case sensitivity

    public static final String API_URL = "https://www.google.com/maps/embed/v1/";

    @SuppressWarnings("java:S115")
    public enum Type {
        place,
        view,
        directions,
        streetview,
        search
    }

    @SuppressWarnings("java:S115")
    public enum MapType {
        roadmap,
        satellite
    }


    private EmbeddedMap() {}

    public static StringBuilder buildPlaceUrl(String key, MapType mapType, Address address) {
        StringBuilder url = defaultUrl(key, Type.place, mapType);
        url.append("&q=").append(GoogleConstants.addressQuery(address));
        return url;
    }

    public static StringBuilder buildPlaceUrl(String key, MapType mapType, Address address,
            Integer zoom,
            Double longitude, Double latitude) {
        StringBuilder sb = buildPlaceUrl(key, mapType, address);
        if (zoom != null) {
            sb.append(zoom(zoom));
        }
        if (longitude != null && latitude != null) {
            sb.append(center(longitude, latitude));
        }
        return sb;
    }

    public static StringBuilder buildView(String key, MapType mapType, Double longitude, Double latitude) {
        return defaultUrl(key, Type.view, mapType).append(center(longitude,latitude));
    }

    public static StringBuilder buildView(String key, MapType mapType, Double longitude, Double latitude, Integer zoom) {
        return buildView(key, mapType ,longitude, latitude).append(zoom(zoom));
    }

    public static StringBuilder buildDirections(String key, MapType mapType, Address origin, Address destination) {
        return defaultUrl(key, Type.directions, mapType)
                .append("&origin=")
                .append(GoogleConstants.addressQuery(origin))
                .append("&destination=")
                .append(GoogleConstants.addressQuery(destination));
    }

    public static StringBuilder buildDirections(String key, MapType mapType, Address origin, Address destination,
            List<Address> waypoint,
            Direction.TransportationMode mode,
            Direction.Avoid avoid,
            Double longitude, Double latitude,
            Integer zoom) {
        StringBuilder sb = buildDirections(key, mapType, origin, destination)
                .append("&mode=")
                .append(mode);
        if (waypoint != null) {
            sb.append(Direction.waypoints(waypoint));
        }
        if (mode != null) {
            sb.append(Direction.mode(mode));
        }
        if (avoid != null) {
            sb.append(Direction.avoid(avoid));
        }
        if (longitude != null && latitude != null) {
            sb.append(center(longitude,latitude));
        }
        if (zoom != null) {
            sb.append(zoom(zoom));
        }

        return sb;
    }

    public static StringBuilder buildStreetView(String key, MapType mapType, Double locLongitude, Double locLatitude) {
        return defaultUrl(key, Type.streetview, mapType)
                .append("&location")
                .append(locLongitude)
                .append(",")
                .append(locLatitude);
    }

    public static StringBuilder buildStreetView(String key, MapType mapType, Double locLongitude, Double locLatitude,
            Integer heading,
            Integer pitch,
            Integer fov) {
        StringBuilder sb = buildStreetView(key, mapType, locLongitude, locLatitude);
        if (heading != null) {
            sb.append(StreetView.heading(heading));
        }
        if (pitch != null) {
            sb.append(StreetView.pitch(pitch));
        }
        if (fov != null) {
            sb.append(StreetView.fov(fov));
        }
        return sb;
    }

    private static StringBuilder center(Double longitude, Double latitude) {
        return new StringBuilder("&center=")
                .append(longitude)
                .append(",")
                .append(latitude);
    }

    private static StringBuilder zoom(Integer zoom) {
        return new StringBuilder("&zoom=").append(zoom);
    }

    private static StringBuilder defaultUrl(String key, Type mapMode, MapType mapType) {
        return new StringBuilder(API_URL + mapMode + "?key=" + key + "&maptype=" + mapType);
    }

    public static class Direction {

        private Direction() {}

        @SuppressWarnings("java:S115")
        public enum TransportationMode {
            driving,
            walking,
            bicycling,
            transit,
            flying
        }

        @SuppressWarnings("java:S115")
        public enum Avoid {
            trolls,
            ferries,
            highways
        }

        public static StringBuilder waypoints(List<Address> addressList) {
            StringBuilder sb = new StringBuilder("&waypoints=");

            for (Address address: addressList) {
                sb.append(GoogleConstants.addressQuery(address)).append('|');
            }
            return new StringBuilder(sb.toString().trim());
        }

        public static StringBuilder mode(Direction.TransportationMode mode) {
            return new StringBuilder("&mode=")
                    .append(mode);
        }

        public static StringBuilder avoid(Direction.Avoid avoid) {
            return new StringBuilder("&avoid=")
                    .append(avoid);
        }
    }

    public static class StreetView {

        private StreetView() {}

        public static StringBuilder heading(Integer heading) {
            return new StringBuilder("&heading=").append(heading);
        }

        public static StringBuilder pitch(Integer pitch) {
            return new StringBuilder("&pitch=").append(pitch);
        }

        public static StringBuilder fov(Integer fov) {
            return new StringBuilder("&fov=").append(fov);
        }
    }
}
