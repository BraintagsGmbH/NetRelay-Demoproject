/*-
 * #%L
 * NetRelay-DemoProject
 * %%
 * Copyright (C) 2017 Braintags GmbH
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */
package de.braintags.netrelay.fairytale.controller;

import java.util.Properties;

import de.braintags.netrelay.controller.AbstractController;
import io.vertx.docgen.Source;
import io.vertx.ext.web.RoutingContext;

@Source(translate = false)
public class HelloWorldController extends AbstractController {
  private static final io.vertx.core.logging.Logger LOGGER = io.vertx.core.logging.LoggerFactory
      .getLogger(HelloWorldController.class);

  public static final String HELLO_PROPNAME = "helloProperty";
  private String propertyName;

  @Override
  public void handleController(RoutingContext context) {
    LOGGER.info("adding to context under " + propertyName);
    context.put(propertyName, "Hello world");
    context.next();
  }

  @Override
  public void initProperties(Properties properties) {
    LOGGER.info("init");
    propertyName = readProperty(HELLO_PROPNAME, null, true);
  }

}
