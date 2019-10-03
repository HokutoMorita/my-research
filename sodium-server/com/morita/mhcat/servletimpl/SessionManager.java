package com.morita.mhcat.servletimpl;
import java.util.*;
import java.util.concurrent.*;
import nz.sodium.*;

/**
 * セッションオブジェクトを管理するクラス
 */
class SessionManager {
	private final ScheduledExecutorService scheduler;
	
    @SuppressWarnings("unused")
	private final ScheduledFuture<?> cleanerHandle;
	
    private final int CLEAN_INTERVAL = 60; // seconds
	private final int SESSION_TIMEOUT = 10; // minutes
	
    private Map<String, Cell<HttpSessionImpl>> sessions = new ConcurrentHashMap<String, Cell<HttpSessionImpl>>();
	private SessionIdGenerator sessionIdGenerator;
	
	SessionManager() {
		scheduler = Executors.newSingleThreadScheduledExecutor();

		Runnable cleaner = new Runnable() {
			public void run() {
				for (String id : sessions.keySet()) {
					cleanSession(id);
				}
			}
		};
		this.cleanerHandle = scheduler.scheduleWithFixedDelay(cleaner, CLEAN_INTERVAL, CLEAN_INTERVAL, TimeUnit.SECONDS);
		this.sessionIdGenerator = new SessionIdGenerator();
    }

    synchronized Cell<HttpSessionImpl> getSession(String id) {
		StreamSink<Unit> sAccess = new StreamSink<>();
		Cell<HttpSessionImpl> cHttpSession = sessions.get(id);
		Stream<HttpSessionImpl> sHttpSession = sAccess.snapshot(cHttpSession, (u, session) -> session);
		Listener l = sHttpSession.listen(x -> {
			x.access();
		});
		sAccess.send(Unit.UNIT);
		l.unlisten();
		return cHttpSession;
    }

    Cell<HttpSessionImpl> createSession() {
		// セッションIDを生成します。
		String id = this.sessionIdGenerator.generateSessionId();
		HttpSessionImpl httpSessionImpl = new HttpSessionImpl(id);
		CellSink<HttpSessionImpl> cHttpSessionSink = new CellSink<>(httpSessionImpl);
		Cell<HttpSessionImpl> cHttpSession = cHttpSessionSink;
		sessions.put(id, cHttpSession);
		return cHttpSession;
    }

	/**
	 * 保持しているセッションオブジェクトをじゅんに確認し、SESSION_TIMEOUTの分数を超えたセッションオブジェクトを削除します。
	 */
    private synchronized void cleanSession(String id) {
		StreamSink<Unit> sClean = new StreamSink<>();
		// filterで何も該当しなかった場合は、ユニットが返ってる可能性あり
		Stream<HttpSessionImpl> sSession = sClean.snapshot(sessions.get(id), (u, cSession) -> cSession)
												 .filter(x -> x.getLastAccessedTime() < (System.currentTimeMillis() - (SESSION_TIMEOUT * 60 * 1000)));
		Listener l = sSession.listen(x -> {
			System.out.println(id + " is session time out");
			sessions.remove(id);
		});
		sClean.send(Unit.UNIT);
		l.unlisten();
    }
}