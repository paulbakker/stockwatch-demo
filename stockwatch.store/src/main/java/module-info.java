module stockwatch.store {
    requires stockwatch.api;


    provides codeone.stockwatch.api.StockWatchStore with codeone.stockwatch.store.InMemStore;
}