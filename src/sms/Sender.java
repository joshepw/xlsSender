package sms;
/*
 * 
 * @author : William Alexander
 *
*/
import java.util.Date;
import main.mainFrame;
import resources.Toast;

public class Sender implements Runnable  {

    private static final long STANDARD=500;
    private static final long LONG=2000;
    private static final long  VERYLONG=20000;

    SerialConnection mySerial =null;

    static final private char cntrlZ=(char)26;
    String in, out;
    Thread aThread=null;
    private long delay=STANDARD;
    String recipient=null;
    String message=null;
    private Toast toast = new Toast();

    private String csca; // the message center
    private SerialParameters defaultParameters;
    public int step;
    public int status=-1;
    public long messageNo=-1;

    public Sender(String recipient, String message){
        csca="+6596845999";
        defaultParameters= new SerialParameters ("COM3",9600,0,0,8,1,0);
        this.recipient=recipient;
        this.message=message;

    }

    public Sender(String recipient, String message,String port,String service){
        csca=service;
        defaultParameters= new SerialParameters (port,9600,0,0,8,1,0);
        this.recipient=recipient;
        this.message=message;

    }

      /**
       * connect to the port and start the dialogue thread
       */
    public int send () throws Exception{

        SerialParameters params = defaultParameters;
        mySerial =new SerialConnection (params);
        mySerial.openConnection();
        aThread=new Thread(this);
        aThread.start();
        return 0;
    }

      /**
       * implement the dialogue thread,
       * message / response via steps,
       * handle time out
       */

    @Override
    public void run(){

        boolean timeOut=false;
        long startTime=(new Date()).getTime();



        while ((step <7) && (!timeOut)){
          //check where we are in specified delay
          timeOut=((new Date()).getTime() - startTime)>delay;

          //if atz does not work, type to send cntrlZ and retry, in case a message was stuck
          if (timeOut && (step==1)) {
              step=-1;
              mySerial.send(        ""+cntrlZ);
              System.out.println("SENDER >> TIME OUT");
              toast.showItem("Error al enviar", "Tiempo de comunicacion agotado", "error");
              mainFrame.LOG("SMS >> Tiempo de comunicacion agotado", 2);
          }

          //read incoming string
          String result=  mySerial.getIncommingString() ;
          int expectedResult=-1;

          try{

            switch (step){
              case 0:
                main.mainFrame.textLed("Enviando Mensaje ... ");
                mySerial.send("atz");
                delay=LONG;
                startTime=(new Date()).getTime();
                System.out.println("SENDER >> ATZ [OK]");
                break;

              case 1:
                delay=STANDARD;
                mySerial.send("ath0");
                startTime=(new Date()).getTime();
                System.out.println("SENDER >> ATHO [OK]");
                break;
              case 2:
                expectedResult=result.indexOf("OK");
                //log ("received ok ="+expectedResult);
                if (expectedResult>-1){
                  mySerial.send("at+cmgf=1");
                  startTime=(new Date()).getTime();
                  System.out.println("SENDER >> AT+CMGF=1");
                  mainFrame.textLed("Iniciando configuracion SMS");
                  mainFrame.LOG("SMS >> Iniciando configuracion SMS", 0);
                }else{
                    step=step-1;
                }
                break;
              case 3:
                expectedResult=result.indexOf("OK");
                System.out.println("SENDER >> RECEIVED OK ="+expectedResult);
                if (expectedResult>-1){
                  mySerial.send("at+csca=\""+csca+"\"");
                  startTime=(new Date()).getTime();
                  System.out.println("SENDER >> AT+CSCA "+csca);
                }else{
                  step=step-1;
                }

                break;
              case 4:
                expectedResult=result.indexOf("OK");

               // log ("received ok ="+expectedResult);
                if (expectedResult>-1){
                  mySerial.send("at+cmgs=\""+recipient+"\"");
                  startTime=(new Date()).getTime();
                    System.out.println("SENDER >> AT+CMGS "+recipient);
                }else{
                  step=step-1;
                }

                break;
              case 5:
                expectedResult=result.indexOf(">");

               // log ("received ok ="+expectedResult);
                if (expectedResult>-1){
                  mySerial.send(message+cntrlZ);
                  startTime=(new Date()).getTime();
                    System.out.println("SENDER >> SENDING ...");
                    mainFrame.textLed("Procesando envio de SMS");
                }else{
                  step=step-1;
                }
                delay=VERYLONG;//waitning for message ack
                  System.out.println("SENDER >> WAIT FOR STATUS");
                  mainFrame.textLed("En espera de respuesta ...");
                  mainFrame.LOG("SMS >> En espera de respuesta ...", 3);
                break;

              case 6:
                expectedResult=result.indexOf("OK");
                //read message number
                if (expectedResult>-1){
                  int n=result.indexOf("CMGS:");
                  result=result.substring(n+5);
                  n=result.indexOf("\n");
                  status=0;
                  messageNo=Long.parseLong(result.substring(0,n).trim() );
                  System.out.println("SENDER >> STATUS OK");
                  mainFrame.textLed("SMS enviado con exito");
                  mainFrame.LOG("SMS >> El mensaje a "+recipient+" a sido enviado con exito!", 1);
                  toast.showItem("Mensaje enviado", "El mensaje a "+recipient+" a sido enviado con exito!", "send");

                }else{
                  step=step-1;
                }

              break;
            }
            step=step+1;

            aThread.sleep(100);

          }catch (NumberFormatException | InterruptedException e){
              System.out.println("ERROR >> "+e.getMessage());
          }
        }

        mySerial.closeConnection() ;

        //if timed out set status

        if (timeOut ) {
            status=-2;
            System.out.println("SENDER >> TIME OUT AT STEP"+step);
            toast.showItem("Error al enviar", "Tiempo de espera agotado, Error #"+step, "error");
            mainFrame.LOG("SMS >> Tiempo de espera agotado, Error #"+step, 2);
        }
      }

      private void log(String s){
        System.out.println (new java.util.Date()+":"+this.getClass().getName()+":"+s);
      }
}