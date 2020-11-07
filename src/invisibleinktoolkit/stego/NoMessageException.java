// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.stego;

public class NoMessageException extends Exception
{
    private static final long serialVersionUID = -3478476548544952717L;
    
    public String toString() {
        return "No message found with current settings";
    }
}
