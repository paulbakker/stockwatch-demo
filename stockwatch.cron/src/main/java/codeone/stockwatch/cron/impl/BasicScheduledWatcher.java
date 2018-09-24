package codeone.stockwatch.cron.impl;

import codeone.stockwatch.api.StockWatch;
import codeone.stockwatch.api.StockWatchStore;
import codeone.stockwatch.cron.ScheduledWatcher;
import codeone.symbolfetch.SymbolFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BasicScheduledWatcher implements ScheduledWatcher {
    private final static Logger LOG = LoggerFactory.getLogger(BasicScheduledWatcher.class);

    @Override
    public void watch() {
        SymbolFetcher symbolfetcher = ServiceLoader.load(SymbolFetcher.class).findFirst().orElseThrow(() -> new RuntimeException("No symbol fetcher found!"));
        StockWatchStore watchStore = ServiceLoader.load(StockWatchStore.class).findFirst().orElseThrow(() -> new RuntimeException("No stockwatch store found"));

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Checking current prices");
                Set<StockWatch> stockWatches = watchStore.list();
                symbolfetcher.fetch(stockWatches.stream().map(StockWatch::getSymbol).toArray(String[]::new))
                        .thenAccept(prices -> {
                            System.out.println(prices);
                            prices.forEach(price -> {
                                Optional<StockWatch> triggered = stockWatches.stream().filter(s -> s.getSymbol().equals(price.getSymbol())).filter(s -> s.shouldTrigger(price.getPrice())).findAny();
                                if(triggered.isPresent()) {
                                    System.out.println("Trigger for " + price.getSymbol());
                                    LOG.info("Trigger for {}", price.getSymbol());
                                } else {
                                    LOG.info("No trigger for {}", price.getSymbol());
                                }
                            });
                        }).whenComplete((result, error) -> {
                            if(error != null) {
                                LOG.error("Error getting prices", error);
                            }
                });
            }
        }, 10000, 10000);
    }
}
