package com.neo.parkguidance.web.impl.component;

import com.neo.parkguidance.core.api.security.oauth2.OAuth2Client;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@FacesComponent(tagName = "oauth2", createTag = true, namespace = ParkGuidanceUIComponent.NAME_SPACE)
public class OAuth2Component extends ParkGuidanceUIComponent {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        OAuth2Client logic = (OAuth2Client) getAttributes().get("logic");
        ResponseWriter writer = context.getResponseWriter();
        writer.write(logic.renderXhtml());
    }
}
