package org.nuunframework.kernel.internal.scanner;

public class ClasspathStrategy {
    private final Strategy strategy;
    private final boolean additional;

    public ClasspathStrategy() {
        this.strategy = Strategy.ALL;
        this.additional = true;
    }

    public ClasspathStrategy(Strategy strategy, boolean additional) {
        this.strategy = strategy;
        this.additional = additional;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public boolean isAdditional() {
        return additional;
    }

    public enum Strategy {
        ALL,
        SYSTEM,
        CLASSLOADER,
        NONE
    }
}
