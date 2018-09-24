module stockwatch.cron {
    requires stockwatch.api;
    requires symbolfetch;
    requires slf4j.api;

    exports codeone.stockwatch.cron;

    uses codeone.stockwatch.api.StockWatchStore;
    uses codeone.symbolfetch.SymbolFetcher;
}