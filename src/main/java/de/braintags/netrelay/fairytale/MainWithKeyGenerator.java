package de.braintags.netrelay.fairytale;

import de.braintags.io.vertx.keygenerator.KeyGeneratorSettings;
import de.braintags.io.vertx.keygenerator.KeyGeneratorVerticle;
import de.braintags.netrelay.NetRelay;
import de.braintags.netrelay.init.Settings;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.docgen.Source;

/**
 * 
 * @author Michael Remme
 * 
 */

@Source(translate = false)
public class MainWithKeyGenerator extends AbstractVerticle {
  private static final io.vertx.core.logging.Logger LOGGER = io.vertx.core.logging.LoggerFactory
      .getLogger(MainWithKeyGenerator.class);
  private String settingsPath;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    String settingsFile = settingsPath + "fairytale-settings.json";
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put(Settings.SETTINGS_LOCATION_PROPERTY, settingsFile));

    vertx.deployVerticle(NetRelay.class.getName(), options, result -> {
      if (result.failed()) {
        LOGGER.error("", result.cause());
        startFuture.fail(result.cause());
      } else {
        LOGGER.info(NetRelay.class.getSimpleName() + " successfully launched: " + result.result());
        initKeyGeneratorVerticle(vertx, settingsPath, startFuture);
      }
    });
  }

  public void initKeyGeneratorVerticle(Vertx vertx, String settingsPath, Future<Void> startFuture) {
    DeploymentOptions options = new DeploymentOptions();
    String settingsLocation = settingsPath + "KeyGeneratorSettings.json";
    LOGGER.info("Settings for KeyGenerator: " + settingsLocation);
    options.setConfig(new JsonObject().put(KeyGeneratorSettings.SETTINGS_LOCATION_PROPERTY, settingsLocation));
    vertx.deployVerticle(KeyGeneratorVerticle.class.getName(), options, result -> {
      if (result.failed()) {
        startFuture.fail(result.cause());
      } else {
        LOGGER.info(KeyGeneratorVerticle.class.getSimpleName() + " successfully launched: " + result.result());
        startFuture.complete();
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.core.AbstractVerticle#init(io.vertx.core.Vertx, io.vertx.core.Context)
   */
  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    settingsPath = "src/main/resources/";
  }

}
