/**
 * :numbered:
 * :toc: left
 * :toclevels: 3
 * 
 * = NetRelay^(R)^ demo project
 * 
 * This tutorial describes the creation of a web application based on NetRelay. The project assumes, that you have a
 * MongoDb running.
 * 
 * == Creating the pom.xml
 * Add the parent information as reference to btVertxBasePOM, which will add all needed base information
 * 
 * [source,xml,subs="+attributes"]
 * ----
 * <parent>
 *  <groupId>de.braintags</groupId>
 *  <artifactId>btVertxBasePOM</artifactId>
 *  <version>15-SNAPSHOT</version>
 * </parent>
 * ----
 *
 * add properties into the properties section:
 * [source,xml,subs="+attributes"]
 * ----
 * <netrelay.version>10.0.0-SNAPSHOT</netrelay.version>
 * <netrelayController.version>1.0.0-SNAPSHOT</netrelayController.version>
 * ----
 *
 * add dependencies for the core project NetRelay and for the NetRelay-Controller:
 * 
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *  <groupId>de.braintags</groupId>
 *  <artifactId>netrelay</artifactId>
 *  <version>${netrelay.version}</version>
 * </dependency>
 * 
 * <dependency>
 *  <groupId>de.braintags</groupId>
 *  <artifactId>NetRelayController</artifactId>
 *  <version>${netrelayController.version}</version>
 * </dependency>
 * ----
 *
 * == Create the main verticle
 * Create a new class MainVerticle, which extends {@link io.vertx.core.AbstractVerticle}. This will be the verticle,
 * which is used to start the application and to init two other verticles:
 * 
 * * NetRelay
 * * Key-Generator-Verticle, which is used to generate IDs for new records
 * 
 * The first we define is the location, where our application will check for configuration files. If this is not set,
 * then the application will automatically use a subdirectory of the user directory. +
 * Add the method `init(io.vertx.core.Vertx, io.vertx.core.Context)` with:
 * 
 * [source, java]
 * ----
 * {@link de.braintags.netrelay.fairytale.MainWithKeyGenerator#init(io.vertx.core.Vertx, io.vertx.core.Context) }
 * ----
 * 
 * Next add the method `de.braintags.netrelay.fairytale.MainWithKeyGenerator#start(io.vertx.core.Future)` +
 * This method launches NetRelay and calls a method, which launches the Key-Generator-Verticle. NetRelay needs a
 * configuration file, which we are defining to be found inside the formerly defined settings directory.
 * 
 * [source, java]
 * ----
 * {@link de.braintags.netrelay.fairytale.MainWithKeyGenerator#start(io.vertx.core.Future)}
 * ----
 * 
 * Next, add the method `initKeyGeneratorVerticle(io.vertx.core.Vertx, String, io.vertx.core.Future)`, which initializes
 * the Key-Generator-Verticle. Inside the init process it needs a configuration file, which we define to be found inside
 * the formerly defined settings location.
 * 
 * [source, java]
 * ----
 * {@link de.braintags.netrelay.fairytale.MainWithKeyGenerator#initKeyGeneratorVerticle(io.vertx.core.Vertx, String, io.vertx.core.Future) }
 * ----
 * 
 * == Creating the settings files
 * The settings files, which are needed for our two verticles, will be created automatically if they are not found at
 * the expected location at startup by using some default values. With the creation of the Main Verticle above we are
 * ready to launch the application for the first time. ( See <<launch>> )
 * 
 * When you launch the application, it will finish directly after displaying an exception inside the console:
 * 
 * ----
 * de.braintags.io.vertx.util.exception.InitException:
 *    Settings file did not exist and was created new in path src/main/resources/fairytale-settings.json.
 *    NOTE: edit the file, set edited to true and restart the server
 *      at de.braintags.netrelay.init.Settings.loadSettings(Settings.java:182)
 *      ...
 * ----
 * 
 * Open the settings file and change the property "edited" to "true". Additionally edit the section
 * "datastoreSettings" to your needs by adding the suitable Mongo location. In our example we are running MongoDb local
 * and use the database "fairytale".
 * 
 * [source, json]
 * ----
 * "datastoreSettings" : {
 * "datastoreInit" : "de.braintags.io.vertx.pojomapper.mongo.init.MongoDataStoreInit",
 *   "properties" : {
 *     "startMongoLocal" : "false",
 *     "handleReferencedRecursive" : "true",
 *     "connection_string" : "mongodb://localhost:27017",
 *     "shared" : "false"
 *   },
 *   "databaseName" : "fairytale"
 * }
 * ----
 * 
 * Now, launch the application again, again it will finish with an error, now referencing the config file for the key
 * generator verticle, which will be used to generate record identifyers:
 * 
 * ----
 * de.braintags.io.vertx.util.exception.InitException:
 *   Settings file did not exist and was created new in path src/main/resources/KeyGeneratorSettings.json.
 *   NOTE: edit the file, set edited to true and restart the server
 *     at de.braintags.io.vertx.keygenerator.KeyGeneratorSettings.loadSettings(KeyGeneratorSettings.java:103)
 * 
 * ----
 * 
 * Open the specified file, set the property "edited" to true and modify the connection string to the position of your
 * mongo db. If you want to know more details about the key generator go to the project
 * link:https://github.com/BraintagsGmbH/vertx-key-generator[ vertx-key-generator]
 * 
 * [source, json]
 * ----
 * {
 *   "edited" : true,
 *   "keyGeneratorClass" : "de.braintags.io.vertx.keygenerator.impl.MongoKeyGenerator",
 *   "generatorProperties" : {
 *     "db_name" : "KeygeneratoDb",
 *     "startMongoLocal" : "false",
 *     "connection_string" : "mongodb://localhost:27017",
 *     "shared" : "false"
 *   }
 * }
 * ----
 * 
 * == Adding a controller to the settings
 * Since our project shall process dynamic pages, we will add the ThymeleafTemplateController from the project
 * NetRelay-Controllers into the netrelay settings. Please make sure that you added the suitable dependency into the
 * build file of your project like described above.
 * Open the NetRelay-settings, which was the file "fairytale-settings.json" in our example above. The first part of the
 * file are server specific properties like the port for instance. The second part define the datastore to be used,
 * which we were editing before already. The next part are the routerdefinitions, where it is specified which
 * Controllers are used by our application and which controller is activated on which routes. This is the part, we are
 * interested now in. Locate the end of the block "routerdefinitions", which should be a definition with the name
 * "FailureDefinition".
 * Add a new entry behind this definition with the following content:
 * 
 * [source, json]
 * ----
 * 
 * , {
 *   "name" : "ThymeleafTemplateController",
 *   "routes" : [ "/*" ],
 *   "blocking" : false,
 *   "failureDefinition" : false,
 *   "controller" : "de.braintags.netrelay.controller.ThymeleafTemplateController",
 *   "httpMethod" : null,
 *   "handlerProperties" : {
 *     "templateDirectory" : "templates",
 *     "mode" : "XHTML",
 *     "contentType" : "text/html",
 *     "cacheEnabled" : "false"
 *   },
 *   "captureCollection" : null
 * }
 * ----
 * 
 * By adding this definition you are activating Thymeleaf as template engine. At the moment we are activating it on any
 * path, which is called. It is important to add the controller at the end of the definition list, because the
 * controllers are checked and executed in the order of this list and normally the TemplateController depends on the
 * result of some previously executed other controllers.
 * 
 * 
 * == Launching the application
 * Create a directory "templates" inside your project and inside the directory create a file "index.html" with the
 * content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 * 
 * <body>
 * my index page
 * </body>
 * </html>
 * 
 * ----
 * (Re)Launch the application and open link:http://localhost:8080[ localhost:8080] in a browser, which should show you
 * the expected result.
 * 
 * == Adding static contents
 * Before we are going to implement dynamic templates, we will take care of static resources, which we will need in the
 * later run. Add a new directory "webroot" into your project. Download the latest version of bootstrap from
 * link:http://getbootstrap.com/getting-started/#download[ the bootstrap download site ], extract it into the webroot
 * directory and rename the new subdirectory to "bootstrap". If you like, search or use a "favicon.ico" from the net and
 * store it into webroot.
 * 
 * NOTE: At this point we are preparing some contents, which are used by the controllers StaticController and
 * FaviconController, which are both defined by default inside the configuration of NetRelay. The bootstrap will be used
 * in coming templates to simplify our styling live.
 * 
 * == Handling persistence
 * creating a mapper and templates for free editing etc.
 * 
 * === add persistence controller
 * 
 * === insert a record
 * 
 * === list records
 * 
 * === delete records
 * 
 * == Adding protected areas
 * Lets say, that inside the project exists an area, where a user can edit his own data, like his name, password etc.
 * 
 * === Adding a mapper to the configuration
 * User information shall be stored into our MongoDb. To be able to work with records from a datastore, we have to
 * make the pojo mapper known for NetRelay.
 * In our example we are using the mapper class de.braintags.netrelay.model.Member from the project NetRelay-Connectors.
 * Open the settings file of NetRelay again and location the section "mapperDefinitions", which you should find at the
 * bottom of the document. Inside the part "mapperMap" add the entry `"Member" : "de.braintags.netrelay.model.Member"`.
 * Afterwards this part should look like that:
 * 
 * [source, json]
 * ----
 * "mappingDefinitions" : {
 *   "mapperMap" : {
 *     "Member" : "de.braintags.netrelay.model.Member"
 *   }
 * }
 * 
 * ----
 * 
 * === Creating the protected area
 * A user will be able to edit his data under the path /member/memberData.html. Therefor create a new directory and
 * file in "templates/member/memberData.html".
 * 
 * === Creating the login template
 * If a user, who is not logged in, wants to enter a restricted area, he will be redirected to a page, where he can
 * login or create an account inside the server.
 * Create a new file "login.html" inside the directory templates and add the following code:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * 
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 * <head>
 *   <title>Login page</title>
 *   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 * </head>
 * <body class="general">
 *   <div class="jumbotron">
 *     <div class="container">
 * <h3>Login</h3>
 *     </div>
 *   </div>
 *   <div id="columns">
 *     <div class="container">
 *       <div class="row">
 *         <form action="/member/login" method="POST">
 *           <div class="form-group">
 *             <label for="username">username</label>
 *             <input type="text" class="form-control" id="username" name="username"
 *               placeholder="username" required="required" />
 *           </div>
 *           <div class="form-group">
 *             <label for="password">password</label> <input type="password"
 *               class="form-control" id="password" name="password" required="required" />
 *           </div>
 *           <button type="submit" class="btn btn-default">login</button>
 *         </form>
 *       </div>
 *     </div>
 *   </div>
 * </body>
 * </html>
 * ----
 * 
 * This template creates a login form with the two fields username and password, which will call the url "/member/login"
 * when sent. Behind this url we will soon implement the check for an existing user as authentication.
 * 
 * === Adding the AuthenticationController
 * 
 * 
 * {
 *   "name" : "AuthenticationController",
 *   "routes" : [
 *     "/backend/dashboard/*"
 *   ],
 *   "blocking" : false,
 *   "failureDefinition" : false,
 *   "controller" : "de.braintags.netrelay.controller.authentication.AuthenticationController",
 *   "httpMethod" : null,
 *   "handlerProperties" : {
 *     "loginPage" : "/backend/login.html",
 *     "logoutAction" : "/member/logout",
 *     "logoutDestinationPage": "/backend/login.html",
 *     "directLoggedInOKURL": "/backend/dashboard/",
 *     "roleField" : "roles",
 *     "collectionName" : "Member",
 *     "loginAction" : "/member/login",
 *     "passwordField" : "password",
 *     "usernameField" : "userName",
 *     "authProvider" : "MongoAuth"
 *   },
 *   "captureCollection" : null
 * }
 * 
 * 
 * === Adding the RegisterController
 * 
 * 
 * 
 * 
 * == Adding persistence
 * 
 * == Rendering pdf
 * 
 * 
 * == Creating an own controller
 * 
 * 
 * 
 * [#launch]
 * == Launching the application with eclipse
 * If you want to launch the application from out of eclipse directly:
 * 
 * * Create a new run configuration as Java-application ( Run -> Run-Configurations )
 * * Choose the correct project
 * * Define the main class to be io.vertx.core.Starter
 * * Open tab "Arguments" and add "run de.braintags.netrelay.fairytale.Main" to the Program arguments, where the class
 * should point to the previously created main verticle.
 * 
 * 
 * == Further links
 * For basic information about NetRelay go to the https://github.com/BraintagsGmbH/NetRelay[ NetRelay documentation ]
 * * NetRelay
 * * NetRelay-Controller
 * 
 */

@Document(fileName = "index.adoc")
package de.braintags.netrelay.fairytale;

import io.vertx.docgen.Document;