package codeone.stockwatch.store;

import codeone.stockwatch.api.StockWatch;
import codeone.stockwatch.api.StockWatchStore;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class InMemStore implements StockWatchStore {
    private final static InMemStore store = new InMemStore();

    private final Set<StockWatch> watches = new CopyOnWriteArraySet<>();

    @Override
    public void store(StockWatch stockWatch) {
        watches.add(stockWatch);
    }

    @Override
    public void delete(String symbol) {
        watches.removeIf(w -> w.getSymbol().equals(symbol));
    }

    @Override
    public Set<StockWatch> list() {
        return watches;
    }

    public static StockWatchStore provider() {
        return store;
    }
}
