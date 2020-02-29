package org.github.tgn.expanse.util;

import net.devtech.yajslib.io.PersistentInput;
import net.devtech.yajslib.io.PersistentOutput;
import net.devtech.yajslib.persistent.Persistent;
import java.io.IOException;
import java.util.function.Supplier;

public class EmptyPersistent<T> implements Persistent<T> {
	private final long versionHash;
	private final Supplier<T> supplier;

	public EmptyPersistent(long hash, Supplier<T> supplier) {
		this.versionHash = hash;
		this.supplier = supplier;
	}

	@Override
	public long versionHash() {
		return this.versionHash;
	}

	@Override
	public void write(T t, PersistentOutput output) throws IOException {

	}

	@Override
	public T read(PersistentInput input) throws IOException {
		return this.blank();
	}

	@Override
	public T blank() {
		return this.supplier.get();
	}
}
