/**
 * 
 */
package se.mrpeachum.scheduler.exception;

/**
 * @author eolsson
 *
 */
public class RedirectException extends RuntimeException {

    private String redirectUrl;
    
    private static final long serialVersionUID = 1L;

    public RedirectException(String url) {
        this.redirectUrl = url;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
}
