package com.neo.parkguidance.web.admin.pages.sheetlist;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.web.infra.entity.DataSheetEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.util.Messages;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import static com.neo.parkguidance.web.utils.Utils.pingHost;

@Stateless
public class DataSheetListFacade {

    @Inject
    DataSheetEntityService dataSheetService;

    public void initDataModel(DataSheetListModel model) {
        model.setData(new LazyDataModel<DataSheet>() {
            @Override
            public List<DataSheet> load(int first, int pageSize,
                    String sortField, SortOrder sortOrder,
                    Map<String, FilterMeta> filters) {
                com.neo.parkguidance.web.infra.table.SortOrder order = null;
                if (sortOrder != null) {
                    switch (sortOrder) {
                    case UNSORTED:
                        order = com.neo.parkguidance.web.infra.table.SortOrder.UNSORTED;
                        break;
                    case ASCENDING:
                        order = com.neo.parkguidance.web.infra.table.SortOrder.ASCENDING;
                        break;
                    case DESCENDING:
                        order =  com.neo.parkguidance.web.infra.table.SortOrder.DESCENDING;
                        break;
                    }
                }
                model.getFilter().setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<DataSheet> list = dataSheetService.paginate(model.getFilter());
                setRowCount((int) dataSheetService.count(model.getFilter()));

                return list;
            }

            @Override
            public DataSheet getRowData(String key) {
                return dataSheetService.findById(Integer.valueOf(key));
            }
        });
    }

    public Filter<DataSheet> newFilter() {
        return new Filter<>(new DataSheet());
    }

    public DataSheet findById(int id) {
        return dataSheetService.findById(id);
    }

    public int delete(List<DataSheet> list) {
        int numCars = 0;

        if(list != null) {
            for (DataSheet selectedCar : list) {
                numCars++;
                dataSheetService.remove(selectedCar);

            }

        }
        return numCars;
    }

    public boolean sorterOffline() {
        return !pingHost(System.getProperty("parkguidance.endpoint.sorter"));
    }

    public void sortData() {
        try {
            URL url = new URL(System.getProperty("parkguidance.endpoint.sorter"));
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();

            if (http.getResponseCode() != HttpServletResponse.SC_ACCEPTED) {
                throw new IOException(http.getResponseCode() + " " + http.getResponseMessage());
            }
        } catch (Exception e) {
            Messages.addError(null, "Something went wrong while sorting" + e.getMessage());
        }
    }
}
