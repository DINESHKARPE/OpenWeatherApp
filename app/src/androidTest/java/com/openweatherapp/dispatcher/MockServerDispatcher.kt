package com.openweatherapp.dispatcher

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher {

    class ResponseDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.path.equals(
                    "/data",
                    true
                )
            ) return MockResponse().setResponseCode(200).setBody("{\"city\":{\"id\":1254661,\"name\":\"Thāne\",\"coord\":{\"lon\":72.9667,\"lat\":19.2},\"country\":\"IN\",\"population\":1261517,\"timezone\":19800},\"cod\":\"200\",\"message\":4.1922544,\"cnt\":7,\"list\":[{\"dt\":1585981800,\"sunrise\":1585961955,\"sunset\":1586006551,\"temp\":{\"day\":34.07,\"min\":25.24,\"max\":34.07,\"night\":25.24,\"eve\":30.79,\"morn\":25.47},\"feels_like\":{\"day\":32.85,\"night\":28.31,\"eve\":30.08,\"morn\":27.77},\"pressure\":1012,\"humidity\":31,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"speed\":3.81,\"deg\":272,\"clouds\":51},{\"dt\":1586068200,\"sunrise\":1586048305,\"sunset\":1586092965,\"temp\":{\"day\":33.8,\"min\":23.72,\"max\":33.8,\"night\":24.73,\"eve\":29.65,\"morn\":23.72},\"feels_like\":{\"day\":33.22,\"night\":27.86,\"eve\":29.95,\"morn\":26.07},\"pressure\":1012,\"humidity\":31,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":2.78,\"deg\":261,\"clouds\":0},{\"dt\":1586154600,\"sunrise\":1586134655,\"sunset\":1586179380,\"temp\":{\"day\":31.64,\"min\":22.98,\"max\":31.64,\"night\":24.88,\"eve\":29.52,\"morn\":22.98},\"feels_like\":{\"day\":31.94,\"night\":28.01,\"eve\":29.83,\"morn\":25.85},\"pressure\":1013,\"humidity\":41,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"speed\":2.83,\"deg\":278,\"clouds\":89},{\"dt\":1586241000,\"sunrise\":1586221006,\"sunset\":1586265795,\"temp\":{\"day\":31.22,\"min\":23.63,\"max\":32.22,\"night\":24.3,\"eve\":29.46,\"morn\":23.63},\"feels_like\":{\"day\":30.56,\"night\":26.78,\"eve\":28.81,\"morn\":26.07},\"pressure\":1012,\"humidity\":39,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":3.56,\"deg\":269,\"clouds\":0},{\"dt\":1586327400,\"sunrise\":1586307357,\"sunset\":1586352210,\"temp\":{\"day\":30.61,\"min\":22.28,\"max\":31.91,\"night\":24.09,\"eve\":29.42,\"morn\":22.28},\"feels_like\":{\"day\":30.43,\"night\":27.41,\"eve\":29.24,\"morn\":24.89},\"pressure\":1013,\"humidity\":45,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":3.83,\"deg\":269,\"clouds\":0},{\"dt\":1586413800,\"sunrise\":1586393708,\"sunset\":1586438625,\"temp\":{\"day\":31.06,\"min\":22.77,\"max\":31.52,\"night\":23.82,\"eve\":29.39,\"morn\":22.77},\"feels_like\":{\"day\":30.26,\"night\":25.99,\"eve\":27.98,\"morn\":25.99},\"pressure\":1012,\"humidity\":37,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":3.26,\"deg\":276,\"clouds\":0},{\"dt\":1586500200,\"sunrise\":1586480060,\"sunset\":1586525041,\"temp\":{\"day\":32.94,\"min\":21.61,\"max\":33.47,\"night\":25.26,\"eve\":31.49,\"morn\":21.61},\"feels_like\":{\"day\":31.92,\"night\":27.13,\"eve\":30.8,\"morn\":22.95},\"pressure\":1012,\"humidity\":31,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":3.04,\"deg\":313,\"clouds\":0}]}")
            return MockResponse().setResponseCode(400)
        }
    }

    class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse = MockResponse().setResponseCode(400)
    }
}