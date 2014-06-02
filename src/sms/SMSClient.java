package sms;
/*
 * 
 * @author : William Alexander
 *
*/
public class SMSClient implements Runnable{

    public final static int SYNCHRONOUS=0;
    public final static int ASYNCHRONOUS=1;
    private Thread myThread=null;

    private int mode=-1;
    private String port = null;
    private String recipient=null;
    private String message=null;
    private String service=null;

    public int status=-1;
    public long messageNo=-1;


    public SMSClient(int mode,String port,String service) {
        this.port=port;
        this.mode=mode;
        this.service=service;
    }

    public int sendMessage (String recipient, String message){
        this.recipient=recipient;
        this.message=message;
//        myThread = new Thread(this);
//        myThread.start();
        run();
        
        return status;
    }
  
    @Override
    public void run(){
        Sender aSender = new Sender(recipient,message,port,service);
        try{
            aSender.send ();
            if (mode==SYNCHRONOUS) {
                while (aSender.status == -1){
                  myThread.sleep (1000);
                }
            }
            if (aSender.status == 0) { messageNo=aSender.messageNo ; }
        }catch (Exception e){
            System.out.println("ERROR >> "+e.getMessage());
        }
        this.status=aSender.status ;
        aSender=null;
  }
}