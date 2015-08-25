package com.guigarage.photonia.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface AsyncService {

    <V> Future<V> call(Callable<V> callable, Consumer<Future<V>> onDone);

    Future<Void> run(Runnable runnable, Consumer<Future<Void>> onDone);

    <V> Future<V> call(Callable<V> callable);

    Future<Void> run(Runnable runnable);
}
