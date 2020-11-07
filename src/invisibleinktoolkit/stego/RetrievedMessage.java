// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.stego;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RetrievedMessage
{
    private FileOutputStream mRetrievedMessage;
    private boolean mIsFinished;
    private int mBitCount;
    private int mBuffer;
    private String mPath;
    
    public RetrievedMessage(final String outfile) throws FileNotFoundException, SecurityException {
        this.mRetrievedMessage = new FileOutputStream(outfile);
        this.mPath = outfile;
        this.mIsFinished = false;
        this.mBitCount = 0;
        this.mBuffer = 0;
    }
    
    public void setNext(final boolean bit) throws IOException {
        if (this.mIsFinished) {
            throw new IOException("File has finished writing!");
        }
        int newbit = 0;
        if (bit) {
            newbit = 1;
        }
        this.mBuffer = (this.mBuffer << 1 | newbit);
        ++this.mBitCount;
        if (this.mBitCount == 8) {
            this.writeBuffer();
        }
    }
    
    private void writeBuffer() throws IOException {
        this.mRetrievedMessage.write(this.mBuffer);
        this.mBitCount = 0;
        this.mBuffer = 0;
    }
    
    public void close() throws IOException {
        if (this.mBitCount > 0) {
            final int left = 8 - this.mBitCount;
            this.mBuffer <<= left;
            this.writeBuffer();
        }
        this.mRetrievedMessage.close();
        this.mIsFinished = true;
    }
    
    public String getPath() {
        return this.mPath;
    }
}
