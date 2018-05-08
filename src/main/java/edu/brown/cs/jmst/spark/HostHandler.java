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
import edu.brown.cs.jmst.spotify.SpotifyAuthentication;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class HostHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    SmuState state = SmuState.getInstance();
    String userid = req.session().attribute("user");
    User u = state.getUser(userid);
    if (u == null || !u.loggedIn()) {
      res.redirect(SpotifyAuthentication.getRootUri() + "/");
    } else {
    	  SparkErrorEnum err = null;  
    	  if(u.inParty()) {
    		  
    	      QueryParamsMap qm = req.queryMap();
    	      Party p = state.getParty(u.getCurrentParty());
    	      if(qm.hasKey("party_id") && p.getHostId().equals(userid)) {
    	          Map<String,
                Object> variables = new ImmutableMap.Builder<String, Object>()
                    .put("party_id", p.getId()).put("hostname", p.getHostName())
                    .put("user_id", u.getId()).build();
            return new ModelAndView(variables, "songmeup/host/host_join.ftl");
    	      } else {
    	    	  	res.redirect(u.getCurrentPartyUrl(p));
//    	    	  err = SparkErrorEnum.INVALID_PARTY_ID;
//    	            List<BasicNameValuePair> pair = new ArrayList<>();
//    	            pair.add(new BasicNameValuePair("error", err.toString()));
//    	            res.redirect(SpotifyAuthentication.getRootUri() + "/error?"
//    	                + URLEncodedUtils.format(pair, "UTF-8"));
//    	    	    res.redirect(u.getCurrentPartyUrl(p));
    	      }
    	  } else {
    		  err = SparkErrorEnum.NOT_IN_PARTY;
            List<BasicNameValuePair> pair = new ArrayList<>();
            pair.add(new BasicNameValuePair("error", err.toString()));
            res.redirect(SpotifyAuthentication.getRootUri() + "/error?"
                + URLEncodedUtils.format(pair, "UTF-8"));
    		  //res.redirect(SpotifyAuthentication.getRootUri() + "/main");
    	  }
    }
    return null;
  }
}
