module stockwatch.web {
    requires vertx.web;
    requires vertx.core;
    requires symbolfetch;
    requires stockwatch.api;
    requires stockwatch.cron;

    uses codeone.symbolfetch.SymbolFetcher;
    uses codeone.stockwatch.api.StockWatchStore;
}