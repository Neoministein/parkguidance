package com.neo.parkguidance.web.admin.pages.sheetlist;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.infra.entity.LazyEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.util.Messages;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.neo.parkguidance.web.utils.Utils.pingHost;

@Stateless
public class DataSheetListFacade {

    @Inject
    AbstractEntityDao<DataSheet> dataSheetDao;

    public LazyEntityService<DataSheet> initDataModel(Filter<DataSheet> filter) {
        return new LazyEntityService<>(dataSheetDao, filter);
    }

    public Filter<DataSheet> newFilter() {
        return new Filter<>(new DataSheet());
    }

    public DataSheet findById(int id) {
        return dataSheetDao.find(Long.valueOf(id));
    }

    public int delete(List<DataSheet> list) {
        int numCars = 0;

        if(list != null) {
            for (DataSheet selectedCar : list) {
                numCars++;
                dataSheetDao.remove(selectedCar);

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

            if (http.getResponseCode() != HttpServletResponse.SC_OK) {
                throw new IOException(http.getResponseCode() + " " + http.getResponseMessage());
            }
        } catch (Exception e) {
            Messages.addError(null, "Something went wrong while sorting " + e.getMessage());
        }
    }
}
