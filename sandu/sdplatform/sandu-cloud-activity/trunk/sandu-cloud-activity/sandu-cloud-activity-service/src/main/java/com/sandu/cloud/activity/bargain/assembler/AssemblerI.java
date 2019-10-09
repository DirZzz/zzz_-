package com.sandu.cloud.activity.bargain.assembler;

public interface AssemblerI<F, T> {

    default T assembleParam(F from) {
        return null;
    }

    default void assembleParam(F from, T to) {
    }

    default T assembleResult(F from) {
        return null;
    }

    default void assembleResult(F from, T to) {
    }
}