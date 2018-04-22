package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MainPage implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    QueryParamsMap qm = req.queryMap();
    if (qm.hasKey("loggedin")) {
      String loggedin = qm.value("loggedin");
      if (loggedin.equals("true") && qm.hasKey("access_token")
          && qm.hasKey("refresh_token")) {
        String access_token = qm.value("access_token");
        String refresh_token = qm.value("refresh_token");

        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("access_token", access_token));
        pairs.add(new BasicNameValuePair("refresh_token", refresh_token));
        pairs.add(new BasicNameValuePair("loggedin", "true"));

        Map<String,
            Object> variables = new ImmutableMap.Builder<String, Object>()
                .put("logchange", "SWITCH USER")
                .put("homelink", "<a id=\"home\" href=\"/main?"
                    + URLEncodedUtils.format(pairs, "UTF-8") + "\">HOME</a>")
                .build();
        return new ModelAndView(variables, "songmeup/main_page/index.ftl");
      }
    }
    Map<String,
        Object> variables = new ImmutableMap.Builder<String, Object>()
            .put("logchange", "LOG IN")
            .put("homelink", "<a id=\"home\" href=\"/main\">HOME</a>").build();
    return new ModelAndView(variables, "songmeup/main_page/index.ftl");
  }

}
