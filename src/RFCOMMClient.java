import java . io . * ;
import javax . microedition.io .*;
import javax . bluetooth . * ;

public class RFCOMMClient {
	
	public static void main( String args[] ) {
		try {
			String url = "btspp://0123456789AB:3" ;
		
			StreamConnection con =
					(StreamConnection) Connector . open(url ) ;
		
			OutputStream os = con . openOutputStream ( ) ;
			InputStream is = con . openInputStream ( ) ;
		
			String greeting = "JSR-82 RFCOMM client says hello" ;
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
