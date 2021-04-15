function getLocation(){
    if (navigator.geolocation) {

        navigator.permissions.query({name:'geolocation'}).then(function(result) {
            if (result.state == 'granted') {
                navigator.geolocation.getCurrentPosition(sentData);
            } else if (result.state == 'denied') {
                alert("You need allow access to your location to use this feature")
            }
        });
    } else {
        alert("Your browser does not support this feature")
    }
}

function sentData(position) {
    geoLocationData (
        [
            {
                name:'currentLatitude',
                value: position.coords.latitude
            },
            {
                name:'currentLongitude',
                value: position.coords.longitude
            },
            {
                name:'accuracyCurrentPosition',
                value: position.coords.accuracy
            }
        ]
    );
}

