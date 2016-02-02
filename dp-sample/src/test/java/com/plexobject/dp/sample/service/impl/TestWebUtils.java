package com.plexobject.dp.sample.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.plexobject.dp.sample.domain.InfoResponse;
import com.plexobject.dp.sample.domain.QueryResponse;
import com.plexobject.encode.CodecType;
import com.plexobject.encode.ObjectCodecFactory;
import com.plexobject.http.ServiceInvocationException;

public class TestWebUtils {
    public static QueryResponse decodeQueryResponse(String jsonResp) {
        QueryResponse response = ObjectCodecFactory.getInstance()
                .getObjectCodec(CodecType.JSON)
                .decode(jsonResp, QueryResponse.class, null);
        return response;
    }

    public static InfoResponse decodeDataInfoResponse(String jsonResp) {
        InfoResponse response = ObjectCodecFactory.getInstance()
                .getObjectCodec(CodecType.JSON)
                .decode(jsonResp, InfoResponse.class, null);
        return response;
    }

    public static String httpGet(String target, String... headers)
            throws IOException {
        URL url = new URL(target);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if (headers.length == 0) {
            con.setRequestProperty("Content-Type", "application/json");
        }
        for (int i = 0; i < headers.length - 1; i += 2) {
            con.setRequestProperty(headers[i], headers[i + 1]);
        }
        return getResponse(con);
    }

    private static String getResponse(HttpURLConnection con) throws IOException {
        if (con.getResponseCode() != 200) {
            System.err.println("HTTP ERROR " + con.getResponseCode() + ": "
                    + con.getResponseMessage());
            throw new ServiceInvocationException("Status "
                    + con.getResponseCode() + ": " + con.getResponseMessage(),
                    con.getResponseCode());
        }
        String resp = toString(con.getInputStream());
        return resp;
    }

    private static String toString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
