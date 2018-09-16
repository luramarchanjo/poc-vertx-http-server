package app;

import app.domain.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRouter {

  private static final Logger logger = LoggerFactory.getLogger(ProductRouter.class);
  private final Map<String, Product> productMap = new ConcurrentHashMap<>();
  private final String resource = "/api/v1/products";
  private Router mainRouter;

  public void createResources() {
    createGetResource();
    createPostResource();
    createPutResource();
    createDeleteResource();
  }

  private void createGetResource() {
    mainRouter.get(resource).handler(routingContext -> {
      HttpServerResponse httpServerResponse = routingContext.response();

      try {
        String json = Application.OBJECT_MAPPER.writeValueAsString(productMap.values());
        httpServerResponse.putHeader("Content-Length", String.valueOf(json.length()));
        httpServerResponse.write(json);
      } catch (JsonProcessingException e) {
        logger.error("Error", e);
      } finally {
        httpServerResponse.end();
      }
    });
  }

  private void createPostResource() {
    mainRouter.post(resource).handler(routingContext -> {
      HttpServerResponse httpServerResponse = routingContext.response();

      try {
        JsonObject requestJson = routingContext.getBodyAsJson();
        Product product = Application.OBJECT_MAPPER
          .readValue(requestJson.toString(), Product.class);
        productMap.put(product.getId(), product);
      } catch (Exception e) {
        logger.error("Error", e);
      } finally {
        httpServerResponse.end();
      }
    });
  }

  private void createPutResource() {
    mainRouter.put(resource + "/:id").handler(routingContext -> {
      HttpServerRequest httpServerRequest = routingContext.request();
      HttpServerResponse httpServerResponse = routingContext.response();

      try {
        String productId = httpServerRequest.getParam("id");
        Product product = productMap.get(productId);
        if (Objects.isNull(product)) {
          httpServerResponse.setStatusCode(HttpResponseStatus.NOT_FOUND.code());
        } else {
          JsonObject requestJson = routingContext.getBodyAsJson();
          Product newProduct = Application.OBJECT_MAPPER
            .readValue(requestJson.toString(), Product.class);
          product.setName(newProduct.getName());
          productMap.put(productId, product);
        }
      } catch (Exception e) {
        logger.error("Error", e);
      } finally {
        httpServerResponse.end();
      }
    });
  }

  private void createDeleteResource() {
    mainRouter.delete(resource + "/:id").handler(routingContext -> {
      HttpServerRequest httpServerRequest = routingContext.request();
      HttpServerResponse httpServerResponse = routingContext.response();

      String productId = httpServerRequest.getParam("id");
      Product product = productMap.get(productId);
      if (Objects.isNull(product)) {
        httpServerResponse.setStatusCode(HttpResponseStatus.NOT_FOUND.code());
      } else {
        productMap.remove(productId);
      }

      httpServerResponse.end();
    });
  }

}
