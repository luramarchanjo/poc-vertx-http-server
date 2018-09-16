package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;

public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static void main(String... arguments) {
    logger.info("Starting application");
    Vertx vertx = Vertx.vertx();

    Router mainRouter = Router.router(vertx);
    mainRouter.route().handler(BodyHandler.create()); // Enable BodyHandler
    mainRouter.route().handler(CookieHandler.create()); // Enable CookieHandler

    // Global request handler
    mainRouter.route().handler(routingContext -> {
      HttpServerRequest httpServerRequest = routingContext.request();
      HttpServerResponse httpServerResponse = httpServerRequest.response();
      httpServerResponse.putHeader("content-type", "application/json");
      logger.info(String.format("Receveid request %s - %s", httpServerRequest.method(),
        httpServerRequest.uri()));
      routingContext.next();
    });

    // Product resources (GET, POST, PUT, DELETE)
    ProductRouter.builder().mainRouter(mainRouter).build().createResources();

    // HttpServer
    vertx.createHttpServer().requestHandler(mainRouter::accept).listen(8080);
  }

}
