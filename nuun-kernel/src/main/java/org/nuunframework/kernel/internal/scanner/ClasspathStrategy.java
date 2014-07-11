package org.nuunframework.kernel.internal.scanner;

public class ClasspathStrategy {
    public static final int DEFAULT_THRESHOLD = 2;

    private final Strategy strategy;
    private final boolean additional;
    private final boolean deduplicate;
    private final boolean removeTrailingSlash;
    private final int threshold;

    public ClasspathStrategy() {
        this.strategy = Strategy.ALL;
        this.additional = true;
        this.deduplicate = true;
        this.removeTrailingSlash = true;
        this.threshold = DEFAULT_THRESHOLD;
    }

    public ClasspathStrategy(Strategy strategy, boolean additional, boolean deduplicate, boolean removeTrailingSlash, int threshold) {
        this.strategy = strategy;
        this.additional = additional;
        this.deduplicate = deduplicate;
        this.removeTrailingSlash = removeTrailingSlash;
        this.threshold = threshold;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public boolean isAdditional() {
        return additional;
    }

    public boolean isDeduplicate() {
        return deduplicate;
    }

    public boolean isRemoveTrailingSlash() {
        return removeTrailingSlash;
    }

    public int getThreshold() {
        return threshold;
    }

    public enum Strategy {
        ALL,
        SYSTEM,
        CLASSLOADER,
        NONE
    }
}
