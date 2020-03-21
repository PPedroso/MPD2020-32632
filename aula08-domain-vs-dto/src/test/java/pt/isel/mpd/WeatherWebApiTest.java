/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.AbstractRequest;
import pt.isel.mpd.util.HttpRequest;
import pt.isel.mpd.weather.dto.WeatherInfo;
import pt.isel.mpd.weather.WeatherWebApi;

import java.io.InputStream;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.LazyQueries.*;

public class WeatherWebApiTest {
    static class RequestMediator extends AbstractRequest{
        private AbstractRequest req;
        int count;
        public RequestMediator(AbstractRequest req ) {
            this.req = req;
        }
        public InputStream getStream(String path) {
            count++;
            return req.getStream(path);
        }
    }
    @Test public void testPastWeather() {
        RequestMediator req = new RequestMediator(new HttpRequest());
        WeatherWebApi api = new WeatherWebApi(req);
        Iterable<WeatherInfo> jan = api.pastWeather(37.017, -7.933, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 30));
        assertEquals(0, req.count); // => Lazy
        Iterable<Integer> tempsJan = map(jan, wi -> wi.getTempC());
        assertEquals(0, req.count); // => Lazy
        int max = max(tempsJan);
        assertEquals(1, req.count); // Terminal Operation consumes pipeline
        assertEquals(17, max);
    }


    // TPC: Add unit test for search() of WeatherWebApi
}