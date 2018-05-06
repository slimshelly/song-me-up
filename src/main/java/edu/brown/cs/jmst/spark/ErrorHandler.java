package edu.brown.cs.jmst.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jmst.party.Party;
import edu.brown.cs.jmst.party.User;
import edu.brown.cs.jmst.songmeup.SmuState;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class ErrorHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);

    QueryParamsMap qm = req.queryMap();
    String err = qm.value("error");
    String errInfo = SparkErrorEnum.errHelp(err);

    if (err.equals(SparkErrorEnum.ALREADY_IN_PARTY.toString())) {
      String pid = u.getCurrentParty();
      Party p = state.getParty(pid);
      String redirect_url;
      List<BasicNameValuePair> pair = new ArrayList<>();
      pair.add(new BasicNameValuePair("party_id", p.getId()));
      String param = URLEncodedUtils.format(pair, "UTF-8");
      if (p.getHostId().equals(u.getId())) {
        redirect_url = "\"host?" + param + "\"";
      } else {
        redirect_url = "\"join?" + param + "\"";
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("errmsg", errInfo).put("redirect", redirect_url).build();
      return new ModelAndView(variables, "songmeup/error.ftl");
    } else {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("errmsg", errInfo).build();
      return new ModelAndView(variables, "songmeup/error.ftl");
    }

  }

}
