// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import javax.swing.SwingUtilities;

public abstract class WorkerThread
{
    private AThread mThread;
    
    public WorkerThread() {
        final Runnable doFinished = new Runnable() {
            public void run() {
                WorkerThread.this.finished();
            }
        };
        final Runnable doSomeWork = new Runnable() {
            public void run() {
                try {
                    WorkerThread.this.doWork();
                }
                finally {
                    WorkerThread.this.mThread.clear();
                }
                WorkerThread.this.mThread.clear();
                SwingUtilities.invokeLater(doFinished);
            }
        };
        final Thread t = new Thread(doSomeWork);
        this.mThread = new AThread(t);
    }
    
    public void start() {
        final Thread t = this.mThread.get();
        if (t != null) {
            t.start();
        }
    }
    
    public void interrupt() {
        final Thread t = this.mThread.get();
        if (t != null) {
            t.interrupt();
            this.isInterrupted();
        }
        this.mThread.clear();
    }
    
    public abstract void doWork();
    
    public void finished() {
    }
    
    public void isInterrupted() {
    }
    
    private static class AThread
    {
        private Thread mThread;
        
        public AThread(final Thread t) {
            this.mThread = t;
        }
        
        public synchronized Thread get() {
            return this.mThread;
        }
        
        public synchronized void clear() {
            this.mThread = null;
        }
    }
}
