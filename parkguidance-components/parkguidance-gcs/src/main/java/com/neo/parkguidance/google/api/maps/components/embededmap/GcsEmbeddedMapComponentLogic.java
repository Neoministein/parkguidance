package com.neo.parkguidance.google.api.maps.components.embededmap;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.google.api.maps.embed.EmbeddedMap;
import com.neo.parkguidance.web.api.component.embeddedmap.EmbeddedMapComponentLogic;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GcsEmbeddedMapComponentLogic implements EmbeddedMapComponentLogic {

    @Inject
    StoredValueService storedValueService;

    @Override
    public String generateHTML(Address center) {
        String url =  EmbeddedMap.buildPlaceUrl(
                storedValueService.getString(StoredValue.V_GOOGLE_MAPS_API_EXTERNAL),
                EmbeddedMap.MapType.roadmap,
                center).toString();

        return    "<iframe frameborder=\"0\"\n"
                + "        style=\"border:0; width: 100%; height: 100%\"\n"
                + "        src=\"" + url +"\">\n"
                + "</iframe>\n";
    }
}
