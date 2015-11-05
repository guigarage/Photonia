package com.guigarage.photonia.util;

import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;

public interface ObservableRunnableFuture<V> extends RunnableFuture<V> {

    void onDone(Consumer<Future<V>> futureConsumer);
}
