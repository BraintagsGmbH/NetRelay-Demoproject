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
/**
 * :numbered:
 * :toc: left
 * :toclevels: 3
 * = NetRelay^(R)^ demo project
 * 
 * '''
 * 
 * NetRelay is a web application framework based on vert.x and vert.x-web. It is styled to allow a configurable,
 * transparent definition of routings inside a web application by using adjustable, reusable instances, the Controllers.
 * Additionaly NetRelay predefines the MVC-concept. +
 * This tutorial describes the creation of a web application based on NetRelay.
 * 
 * NOTE: The project assumes, that you have a MongoDb running, MySql will follow soon
 * 
 * == Quick-Quick start
 * 
 * 
 * If you find it useful: this project is ready to be run. Check it out and launch the class
 * de.braintags.netrelay.fairytale.MainWithKeyGenerator under vertx by adding the system properties: +
 * -DmailClientUserName=mailClientAccountName +
 * -DmailClientPassword=mailClientPassword +
 * -DmailClientHost=mailClientHost +
 * -DmailClientPort=mailClientPort +
 * 
 * === Launching inside an IDE
 * To launch the project directly from eclipse, just use the launch configuration "Start DemoProject.launch" +
 * For other IDEs: i'm sorry :-) Perhaps you can figure out and give me an update?
 * 
 * Launch the program and open link:localhost:8080[localhost:8080]
 * 
 * 
 * == Creating the pom.xml
 * Add the parent information as reference to btVertxBasePOM, which will add all needed base information
 * 
 * [source,xml,subs="+attributes"]
 * ----
 * <parent>
 *  <groupId>de.braintags</groupId>
 *  <artifactId>btVertxBasePOM</artifactId>
 *  <version>19-SNAPSHOT</version> ( check for the latest version )
 * </parent>
 * ----
 *
 * add properties into the properties section:
 * [source,xml,subs="+attributes"]
 * ----
 * <netrelay.version>10.2.0-SNAPSHOT</netrelay.version> ( check for the latest version )
 * <netrelayController.version>1.2.0-SNAPSHOT</netrelayController.version> ( check for the latest version )
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
 * then NetRelay will automatically use a subdirectory of the user directory. Here we want the settings to be used from
 * out of the project directory +
 * 
 * [source, java]
 * ----
 * private String settingsPath;
 * 
 * public void init(Vertx vertx, Context context) {
 * {@link de.braintags.netrelay.fairytale.MainWithKeyGenerator#init(io.vertx.core.Vertx, io.vertx.core.Context) }
 * }
 * ----
 * 
 * Next add the start method, which launches NetRelay and calls a method, which launches the Key-Generator-Verticle.
 * NetRelay needs a configuration file, which we are defining to be found inside the formerly defined settings
 * directory.
 * 
 * [source, java]
 * ----
 * public void start(Future<Void> startFuture) throws Exception {
 * {@link de.braintags.netrelay.fairytale.MainWithKeyGenerator#start(io.vertx.core.Future)}
 * }
 * ----
 * 
 * After this, add the method initKeyGeneratorVerticle, which initializes the Key-Generator-Verticle. This verticle
 * needs a configuration file as well, which we define to be found inside the formerly defined settings location.
 * 
 * [source, java]
 * ----
 * public void initKeyGeneratorVerticle(Vertx vertx, String settingsPath, Future<Void> startFuture) {
 * {@link de.braintags.netrelay.fairytale.MainWithKeyGenerator#initKeyGeneratorVerticle(io.vertx.core.Vertx, String, io.vertx.core.Future) }
 * }
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
 * de.braintags.vertx.util.exception.InitException:
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
 * "datastoreInit" : "de.braintags.vertx.jomnigate.mongo.init.MongoDataStoreInit",
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
 * de.braintags.vertx.util.exception.InitException:
 *   Settings file did not exist and was created new in path src/main/resources/KeyGeneratorSettings.json.
 *   NOTE: edit the file, set edited to true and restart the server
 *     at de.braintags.vertx.keygenerator.KeyGeneratorSettings.loadSettings(KeyGeneratorSettings.java:103)
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
 *   "keyGeneratorClass" : "de.braintags.vertx.keygenerator.impl.MongoKeyGenerator",
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
 * Controllers are the reusable, configurable entities which are building the logic of a NetRelay project.
 * Since our project shall process dynamic pages, we will add the ThymeleafTemplateController from the project
 * NetRelay-Controllers into the netrelay settings. Please make sure that you added the suitable dependency into the
 * build file of your project like described above. +
 * Open the NetRelay-settings, which is the file "fairytale-settings.json" in our example above. The first part of the
 * file are server specific properties like the port for instance. The second part defines the datastore to be used,
 * which we were editing before already. The next part are the router definitions, through them it is specified which
 * controllers are used by our application and which controller is activated on which routes. This is the part, we are
 * interested in, now. Locate the end of the block "routerdefinitions", which should be a definition with the name
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
 * ADD DOCUMENTATION FOR REGEX IN ROUTERDEFINITION LIKE "regex:.*foo" will fit for any request ending with "foo"
 * 
 * By adding this definition you are activating Thymeleaf as template engine. At the moment we are activating it on any
 * path, which is called. It is important to add the controller at the end of the definition list, because the
 * controllers are checked and executed in the order of this list and normally the TemplateController depends on the
 * result of some previously executed controllers.
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
 * directory and rename the new subdirectory to "bootstrap". If you like, search or use a "favicon.ico" from the net for
 * instance and store it into the directory "webroot".
 * 
 * NOTE: At this point we are preparing some contents, which are used by the controllers StaticController and
 * FaviconController, which are both defined by default inside the configuration of NetRelay. The bootstrap will be used
 * in coming templates to simplify our styling live.
 * 
 * == Handling persistence
 * In our example application we want to be able to create new fairytales, to list existing fairytales, to edit or
 * delete them. All those use cases are covered by the PersistenceController. +
 * The PersistenceController is the instance, which translates the parameters and data of a request into datastore
 * based actions. A request like "http://localhost/fairytale/detail?entity=fairytale(ID:5)" will be interpreted by the
 * controller to fetch the fairytale with the id 5 from the datastore and to store it inside the context, so that it can
 * be displayed by a template engine. +
 * The PersistenceController covers the most frequent use cases, so that the number of particular controllers can be
 * reduced to specialized implementations. On the other hand the PersistenceController shall not give the ability to
 * create uncontrollable datastore actions just by configuration, to force the creation of dedicated, well tested
 * controllers and to avoid unrecognized performance bottlenecks
 * 
 * === Defining a mapper
 * In our example we want to create an area, where we are able to create, edit and delete FairyTales. A FairyTale at
 * that time is a simple mapper, which contains the fields for a name, a description, a creation and modification
 * date. Therefore we are creating our mapper in the subpackage "model" as followed:
 * 
 * [ source, java ]
 * ----
 * {@link de.braintags.netrelay.fairytale.model.FairyTale}
 * ----
 * 
 * === Declaring the mapper
 * Creating the mapper is not enough, we must declare it so, that NetRelay gets known about it. Therefore open the
 * settings of NetRelay, locate the section "mappingDefinitions" and add the following entry into the mapperMap +
 * 
 * `"FairyTale" : "de.braintags.netrelay.fairytale.model.FairyTale"`
 * 
 * After that the declaration should look like
 * 
 * [source, json]
 * ----
 * "mappingDefinitions" : {
 *   "mapperMap" : {
 *     "FairyTale" : "de.braintags.netrelay.fairytale.model.FairyTale"
 *   }
 * }
 * ----
 * 
 * === Adding the setup for a PersistenceController
 * In the configuration of the PersistenceController we are defining the pages, where the controller is used and how it
 * shall interpret the request. With the routes, we are activating the controller for certain pages. With the
 * definitions in the section "captureCollection" we are defining the structure of the link and how it can be translated
 * into a database action. +
 * In our example in the first step we want to open the page "fairytales/index.html". There inside will be a form, by
 * which we will be able to create a new FairyTale. When pushing the submit button of the form, the new record shall be
 * written into the datastore and displayed by the page "/fairytales/detail.html". +
 * That means, that the action to insert a new record into the datastore is executed by the template
 * "/fairytales/detail.html" and because of that we are adding this page into the route definitions of the
 * PersistenceController. +
 * 
 * [source, json]
 * ----
 * {
 *   "name" : "PersistenceController",
 *   "routes" : [ "/fairytale/detail.html" ],
 *   "controller" : "de.braintags.netrelay.controller.persistence.PersistenceController",
 *   "handlerProperties" : {
 *     "mapperfactory" : "de.braintags.netrelay.mapping.NetRelayMapperFactory",
 *     "reroute" : "false",
 *     "cleanPath" : "true",
 *     "uploadDirectory" : "webroot/upload/",
 *     "uploadRelativePath" : "upload/"
 *   },
 *   "captureCollection" : [ {
 *     "captureDefinitions" : [ {
 *       "captureName" : "entity",
 *       "controllerKey" : "mapper",
 *       "required" : false
 *     }, {
 *       "captureName" : "action",
 *       "controllerKey" : "action",
 *       "required" : false
 *     } ]
 *   } ]
 * }
 * ----
 * 
 * When the form is sent, the request will be something like "/fairytale/detail.html?entity=FairyTale&action=INSERT",
 * which shall advice the PersistenceController to create a new instance of FairyTale, fill it with the contents from
 * the sent form, save it as new instance into the datastore and put it into the context, so that it is available for
 * our template processor etc. +
 * The part "captureCollection" will be explained more in detail below.
 * 
 * === Inserting records
 * After the preparation of the configuration its time to create the needed templates.
 * Create a directory "fairytales" in "templates" and add the file "index.html" with the following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 * <head>
 *   <title>fairytales</title>
 *   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 * </head>
 * <body>
 *   <div class="container">
 * <h3 class="overview">Insert new fairytale</h3>
 *       <form method="POST" action="detail.html?entity=FairyTale&amp;action=INSERT">
 *         <div class="form-group">
 *           <label for="name" class="control-label">name</label>
 *           <input type="text" name="FairyTale.name" class="form-control" id="name" placeholder="name" />
 *         </div>
 *         <div class="form-group">
 *           <button class="btn btn-primary pull-right" type="submit" name="SAVE">SAVE</button>
 *         </div>
 *       </form>
 *     </div>
 *   </body>
 * </html>
 * 
 * ----
 * This template creates a form, which calls the "detail.html" with the parameters like they are defined inside the
 * settings of the PersistenceController. Cause we want to create a new record, when sent, we define the action as
 * "INSERT".
 * As you can see in the input field, the name is defined as "FairyTale.name", which advices the PersistenceController
 * to store the value into the field name of the new Fairytale.
 * 
 * 
 * Next add the file "detail.html" into the same subdirectory with the content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
 *   th:with="fairytale=${context.get('FairyTale')}">
 * <head>
 *   <title>edit fairytale</title>
 *   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 * </head>
 * <body>
 *   <div class="container">
 * <h3 class="overview">Edit fairytale</h3>
 *     <form method="POST" th:action="'detail.html?entity=FairyTale(id:' + ${fairytale.id} + ')&amp;action=UPDATE'"">
 *       <div class="form-group">
 *         <label for="ft_id" class="control-label">ID</label>
 *         <input id="ft_id" class="form-control" readonly="readonly" name="FairyTale.id" th:value="${fairytale.id}" />
 *       </div>
 *       <div class="form-group">
 *         <label for="ft_id" class="control-label">last modified</label>
 *         <input id="ft_id" class="form-control" readonly="readonly" name="FairyTale.modifiedOn" th:value=
 *                "${fairytale.modifiedOn}" />
 *       </div>
 *       <div class="form-group">
 *         <label for="ft_name" class="control-label">Name</label>
 *           <input type="text" name="FairyTale.name" class="form-control" id="ft_name"
 *             th:value="${fairytale.name}" placeholder="name" />
 *       </div>
 *       <div class="form-group">
 *         <label for="ft_description" class="control-label">Description</label>
 *         <input type="text" name="FairyTale.description" class="form-control" id="ft_description" th:value=
 *           "${fairytale.description}" placeholder="description" />
 *       </div>
 *       <div class="form-group">
 *         <button class="btn btn-primary pull-right" type="submit" name="SAVE">SAVE</button>
 *       </div>
 *     </form>
 *   </div>
 * </body>
 * </html>
 * 
 * ----
 * 
 * This template creates a form, where the values of an existing FairyTale are displayed for editing. If the submit
 * button of the form is pressed, then the same page is called again with the action UPDATE, which will save the record.
 * Additionally the ID parameter is specified, so that the correct record is updated.
 * Launch the server now and call link:http://localhost:8080/fairytale/index.html
 * [http://localhost:8080/fairytale/index.html]. Enter a name in the form and push the submit button, which will lead
 * you to the page detail.html. Here you will now be able to edit the record and save it again.
 * 
 * === Creating a dynamic record list
 * In the start page of the fairytales we want to add now a list of existing records with the ability to open a record
 * for editing. Open the template "/fairytale/index.html" again and add the following content at the bottom before the
 * body tag:
 * 
 * [source, html]
 * ----
 * 
 * <div class="container"> *   
 * <h2 class="overview">List of fairytales</h2>
 * <table class="table table-striped table-bordered" cellspacing="0" width="100%">
 * <tr th:each="ft : ${ context.get( 'FairyTale') }">
 * <td th:text="${ft.id}"></td>
 * <td th:text="${ft.name}"></td>
 * <td><a th:href="'detail.html?entity=FairyTale(id:' + ${ft.id} + ')&amp;action=DISPLAY'"">edit</a>
 *       </td>
 * </tr>
 * </table>
 * </div>
 * 
 * ----
 * This extension shall use an existing selection of FairyTale and will create one table row per record with a link to
 * the detail page, so that it can be edited. To get that work, we have to put the page "/fairytale/index.html" under
 * the control of the PersistenceController, so that the selection is created. After the route definitions should look
 * like:
 * 
 * [source, json]
 * ----
 * "name" : "PersistenceController",
 * "routes" : [ "/fairytale/index.html", "/fairytale/detail.html" ],
 * ----
 * 
 * The rest of the PersistenceController will stay unchanged. Relaunch the server and open the url:
 * 
 * link:http://localhost:8080/fairytale/index.html?entity=FairyTale[http://localhost:8080/fairytale/index.html?entity=
 * FairyTale]
 * 
 * This will open the index page and will display all records, which you created previously. Clicking on one entry will
 * open the selected record in the detail page for editing. From now on you will have to add the entity parameter on a
 * call to this page.
 * 
 * NOTE: In the configuration of the PersistenceController we defined two parameters inside the capture section:
 * action and entity. The definitions here are defining the possible parameters, the PersistenceController creates the
 * best fitting result in dependency to the real existing parameters in a request. +
 * If the action is not set, for instance, it will be interpreted as DISPLAY. If the ID is set as part of the entity,
 * then the one record with this ID is used. If it is not set and the action is DISPLAY, then all records from the
 * entity are fetched from the datastore.
 * 
 * === Deleting records
 * In the next step we will extend the record list by the ability to delete a selected record. Therefore a link will be
 * added, which - by clicking it - will delete the selected record and call the page "/fairytale/index.html" again.
 * Add the following code into the template "/fairytale/index.html" as new table cell directly behind the cell, which
 * contains the edit link:
 * 
 * [source, html]
 * ----
 * <td><a th:href="'delete?entity=FairyTale(id:' + ${ft.id} + ')&amp;action=DELETE'">delete</a>
 * </td>
 * ----
 * Open the page
 * link:http://localhost:8080/fairytale/index.html?entity=FairyTale[http://localhost:8080/fairytale/index.html?entity=
 * FairyTale] again, in the record list you will find another link called "delete" +
 * If you click to one delete link, an error will occur, telling, that the template "delete" does not exist.
 * Additionally the choosen record is not deleted. To enable the full functionality, we have to extend the
 * configuration.
 * 
 * First we will add the path "fairytale/delete" to the routelist of the PersistenceController:
 * 
 * [source, json]
 * ----
 * "name" : "PersistenceController",
 * "routes" : [ "/fairytale/index.html", "/fairytale/detail.html", "/fairytale/delete" ],
 * ----
 * 
 * Second we are adding a new Controller - it is important to add it after the PersistenceController:
 * 
 * [source, json]
 * ----
 * {
 * "name" : "RedirectController",
 * "routes" : [ "/fairytale/delete" ],
 * "controller" : "de.braintags.netrelay.controller.RedirectController",
 * "handlerProperties" : {
 * "destination" : "/fairytale/index.html?entity=FairyTale",
 * "reusePathParameters": false
 * }
 * ----
 * 
 * With the RedirectController we are simply defining, that we want to redirect from the page "/fairytale/delete" to the
 * page "/fairytale/index.html?entity=FairyTale" and that we don't want to append the parameters of the current request.
 * Restart the server now and call
 * link:http://localhost:8080/fairytale/index.html?entity=FairyTale[http://localhost:8080/fairytale/index.html?entity=
 * FairyTale] again. In the record list click to one delete entry. You will land on the same page - the list will be
 * reduced by the chosen record. In this scenario we use the virtual page "fairytale/delete", wherefor no template
 * exists. The only sense of this page is to execute the persistence action "delete" and to redirect the user back to
 * the original page. That way we are preventing errors, which could occur in case the user would perform a refresh.
 * 
 * 
 * == Activating mail settings
 * One part of the NetRelay settings is the section mailClientSettings. You can define all values here inside.
 * Additionally, if you don't want to add information about username, userpassword and host into the settings,
 * then it is possible to define some System properties:
 * 
 * * {@link de.braintags.netrelay.init.MailClientSettings#USERNAME_SYS_PROPERTY} property name "mailClientUserName"
 * * {@link de.braintags.netrelay.init.MailClientSettings#PASSWORD_SYS_PROPERTY} property name "mailClientPassword"
 * * {@link de.braintags.netrelay.init.MailClientSettings#HOST_SYS_PROPERTY} property name "mailClientHost"
 * * {@link de.braintags.netrelay.init.MailClientSettings#PORT_SYS_PROPERTY} property name "mailClientPort"
 * 
 * 
 * 
 * == Adding protected areas
 * Lets say, that inside the project exists an area, where a user can edit his own data, like his name, password etc.
 * Thus we need the typical possibilities of member registration ( with double opt in ), login, password forgotten which
 * we will implement now.
 * 
 * === Adding required mappers to the configuration
 * User information shall be stored into our MongoDb. To be able to work with records from a datastore, we have to
 * make the pojo mapper known for NetRelay. +
 * In our example we are using the mapper class de.braintags.netrelay.model.Member from the project NetRelay-Controller.
 * Open the settings file of NetRelay again and locate the section "mapperDefinitions", which you should find at the
 * bottom of the document. Inside the part "mapperMap" add the entry `"Member" : "de.braintags.netrelay.model.Member"`.
 * Afterwards this part should look like that:
 * 
 * [source, json]
 * ----
 * "mappingDefinitions" : {
 *   "mapperMap" : {
 *     "FairyTale" : "de.braintags.netrelay.fairytale.model.FairyTale",
 *     "Member" : "de.braintags.netrelay.model.Member"
 *   }
 * }
 * 
 * ----
 * With this entry you are simply defining, that there exists a mapper with the reference name "Member", which is
 * pointing to the defined class. The mapper will be initialized by NetRelay and inside the underlying datastore, when
 * it is needed.
 * 
 * 
 * 
 * === Adding the RegisterController
 * Before we are able to login into the restricted area, we must take care that there are existing valid user data
 * inside
 * the system, which we can use for authentication. We could program that complete by defining the templates and the
 * handlers to put down as member and process the double-opt-in. Or we are using the
 * {@link de.braintags.netrelay.controller.authentication.RegisterController}, which is built to structure this
 * process in a reusable way.
 * 
 * ==== Creating the registration page
 * The registration page will have two tasks. First it can be opened by a user, who will fill in his user data and send
 * the form to create an account inside the system. If during this step an error occurred, the same page will be called
 * again and the error is displayed of top of the form. +
 * Create the file "registration.html" in directory "templates" and paste the following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 * <head>
 *   <title>new registration</title>
 *   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 * </head>
 * <body class="registration">
 *   <div class="jumbotron">
 *     <div class="container">
 *       
 * <h3>Please enter registration data</h3>
 *     </div>
 *   </div>
 *   <div class="container">
 *     <div th:if="${context.get('registerError') != null}" >
 *       <div class="alert alert-danger" th:text="${context.get( 'registerError')}">
 *     </div>
 *   </div>
 *   <form id="regForm" name="regForm" class="validation regForm" action="/doRegister">
 *     <div class="form-group">
 *       <label for="firstName" class="control-label">first name</label>
 *         <input type="text" name="Member.firstName" id="firstName" placeholder="first name" />
 *     </div>
 *     <div class="form-group">
 *       <label for="lastName" class="control-label">last name</label>
 *         <input type="text" name="Member.lastName" id="lastName" placeholder="last name" />
 *     </div>
 *     <div class="form-group">
 *       <label for="email" class="control-label">E-Mail*</label>
 *         <input type="email" class="form-control" name="email" id="email" placeholder="E-Mail" />
 *     </div>
 *     <div class="form-group">
 *       <label for="newpassword" class="control-label">password*</label>
 *       <input type="password" name="password" class="form-control" id="password" placeholder="Passwort" />
 *     </div>
 *     <button type="submit" class="btn btn-default">register</button>
 *   </form>
 * </div>
 * </body>
 * </html>
 * 
 * ----
 * 
 * 
 * ==== Adding the configuration
 * 
 * Improve at that point, that the mailClientSettings are correct and point to an existing mail account.
 * Add the following configuration behind the SessionController:
 * 
 * [source, json]
 * ----
 * {
 *   "name" : "RegisterController",
 *   "routes" : [ "/doRegister","/verifyRegistration"],
 *   "controller" : "de.braintags.netrelay.controller.authentication.RegisterController",
 *   "handlerProperties" : {
 *     "regStartFailUrl" : "/registration.html",
 *     "regStartSuccessUrl" : "/registrationSuccess.html",
 *     "regConfirmSuccessUrl" : "/confirmRegSuccess.html",
 *     "regConfirmFailUrl" : "/confirmRegFailure.html",
 *     "authenticatableClass" : "de.braintags.netrelay.model.Member",
 *     "templateDirectory" : "templates",
 *     "template": "/mails/verifyEmail.html",
 *     "mode" : "XHTML",
 *     "from" : "registration@braintags.de",
 *     "subject": "fairytale registration: Email-confirmation"
 *   }
 * }
 * ----
 * 
 * Inside the configuration we are defining the property "authenticatableClass", which must be a Class, which
 * implements the interface {@link de.braintags.vertx.auth.datastore.IAuthenticatable} . Additionally the class, which
 * we are using here must be added into the mapper list like described above.
 * 
 * The RegisterController is processed in two phases: +
 * The first phase is executed, when a user fills out and sends the registration form. If this is successfull, the
 * registration data are stored temporary and a confirmation mail is sent to the email address of the user. +
 * The second phase is executed, when the user clicks to the confirmation link. Through this phase the user account are
 * finalized and saved into the datastore. +
 * For both phases must be declared a success and a fail url, which will be called when the appropriate action succeeded
 * or failed.
 * 
 * The routes, which are covered by the RegisterController, are the addresses of those two actions. The first action is
 * added as form action ( "/doRegister" ) inside the registration template and is called, when a user fills and sends
 * this form. The second ( /verifyRegistration ) is contained as link inside the confirmation mail and is called, when a
 * user clicks the confirmation link.
 * 
 * ==== The registration process
 * When a user fills out the registration form and clicks the send button, the first part of the registration can
 * succeed or can fail. The two properties "regStartFailUrl" and "regStartSuccessUrl" define the urls which are called
 * in those cases. In our example configuration we were setting the regStartFailUrl to the page "/registration.html" and
 * added the output of the error message to this template.
 * 
 * A simple example for a successful registration, which you should create as "registrationSuccess.html" inside the
 * templates directory, could look like that:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>registration success</title>
 *   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="registration">
 *     <div class="jumbotron">
 *       <div class="container">
 * <h3>successful registration</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       <div>registration succeeded - we sent a confirmation message per email
 *     </div>
 *     <div class="hidden">
 *       DEBUG: registerError = <span th:text="${context.get('registerError')}"></span><br/>
 *       mailSendResult = <span th:text="${context.get('mailSendResult')}"></span>
 *     </div>
 *     </div>
 *   </body>
 * </html>
 * ----
 * 
 * If you want to use a separate error page for this step, here a simple example for a successless registration, which
 * you should create as "registrationError.html" inside the templates directory and modify the configuration
 * accordingly:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>registration error</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="registration">
 *     <div class="jumbotron">
 *     <div class="container">
 * <h3>Error in registration</h3>
 *     </div>
 *     </div>
 *     <div class="container">
 *       <div th:if="${context.get('registerError') != null}" >
 *         <div class="alert alert-danger" th:text="${context.get( 'registerError')}">
 *       </div>
 *     </div>
 *   </div>
 *   </body>
 * </html>
 * 
 * ----
 * 
 * ==== The confirmation process
 * If the registration was successful, an email with the confirmation link is sent to the user. The content of the mail
 * is processed by a template, which is specified by the parameter "template" in the configuration - in our case we
 * defined the template as "/mails/verifyEmail.html". Create this file inside the template directory and add the
 * following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
 *   th:with=
 * "host = 'http://'+${context.get('REQUEST_HOST')+(context.get('REQUEST_PORT')?':'+context.get('REQUEST_PORT'):'')}">
 *   <head>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   </head>
 * <body>
 *   dear <span th:text="${context.request().params().get('Member.firstName')}" th:remove="tag"></span>
 *   <span th:text="${context.request().params().get('Member.lastName')}" th:remove="tag"></span>,
 *   the confirmation link:
 *     <a th:href="${host}+'/verifyRegistration?validationId='+${context.get('validationId')}"
 *       target="_blank">FINISH REGISTRATION</a>
 *   </body>
 * </html>
 * ----
 * If a user performs the registration ( and if the mail settings are correct in your settings ), a mail is processed,
 * which will contain a link, where the url points to the "verifyRegistration", which is covered by the routing of the
 * RegisterController. Here the properties "reqConfirmSuccessUrl" and "reqConfirmFailUrl" define the result pages, which
 * shall be called if the confirmation succeeeded or failed. Again two small examples:
 * 
 * Create file "confirmRegFailure.html" in directory "templates" with the content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>registration success</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="registration">
 *     <div class="jumbotron">
 *       <div class="container">
 * <h3>confirm error</h3>
 *       </div>
 *     </div>
 *     <div>confirmation: an error occured
 *       <span th:text="${context.get('registerError')}"></span>
 *     </div>
 *   </body>
 * </html>
 * 
 * ----
 * 
 * Create file "confirmRegSuccess.html" in directory "templates" with the content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 *   <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>registration success</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="registration">
 *     <div class="jumbotron">
 *       <div class="container">
 * <h3>confirmation succeeded</h3>
 *     </div>
 *     </div>
 *     <div>Your account is ready to be used now, you can login
 *     </div>
 *   </body>
 * </html>
 * ----
 * 
 * ==== Executing the registration
 * (Re)Launch the server and open the page
 * link:http://localhost:8080/registration.html[http://localhost:8080/registration.html]. Inside the registration form
 * add your data with a valid email address and push the button "register". As a result you will see the success page.
 * Additionally you should receive a confiormation mail, which includes the confirmation link. When clicking to the
 * link, the registration process is finalized and you should see the according page. +
 * Try now to perform the registration process again or remove the mail client settings and restart the server. In both
 * cases, after pushing the "register" button of the registration form, you should land on the same page, where an error
 * message is displayed.
 * 
 * === Creating the protected area
 * A user will be able to edit his data under the path /member/memberData.html. Therefore create a new directory and
 * file in "templates/member/memberData.html". Add some content to the file like:
 * 
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 *   <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>start page</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body>
 *     <div class="jumbotron">
 *       <div class="container">
 * <h3>Fairytales: Member area</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       coming: Member area
 *     </div>
 *   </body>
 * </html>
 * ----
 * 
 * === Creating the login template
 * If a user, who is not logged in, wants to enter a restricted area, he will be redirected to a page, where he can
 * login ( or create an account inside the server if you want to add this function later ).
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
 *         <form action="/login" method="POST">
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
 * This template creates a login form with the two fields username and password. When executed by a user, this form will
 * call the url "/member/login". Behind this url we will soon implement the check for an existing user as
 * authentication.
 * 
 * === Adding the AuthenticationController
 * All routes, which are covered by the AuthenticationController, are protected and require a valid login. The
 * AuthenticationController itself displays the login form with the page we created before, when it is required. The
 * configuration for our solution looks like that ( it is required to add this definition behind the SessionController
 * ):
 * 
 * [source, json]
 * ----
 * {
 *   "name" : "AuthenticationController",
 *   "routes" : [
 *     "/member/*"
 *   ],
 *   "controller" : "de.braintags.netrelay.controller.authentication.AuthenticationController",
 *   "handlerProperties" : {
 *     "loginPage" : "/login.html",
 *     "logoutAction" : "/logout",
 *     "logoutDestinationPage": "/index.html",
 *     "directLoggedInOKURL": "/index.html",
 *     "collectionName" : "Member",
 *     "loginAction" : "/login",
 *     "passwordField" : "password",
 *     "usernameField" : "email",
 *     "authProvider" : "MongoAuth"
 *   }
 * }
 * ----
 * As described above, all routes of the configuration are protected areas. So if you are starting the application now
 * and open the url link:http://localhost:8080/member/[member] you should see the login form inside the opened page
 * "login.html". If you enter now your userdata from the previously registered account, you should be able to enter the
 * protected page.
 * 
 * Although the AuthenticationController is quite complex and integrates several properties, the definition here is
 * quite simple to explain: +
 * If a user tries to enter a resticted area like "/member/memberData.html" and did not login before, then the login
 * form will be displayed, which is defined by the property "loginPage". The property "loginAction" defines the URL,
 * where the authentication - the check for a valid user - is executed. It is important, that the value of the form
 * action of the login-page and the value of this property are identic! +
 * The next, what we define is the way, how the authentication is processed. With the property "authProvider" we are
 * defining, that {@link io.vertx.ext.auth.mongo.MongoAuth} shall be used. Currently this is the only implemented
 * authprovider, others like JDBC etc. will follow. +
 * The property "collectionName" defines the collection or table to be used for authentication;
 * the properties usernameField and passwordField define the fields in the collection, which shall be used to search for
 * a suitable user for a username / password combination. +
 * The fields of the login form are currently always named "username" and "password"
 * 
 * 
 * === Adding the PasswordLostController
 * The PasswordLostController is very similar to the RegisterController and performs the process, if a user doesn't
 * remember his password. In the process, the user first calls a page, where he can enter the email address of his
 * account and activate the password lost process by submitting the form. The controller tries to find a valid account.
 * If successfull, it will compose a mail, where a temporary link is contained. If an error occured inside this phase of
 * the process, an error page is called. +
 * When the user clicks to the temporary link inside the mail, the second phase is started. First the controller will
 * verify, wether the reset link is still valid. If not, or another error occured, an error page is displayed. If the
 * link is still successfull, the success page is called, where the user can add a new password, for instance.
 * 
 * ==== Adding the configuration
 * 
 * [source, json]
 * ----
 * {
 *   "name" : "PasswordLostController",
 *   "routes" : [ "/passwordLost","/passwordReset"],
 *   "controller" : "de.braintags.netrelay.controller.authentication.PasswordLostController",
 *   "handlerProperties" : {
 *     "pwLostFailUrl" : "/passwordReset/passwordLost.html",
 *     "pwLostSuccessUrl" : "/passwordReset/confirmReset.html",
 *     "pwResetSuccessUrl" : "/passwordReset/verifyReset.html",
 *     "pwResetFailUrl" : "/passwordReset/failureReset.html",
 *     "authenticatableClass" : "de.braintags.netrelay.model.Member",
 *     "templateDirectory" : "templates",
 *     "template": "/mails/passwordLostEmail.html",
 *     "mode" : "XHTML",
 *     "from" : "registration@braintags.de",
 *     "subject": "password lost"
 *   }
 * }
 * 
 * ----
 * 
 * The defined routes "passwordLost" and "passwordReset" define the two main actions of the controller.
 * passwordLost searches a valid account and sends a reset mail. If successfull, it redirects to the template defined by
 * property "pwLostSuccessUrl". If not, it will redirect to "pwLostFailUrl". +
 * passwordReset validates the reset data behind the temporary link. If successfull, it will redirect to the template
 * defined by property "pwResetSuccessUrl". If not, it will redirect to pwResetFailUrl"
 * 
 * ==== Creating the start template
 * Create the directory "passwordReset" in templates and add the template "passwordLost.html" with the following
 * content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml"
 * xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>Login page</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="general">
 *     <div class="jumbotron">
 *       <div class="container">
 *         
 * <h3>password lost - please enter email address</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       <div class="row" th:if="${context.get('resetError')}">
 *         <div class="alert alert-danger" role="alert">
 *           Error resetting password: (<span
 *             th:text="${context.get('resetError')}"></span>)
 *         </div>
 *     </div>
 *     <form action="/passwordLost" name="passwordLostForm"
 *       id="passwordLostForm" method="POST" class="passwordLostForm">
 *       <div class="form-group">
 *         <label for="email">Email*</label> <input type="text"
 *           class="form-control" id="email" name="email" placeholder="Email"
 *           required="required" />
 *       </div>
 *       <button type="submit" class="btn btn-default">reset password</button>
 *     </form>
 *   </div>
 * </body>
 * </html>
 * 
 * ----
 * 
 * This template contains a form, where an email address must be entered. The form action calls the virtual page
 * "/passwordLost", which executes the first phase of the process. Because this template is defined to be the error
 * template of the first phase as well, an error view is added either.
 * 
 * ==== Creating the mail template
 * The PasswordLostController sends an email with the link, to confirm the process. Add the file
 * "passwordLostEmail.html" to the directory "mails" in templates with the following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
 * th:with=
 * "host = 'http://'+${context.get('REQUEST_HOST')+(context.get('REQUEST_PORT')?':'+context.get('REQUEST_PORT'):'')}">
 *   <head>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *   </head>
 *   <body th:with="m = ${context.get('authenticatable')}">
 *     dear <span th:text="${m.firstName}" th:remove="tag"></span>
 *     <span th:text="${m.lastName}" th:remove="tag"></span>, the password reset link:
 *       <a th:href="${host}+'/passwordReset?validationId='+${context.get('validationId')}"
 *       target="_blank">RESET NOW</a>
 *   </body>
 * </html>
 * 
 * ----
 * 
 * ==== Creating the confirmation template
 * When the reset process could be successfully started, the template is called, which is defined by the property
 * "pwLostSuccessUrl". Create the template "passwordReset/confirmReset.html" with the following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd"> *
 * <html xmlns="http://www.w3.org/1999/xhtml"
 * xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>registration success</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="registration">
 *     <div class="jumbotron">
 *       <div class="container">
 *         
 * <h3>successful password reset</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       <div>We sent a message to your email address, which contains a link to reset the password.
 *       </div>
 *       <div class="hidden">
 *         DEBUG: resetError = <span th:text="${context.get('resetError')}"></span><br/>
 *         mailSendResult = <span th:text="${context.get('mailSendResult')}"></span>
 *       </div>
 *     </div>
 *   </body>
 * </html>
 * ----
 * 
 * ==== Create the template to enter new password
 * When a user clicks the verification link in the email, the controller validates the data. If successfull, it calls
 * the template defined by the property "pwResetSuccessUrl". Therefor add "passwordReset/verifyReset.html" with the
 * following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd"> *
 * <html xmlns="http://www.w3.org/1999/xhtml"
 * xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>Login page</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 * 
 *   <body class="general" th:with="m= ${context.get('authenticatable')}">
 *     <div class="jumbotron">
 *       <div class="container">
 *         
 * <h3>enter new password</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       <form
 *         th:action="'/passwordReset/changePassword.html?entity=Member(id:' + ${m.id} + ')&amp;action=UPDATE'"
 *         name="passwordLostForm" id="passwordLostForm" method="POST"
 *         class="passwordLostForm">
 * 
 *       <div class="form-group" id="passwortstrength">
 *         <label for="newpassword" class="control-label">new password</label>
 * <span class="pwstrength_viewport_progress"><input
 *             type="password" name="Member.password" class="form-control"
 *             id="newpassword" placeholder="new password" /></span>
 *       </div>
 *       <button type="submit" class="btn btn-default">save password</button>
 *     </form>
 *   </div>
 *   </body>
 * </html>
 * 
 * ----
 * 
 * This template will give the user the ability to enter a new password. By submitting the form, the following template
 * "/passwordReset/changePassword.html" is called. Add this path to the routings of the PersistenceController now,
 * create the template with some simple content like "Your password was successfully changed".
 * 
 * 
 * === Creating the failure template
 * If an error occured, after the user clicked the verification link, the template is called, which is defined by the
 * property "pwResetFailUrl" of the configuration. Add this template now with the following content:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd"> *
 * <html xmlns="http://www.w3.org/1999/xhtml"
 * xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>registration error</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body class="registration">
 *     <div class="jumbotron">
 *       <div class="container">
 *         
 * <h3>Error in reset</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       <div th:if="${context.get('resetError') != null}" >
 *         <div class="alert alert-danger" th:text="${context.get( 'resetError')}">
 *         </div>
 *       </div>
 *     </div>
 *   </body>
 * </html>
 * 
 * ----
 * 
 * ==== Executing the password lost process
 * Open the page
 * link:http://localhost:8080/passwordReset/passwordLost.html[http://localhost:8080/passwordReset/passwordLost.html],
 * enter your email address and submit the form. You should receive an email with the verification link. Click this link
 * and follow the action.
 * 
 * 
 * == Creating an own controller
 * Controllers are handlers, which shall be executed for one or more route definitions and are configured as part of the
 * settings file. Creating a new controller is simply done by implementing
 * {@link de.braintags.netrelay.controller.IController} or by extending
 * {@link de.braintags.netrelay.controller.AbstractController}, for instance. +
 * In our example here we will create a new controller, which will add the text "Hello world" into the context, from
 * where it can be read from out of a template. The name of the variable in the context must set inside the
 * configuration in the settings. +
 * 
 * [source, java]
 * ----
 * {@link de.braintags.netrelay.fairytale.controller.HelloWorldController}
 * 
 * ----
 * 
 * During the intialization of NetRelay the init method of a controller is called, which is defined inside the
 * configuration. In our example we are reading the name, which shall be used to store the text "Hello world" into the
 * context. +
 * The method "handle" is called during runtime, when an url was called by the client, which shall activate the
 * controller. In our example we are simply adding the text "Hello world" into the context.
 * 
 * Next we are adding the configuration into the settings. Please ensure, that this configuration is placed before the
 * TemplateController. The corresponding configuration part from the settings looks like that:
 * 
 * [source, json]
 * ----
 * {
 *   "name" : "HelloWorldController",
 *   "controller" : "de.braintags.netrelay.fairytale.controller.HelloWorldController",
 *   "active" : true,
 *   "routes" : [ "/helloTemplate.html" ],
 *   "blocking" : false,
 *   "failureDefinition" : false,
 *   "handlerProperties" : {
 *     "helloProperty" : "HELLO"
 *   }
 * },
 * 
 * ----
 * 
 * Next, add the file "helloTemplate.html" into the template directory and add the following code:
 * 
 * [source, html]
 * ----
 * <!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
 * <html xmlns="http://www.w3.org/1999/xhtml"
 * xmlns:th="http://www.thymeleaf.org">
 *   <head>
 *     <title>hello</title>
 *     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 *     <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
 *   </head>
 *   <body >
 *     <div class="jumbotron">
 *       <div class="container">
 *         
 * <h3>Say it ...</h3>
 *       </div>
 *     </div>
 *     <div class="container">
 *       <div th:text="${context.get( 'HELLO')}">
 *       </div>
 *     </div>
 *   </body>
 * </html>
 * 
 * ----
 * 
 * Now, restart the server, call the link
 * link:http://localhost:8080/helloTemplate.html[http://localhost:8080/helloTemplate.html] - you will see the dexpected
 * result.
 * 
 * == The failure definition
 * TODO
 * 
 * == Authorization and permissions
 * TODO
 * 
 * == Linking subobjects
 * TODO
 * 
 * == Launching https
 * TODO
 * 
 * 
 * 
 * == More details about PersistenceController and Captures
 * The PersistenceController knows several possible keys, which can be used to describe an action as a capture
 * definition, like "entity", "ID", "action" and some others. One capture definition gives the information, which
 * parameter has to be mapped into which key. In our example we are defining, that "entity" is mapped to "mapper", which
 * is the part of a link, which defines the mapper, where the database action has to be executed. +
 * 
 * NOTE: just for the case that you are asking why this translation exists: we are able to execute links like
 * `/fairytale/detail.html?entity=FairyTale&action=INSERT&entity2=FairyTale&action2=UPDATE&ID2=15`
 * either. More about CaptureCollections you can read in the base documentation of NetRelay under
 * link:https://github.com/BraintagsGmbH/NetRelay[NetRelay].
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
