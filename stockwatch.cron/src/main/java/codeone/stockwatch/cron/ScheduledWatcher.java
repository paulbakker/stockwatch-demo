package codeone.stockwatch.cron;

import codeone.stockwatch.cron.impl.BasicScheduledWatcher;

public interface ScheduledWatcher {
    void watch();

    static ScheduledWatcher getInstance() {
        return new BasicScheduledWatcher();
    }
}
