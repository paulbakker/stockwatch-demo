package codeone.stockwatch.web;

import codeone.stockwatch.api.StockWatch;
import codeone.stockwatch.api.StockWatchStore;
import codeone.stockwatch.cron.ScheduledWatcher;
import codeone.symbolfetch.StockPrice;
import codeone.symbolfetch.SymbolFetcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        Optional<SymbolFetcher> symbolFetcher = ServiceLoader.load(SymbolFetcher.class).findFirst();
        if(symbolFetcher.isEmpty()) {
            throw new RuntimeException("Symbol fetcher could not be loaded.");
        }

        router.get("/symbols/:symbol").handler(ctx -> {
            CompletableFuture<List<StockPrice>> future = symbolFetcher.get().fetch(ctx.pathParam("symbol"));

            future.orTimeout(2, TimeUnit.SECONDS)
            .whenComplete((result, error) -> {
                if(error == null) {
                    ctx.response().end(Json.encode(result));
                } else {
                    ctx.response().setStatusCode(500).end("Error fetching symbols");
                }
            });
        });


        StockWatchStore stockWatchStore = ServiceLoader.load(StockWatchStore.class).findFirst().orElseThrow(() -> new RuntimeException("No stock watch store isntalled"));
        router.post("/watch").handler(BodyHandler.create()).handler(ctx -> {
            JsonObject json = ctx.getBodyAsJson();
            String symbol = json.getString("symbol");

            symbolFetcher.get().fetch(symbol).thenApply(prices -> {
                StockPrice stockPrice = prices.stream().filter(p -> p.getSymbol().equals(symbol)).findFirst().orElseThrow(() -> new RuntimeException("Could not find base price for symbol"));
                stockWatchStore.store(new StockWatch(symbol, json.getDouble("changePercentage"), stockPrice.getPrice()));
                return 204;
            }).orTimeout(2, TimeUnit.SECONDS)
            .whenComplete((status, error) -> {
                if(error != null) {
                    status = 500;
                }

                ctx.response().setStatusCode(status).end();
            });

        });

        router.get("/watches").handler(ctx -> {
            String json = Json.encode(stockWatchStore.list());
            ctx.response().end(json);
        });

        ScheduledWatcher.getInstance().watch();

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
