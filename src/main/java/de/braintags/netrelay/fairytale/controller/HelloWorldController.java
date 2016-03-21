package de.braintags.netrelay.fairytale.controller;

import java.util.Properties;

import de.braintags.netrelay.controller.impl.AbstractController;
import io.vertx.ext.web.RoutingContext;

/**
 * 
 * 
 * @author Michael Remme
 * 
 */
public class HelloWorldController extends AbstractController {
  private static final io.vertx.core.logging.Logger LOGGER = io.vertx.core.logging.LoggerFactory
      .getLogger(HelloWorldController.class);

  public static final String HELLO_PROPNAME = "helloProperty";

  private String propertyName;

  /**
   * 
   */
  public HelloWorldController() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.core.Handler#handle(java.lang.Object)
   */
  @Override
  public void handle(RoutingContext context) {
    LOGGER.info("addin to context under " + propertyName);
    context.put(propertyName, "Hello world");
    context.next();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.braintags.netrelay.controller.impl.AbstractController#initProperties(java.util.Properties)
   */
  @Override
  public void initProperties(Properties properties) {
    LOGGER.info("init");
    propertyName = readProperty(HELLO_PROPNAME, null, true);
  }

}
