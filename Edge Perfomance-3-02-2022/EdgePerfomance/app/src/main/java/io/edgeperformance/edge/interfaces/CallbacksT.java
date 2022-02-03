package io.edgeperformance.edge.interfaces;

public interface CallbacksT<T> {

    void onSuccess(T data);

    default void onFailure(Throwable throwable) {
    }
}