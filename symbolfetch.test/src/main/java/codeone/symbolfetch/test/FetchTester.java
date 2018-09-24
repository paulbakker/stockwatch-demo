package codeone.symbolfetch.test;

import codeone.symbolfetch.SymbolFetcher;

import java.util.ServiceLoader;

public class FetchTester {
    public SymbolFetcher getFetcher() {
        return ServiceLoader.load(SymbolFetcher.class).findFirst().get();
    }
}
