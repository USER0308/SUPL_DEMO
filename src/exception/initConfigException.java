package exception;

public class initConfigException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public initConfigException(){
		
	}
	
	public initConfigException(String msg){
        super(msg);
    }
	
    public initConfigException(String msg,Throwable cause){
        super(msg,cause);
    }
    
    public initConfigException(Throwable cause){
        super(cause);
    }
}
