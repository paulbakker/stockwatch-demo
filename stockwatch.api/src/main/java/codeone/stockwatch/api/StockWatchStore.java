package codeone.stockwatch.api;

import java.util.Set;

public interface StockWatchStore {
    void store(StockWatch stockWatch);
    void delete(String symbol);
    Set<StockWatch> list();
}
