package codeone.symbolfetch.test;

import codeone.symbolfetch.StockPrice;
import codeone.symbolfetch.SymbolFetcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SymbolFetchTest {
    @Test
    public void testServiceLocate() {
        SymbolFetcher fetcher = new FetchTester().getFetcher();

        assertNotNull(fetcher);
    }

    @Test
    public void testFetch() throws InterruptedException, ExecutionException, TimeoutException {
        SymbolFetcher fetcher = new FetchTester().getFetcher();

        CompletableFuture<List<StockPrice>> nflx = fetcher.fetch("NFLX");
        List<StockPrice> prices = nflx.get(2, TimeUnit.SECONDS);
        assertEquals(1, prices.size());
    }
}
