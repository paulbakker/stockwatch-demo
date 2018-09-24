import codeone.symbolfetch.SymbolFetcher;
import codeone.symbolfetch.alphavantage.AlphavantageFetcher;

module symbolfetch {
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    exports codeone.symbolfetch;
    opens codeone.symbolfetch.alphavantage to com.fasterxml.jackson.databind;


    provides SymbolFetcher with AlphavantageFetcher;
}