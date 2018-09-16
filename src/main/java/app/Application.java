package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;

public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static void main(String... arguments) {
    logger.info("Starting application");
    Vertx vertx = Vertx.vertx();

    Router mainRouter = Router.router(vertx);

    // Global Handlers
    mainRouter.route().handler(BodyHandler.create());
    mainRouter.route().handler(CookieHandler.create());
    mainRouter.route().handler(LoggerHandler.create());
    mainRouter.route().handler(ResponseContentTypeHandler.create());

    // Product resources (GET, POST, PUT, DELETE)
    ProductRouter.builder().mainRouter(mainRouter).build().createResources();

    // HttpServer
    vertx.createHttpServer().requestHandler(mainRouter::accept).listen(8080);
  }

}
