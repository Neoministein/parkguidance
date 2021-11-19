function getLocation(){
    if (navigator.geolocation) {
        if(navigator.permissions && navigator.permissions.query) {
            navigator.permissions.query({name:'geolocation'}).then(function(result) {
                if (result.state == 'granted' || result.state == 'prompt') {
                    navigator.geolocation.getCurrentPosition(sentData);
                } else if (result.state == 'denied') {
                    alert("You need allow access to your location to use this feature");
                }
            });
        } else {
            try {
                navigator.geolocation.getCurrentPosition(sentData);
            }catch {
                alert("Your browser does not support this feature");
            }
        }
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

