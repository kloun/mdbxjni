package com.castortech.mdbxjni.pool;

import java.util.Objects;

import com.castortech.mdbxjni.Database;
import com.castortech.mdbxjni.Env;
import com.castortech.mdbxjni.SecondaryDatabase;
import com.castortech.mdbxjni.Transaction;

public class CursorKey {
	private final long threadId;
	private final Env env;
	private final Database db;
	private final Transaction txn;
	private final boolean secondary;

	public CursorKey(final Env env, final Database db, final Transaction txn) {
		threadId = txn.getThreadId();
		this.env = env;
		this.db = db;
		this.txn = txn;
		secondary = db instanceof SecondaryDatabase;
	}

	public CursorKey(final Env env, final boolean secondary, final Transaction txn) {
		threadId = txn.getThreadId();
		this.env = env;
		db = null;
		this.txn = txn;
		this.secondary = secondary;
	}

	public long getThreadId() {
		return threadId;
	}

	public Database getDb() {
		return db;
	}

	public Transaction getTxn() {
		return txn;
	}

	public boolean isSecondary() {
		return secondary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(env, txn, db, isSecondary());
	}

	/**
	 * Here equals is skewed to only take the Env and type into consideration so that the keyed pool can operate
	 * correctly (i.e. as a pair of keys, standard and secondary)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CursorKey other = (CursorKey)obj;
		return Objects.equals(env, other.env) && Objects.equals(txn, other.txn) && Objects.equals(db, other.db) &&
				Objects.equals(secondary, other.secondary);
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [threadId=");
		sb.append(threadId);
		sb.append(", db=");
		sb.append(db);
		sb.append(", txn=");
		sb.append(txn);
		sb.append("]");
		return sb.toString();
	}
}