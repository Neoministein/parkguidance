package com.neo.parkguidance.web.impl.component.heatmap;

import com.neo.parkguidance.web.api.component.heatmap.HeatmapComponentLogic;
import com.neo.parkguidance.web.impl.component.ParkGuidanceUIComponent;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@FacesComponent(tagName = "heatMap", createTag = true, namespace = ParkGuidanceUIComponent.NAME_SPACE)
public class HeatMapComponent extends ParkGuidanceUIComponent {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        HeatmapComponentLogic logic = (HeatmapComponentLogic) getAttributes().get("heatmapLogic");
        ResponseWriter writer = context.getResponseWriter();
        writer.write(logic.generateHTML());
    }
}
