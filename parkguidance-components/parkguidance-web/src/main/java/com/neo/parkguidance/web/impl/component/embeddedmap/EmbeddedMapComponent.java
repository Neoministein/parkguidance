package com.neo.parkguidance.web.impl.component.embeddedmap;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.web.api.component.embeddedmap.EmbeddedMapComponentLogic;
import com.neo.parkguidance.web.impl.component.ParkGuidanceUIComponent;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@FacesComponent(tagName = "embeddedMap", createTag = true, namespace = ParkGuidanceUIComponent.NAME_SPACE)
public class EmbeddedMapComponent extends ParkGuidanceUIComponent {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        EmbeddedMapComponentLogic logic = (EmbeddedMapComponentLogic) getAttributes().get("embeddedMapLogic");
        Address address = (Address) getAttributes().get("address");
        ResponseWriter writer = context.getResponseWriter();
        writer.write(logic.generateHTML(address));
    }
}
