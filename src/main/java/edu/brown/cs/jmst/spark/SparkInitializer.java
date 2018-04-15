package edu.brown.cs.jmst.spark;

import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class SparkInitializer {

  public static void setHandlers(FreeMarkerEngine freeMarker) {
    Spark.get("/login", new SigninPage(), freeMarker);
  }

}
