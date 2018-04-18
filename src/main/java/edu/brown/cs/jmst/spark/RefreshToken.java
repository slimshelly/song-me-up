package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.jmst.general.General;
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class RefreshToken implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    General.printInfo("Refreshing...");
    QueryParamsMap qm = req.queryMap();
    String refresh_token = qm.value("refresh_token");
    General.printInfo("Refresh token: " + refresh_token);
    // JsonObject form = new JsonObject();
    // form.addProperty("grant_type", "authorization_code");
    // form.addProperty("refresh_token", refresh_token);
    //
    // JsonObject authOptions = new JsonObject();
    // authOptions.add("form", form);
    // authOptions.addProperty("json", true);

    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpPost post = new HttpPost("https://accounts.spotify.com/api/token");
      post.setHeader("Authorization",
          "Basic " + SpotifyAuthentication.ENCODED_CLIENT_KEY);
      General.printInfo("Authorization header: " + "Authorization: " + "Basic "
          + SpotifyAuthentication.ENCODED_CLIENT_KEY);

      List<BasicNameValuePair> pairs = new ArrayList<>();
      pairs.add(new BasicNameValuePair("refresh_token", refresh_token));
      pairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
      UrlEncodedFormEntity urlentity = new UrlEncodedFormEntity(pairs, "UTF-8");
      urlentity.setContentEncoding("application/json");
      post.setEntity(urlentity);

      HttpResponse response = client.execute(post);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json_string = EntityUtils.toString(response.getEntity());
        General.printInfo("Json string: " + json_string);
        JsonObject jo = new JsonParser().parse(json_string).getAsJsonObject();
        String access_token = jo.get("access_token").getAsString();
        General.printInfo("Access token: " + access_token);
        Map<String, Object> variables =
            new ImmutableMap.Builder<String, Object>()
                .put("access_token", access_token).build();
        return new ModelAndView(variables, "songmeup/logintest.ftl");
      }
    }
    return new ModelAndView(new ImmutableMap.Builder<String, Object>().build(),
        "songmeup/logintest.ftl");
  }

}
