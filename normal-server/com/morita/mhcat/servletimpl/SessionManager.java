package com.morita.mhcat.servletimpl;
import java.util.*;
import java.util.concurrent.*;

/**
 * セッションオブジェクトを管理するクラス
 */
class SessionManager {
	private final ScheduledExecutorService scheduler;
	
    @SuppressWarnings("unused")
	private final ScheduledFuture<?> cleanerHandle;
	
    private final int CLEAN_INTERVAL = 60; // seconds
	private final int SESSION_TIMEOUT = 10; // minutes
	
    private Map<String, HttpSessionImpl> sessions = new ConcurrentHashMap<String, HttpSessionImpl>();
	private SessionIdGenerator sessionIdGenerator;
	
	SessionManager() {
		scheduler = Executors.newSingleThreadScheduledExecutor();

		Runnable cleaner = new Runnable() {
			public void run() {
				cleanSessions();
			}
		};
		this.cleanerHandle = scheduler.scheduleWithFixedDelay(cleaner, CLEAN_INTERVAL, CLEAN_INTERVAL, TimeUnit.SECONDS);
		this.sessionIdGenerator = new SessionIdGenerator();
    }

    synchronized HttpSessionImpl getSession(String id) {
		HttpSessionImpl ret = sessions.get(id);
		if (ret != null) {
			ret.access();
		}
		return ret;
    }

    HttpSessionImpl createSession() {
		// セッションIDを生成します。
		String id = this.sessionIdGenerator.generateSessionId();
		HttpSessionImpl session = new HttpSessionImpl(id);
		sessions.put(id, session);
		return session;
    }

	/**
	 * 保持しているセッションオブジェクトをじゅんに確認し、SESSION_TIMEOUTの分数を超えたセッションオブジェクトを削除します。
	 */
    private synchronized void cleanSessions() {
		for (Iterator<String> it = sessions.keySet().iterator(); it.hasNext();) {
			String id = it.next();
			HttpSessionImpl session = this.sessions.get(id);
			if (session.getLastAccessedTime() < (System.currentTimeMillis() - (SESSION_TIMEOUT * 60 * 1000))) {
				it.remove();
			}
		}
    }
}