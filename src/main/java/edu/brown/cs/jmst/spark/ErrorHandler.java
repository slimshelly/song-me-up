package edu.brown.cs.jmst.spark;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class ErrorHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    QueryParamsMap qm = req.queryMap();
    String err = qm.value("error");
    String errInfo = SparkErrorEnum.errHelp(err);
    Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
        .put("errmsg", errInfo).build();
    return new ModelAndView(variables, "songmeup/error.ftl");
  }

}
