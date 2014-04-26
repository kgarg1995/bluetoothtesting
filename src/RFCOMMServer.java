import java . io . * ;
import javax . microedition.io .*;
import javax . bluetooth . * ;

public class RFCOMMServer {
	public static void main( String args[] ) {
		try {
			String url = "btspp://localhost:" +
					new UUID( 0x1101 ) . toString( ) +
					";name=SampleServer" ;
			StreamConnectionNotifier service =
					( StreamConnectionNotifier ) Connector . open( url ) ;
			
			StreamConnection con =
					( StreamConnection ) service . acceptAndOpen ( ) ;
			OutputStream os = con . openOutputStream ( ) ;
			InputStream is = con . openInputStream ( ) ;
		
			String greeting = "JSR-82 RFCOMM server says hello" ;
			os . write( greeting . getBytes( ) ) ;
		
			
			byte buffer[] = new byte[80];
			int bytes_read = is . read( buffer ) ;
			String received = new String(buffer, 0, bytes_read ) ;
			System . out . println( "received: " + received ) ;
		
			con . close ( ) ;
		} catch ( IOException e ) {
			System . err . print(e . toString ( ) ) ;
		}
	}

}
