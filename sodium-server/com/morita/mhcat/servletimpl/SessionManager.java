package com.morita.mhcat.servletimpl;
import java.util.*;
import java.util.concurrent.*;

/**
 * �Z�b�V�����I�u�W�F�N�g���Ǘ�����N���X
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
		// �Z�b�V����ID�𐶐����܂��B
		String id = this.sessionIdGenerator.generateSessionId();
		HttpSessionImpl session = new HttpSessionImpl(id);
		sessions.put(id, session);
		return session;
    }

	/**
	 * �ێ����Ă���Z�b�V�����I�u�W�F�N�g�������Ɋm�F���ASESSION_TIMEOUT�̕����𒴂����Z�b�V�����I�u�W�F�N�g���폜���܂��B
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