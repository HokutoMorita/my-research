package com.morita.mhcat.servletimpl;
import java.util.*;
import java.util.concurrent.*;

import nz.sodium.*;

/**
 * �Z�b�V�����I�u�W�F�N�g���Ǘ�����N���X
 */
class SessionManager {
private final ScheduledExecutorService scheduler;
	
    @SuppressWarnings("unused")
	private final ScheduledFuture<?> cleanerHandle;
	
//    private final int CLEAN_INTERVAL = 60; // seconds
//	private final int SESSION_TIMEOUT = 10; // minutes
	
    // �Z�b�V�����^�C���A�E�g�m�F�p
	private final int CLEAN_INTERVAL = 5; // seconds
	private final int SESSION_TIMEOUT = 5; // minutes
	
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
		Cell<HttpSessionImpl> cSession = sessions.get(id);
		Stream<HttpSessionImpl> sSession = sAccess.snapshot(cSession, (u, session) -> session);
		if (cSession != null) {
			Listener l = Transaction.run(() -> {
				Listener l_ = sSession.listen(x -> {
					x.access();
				});
				sAccess.send(Unit.UNIT);
				return l_;
			});
//			Listener l = sSession.listen(x -> {
//				x.access();
//			});
//			sAccess.send(Unit.UNIT);
			l.unlisten();
			return cSession;
		} else {
			return createSession();
		}
    }

    Cell<HttpSessionImpl> createSession() {
		// �Z�b�V����ID�𐶐����܂��B
		String id = this.sessionIdGenerator.generateSessionId();
		HttpSessionImpl httpSessionImpl = new HttpSessionImpl(id);
		CellSink<HttpSessionImpl> cHttpSessionSink = new CellSink<>(httpSessionImpl);
		Cell<HttpSessionImpl> cHttpSession = cHttpSessionSink;
		sessions.put(id, cHttpSession);
		return cHttpSession;
    }

	/**
	 * �ێ����Ă���Z�b�V�����I�u�W�F�N�g�������Ɋm�F���ASESSION_TIMEOUT�̕����𒴂����Z�b�V�����I�u�W�F�N�g���폜���܂��B
	 */
    private synchronized void cleanSession(String id) {
		StreamSink<Unit> sClean = new StreamSink<>();
		// filter�ŉ����Y�����Ȃ������ꍇ�́A���j�b�g���Ԃ��Ă�\������
		Stream<HttpSessionImpl> sSession = sClean.snapshot(sessions.get(id), (u, cSession) -> cSession)
												 //.filter(x -> x.getLastAccessedTime() < (System.currentTimeMillis() - (SESSION_TIMEOUT * 60 * 1000)));
												 // �Z�b�V�����^�C���A�E�g�m�F�p
				                                 .filter(x -> x.getLastAccessedTime() < (System.currentTimeMillis() - (SESSION_TIMEOUT * 1000)));
		Listener l = Transaction.run(() -> {
			Listener l_ = sSession.listen(x -> {
				System.out.println(id + " is session time out");
				sessions.remove(id);
			});
			sClean.send(Unit.UNIT);
			return l_;
		});
//		Listener l = sSession.listen(x -> {
//			System.out.println(id + " is session time out");
//			sessions.remove(id);
//		});
//		sClean.send(Unit.UNIT);
		l.unlisten();
    }
}