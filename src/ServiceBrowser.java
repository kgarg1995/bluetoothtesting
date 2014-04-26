import javax.microedition.io.*;
import javax.bluetooth.*;

public class ServiceBrowser implements DiscoveryListener{
	static int SERVICE_NAME_ATTRID = 0x0100 ;
	RemoteDevice discovered[] = new RemoteDevice[255];
	int num_discovered; 

public void deviceDiscovered(RemoteDevice rd, DeviceClass cod){
	
	String addr = rd . getBluetoothAddress ( ) ;
	String name = " " ;
	try {
	name = rd . getFriendlyName (true );
	} 
	catch( java . io . IOException e ) {}
	this . discovered[this . num_discovered] = rd ;
	this . num_discovered++;
	System . out . println( "discovered " + addr + " - " + name ) ;
}


public void inquiryCompleted ( int status ) {
	System . out . println( "device inquiry complete" ) ;
	synchronized(this) {
		try { this . notifyAll ( ) ; } catch(Exception e) {}
	}
}

public void servicesDiscovered ( int transID , ServiceRecord[] rec ){
for( int i=0; i<rec . length ; i++ ) {
DataElement d =
rec[i ] . getAttributeValue( SERVICE_NAME_ATTRID ) ;
	if ( d != null ) System . out . println( (String)d . getValue ( ) ) ;
	else System . out . println( "unnamed service" ) ;
	System . out . println(rec[i ] . getConnectionURL (
			ServiceRecord . NOAUTHENTICATE_NOENCRYPT , false ) ) ;
							}
}


public void serviceSearchCompleted( int transID , int respCode ) {
	System . out . println( "service search complete" ) ;
	synchronized(this) {
		try { this . notifyAll ( ) ; } catch(Exception e) {}
	}
}

public static void main(String argv[ ] ) {
	try {
		ServiceBrowser sb = new ServiceBrowser ( ) ;
		LocalDevice ld = LocalDevice . getLocalDevice ( ) ;//there is only one local Bluetooth adapter, and the
		//singleton instance of this adapter is retrieved by invoking the static method
		DiscoveryAgent da = ld . getDiscoveryAgent ( ) ;/*The LocalDevice class can be used to obtain and set information about
the local device, and update advertised service records.*/
		
		// device inquiry
		sb . num_discovered = 0;
		da .startInquiry ( DiscoveryAgent . GIAC , sb ) ;
		synchronized(sb) {
			try { sb . wait ( ) ; } catch( Exception e ) {}
			
}
/*DiscoveryAgent.GIAC to instruct the agent to search for all generally discoverable
devices. In rare cases, different values can be used to find devices
responding to specialized access codes. The second parameter, sb, is a class
implementing the DiscoveryListener interface.*/
		
		//service search
		UUID uuids[] = new UUID[ 1 ] ;
		uuids[0] = new UUID( 0x1002 ) ;
		int attridset[] = new int[ 1 ] ;
		attridset[0] = SERVICE_NAME_ATTRID ;
		
/*When the device inquiry is complete, it then iterates through each detected device
and searches it for all publicly advertised services (using the reserved Public
Browse Group Universally Unique Identifier (UUID) of 0x1002)*/
		
		for( int i=0; i<sb . num_discovered ; i++ ) {
				RemoteDevice rd = sb . discovered[i ] ;
				da . searchServices(attridset , uuids , rd , sb ) ;
				synchronized(sb) {
					try { sb . wait ( ) ; } catch( Exception e ) {}
				}
		}
	} catch( BluetoothStateException e ) {
		System . out . print(e . toString ( ) ) ;
	}
  };
};