// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.stego;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;

public class InsertableMessage
{
    private String mPath;
    private int mCount;
    private FileInputStream mMsgFile;
    private byte[] mBuffer;
    private boolean mIsFileFinished;
    
    public InsertableMessage(final String path) throws FileNotFoundException, SecurityException, IOException {
        this.mPath = path;
        this.mMsgFile = new FileInputStream(path);
        this.mBuffer = new byte[1];
        this.mIsFileFinished = false;
        final int status = this.mMsgFile.read(this.mBuffer);
        this.mCount = 8;
        if (status == -1) {
            throw new IOException("File is empty!");
        }
    }
    
    public boolean nextBit() throws IOException {
        if (this.mIsFileFinished) {
            throw new IOException("File reading has finished!");
        }
        final boolean bit = (this.mBuffer[0] >> this.mCount - 1 & 0x1) == 0x1;
        --this.mCount;
        if (this.mCount == 0) {
            final int status = this.mMsgFile.read(this.mBuffer);
            this.mCount = 8;
            if (status == -1) {
                this.mIsFileFinished = true;
                this.mMsgFile.close();
            }
        }
        return bit;
    }
    
    public boolean notFinished() {
        return !this.mIsFileFinished;
    }
    
    public long getSize() throws IOException, FileNotFoundException {
        final RandomAccessFile raf = new RandomAccessFile(this.mPath, "r");
        final long filesize = raf.length();
        raf.close();
        return filesize;
    }
}
