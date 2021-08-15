let map;
let circles;

function initMap(points) {
    let parsed = points.replaceAll("$-$-$", "\\\"");
    let jsonData = JSON.parse(parsed);

    map = new google.maps.Map(document.getElementById("map"), {
        center:  { lat: jsonData.lat, lng: jsonData.lng },
        zoom: jsonData.zoom,
        mapId: jsonData.mapId,
        mapTypeId: jsonData.mapTypeId
    });
    setMapCircle(jsonData);
}

function updateHeatMapPoints(points) {
    let parsed = points.replaceAll("$-$-$", "\\\"");
    let jsonData = JSON.parse(parsed);

    for (let i = 0; i < circles.length;i++) {
        circles[i].setMap(null);
    }
    setMapCircle(jsonData);
}

function setMapCircle(circleData) {
    circles = [circleData.parkgingGarage.length];
    for (let i = 0; i < circleData.parkgingGarage.length; i++) {
        circles[i] = new google.maps.Circle({
            strokeColor: circleData.parkgingGarage[i].color,
            strokeOpacity: circleData.parkgingGarage[i].strokeOpacity,
            strokeWeight: circleData.parkgingGarage[i].strokeWeight,
            fillColor: circleData.parkgingGarage[i].color,
            fillOpacity: circleData.parkgingGarage[i].fillOpacity,
            map,
            center: {lat: circleData.parkgingGarage[i].lat, lng: circleData.parkgingGarage[i].lng},
            radius: circleData.parkgingGarage[i].radius,
        });

        const infowindow = new google.maps.InfoWindow({
            content: circleData.parkgingGarage[i].content,
        });
        google.maps.event.addListener(circles[i], 'click', function(ev) {
            infowindow.setPosition(ev.latLng);
            infowindow.open(map);
        });

    }
}