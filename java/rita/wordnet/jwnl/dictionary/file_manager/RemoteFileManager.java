/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl.dictionary.file_manager;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * An object of this class can serve as a <code>FileManager</code> for remote <code>FileBackedDictionary</code>
 * instantiations using RMI. This class also contains utility routines to publish a <code>RemoteFileManager</code>
 * for remote use, and to lookup a remote one for local use.
 * <P>
 * To make a <CODE>RemoteFileManager</CODE> available to remote clients:
 * <PRE>
 *   System.setSecurityManager(new RMISecurityManager());
 *   LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
 *   new RemoteFileManager().bind();
 * </PRE>
 * <P>
 * To create a local <CODE>Dictionary</CODE> backed by a remote <CODE>RemoteFileManager</CODE>:
 * <PRE>
 *   Dictionary dictionary = new FileBackedDictionary(RemoteFileManager.lookup(hostname));
 * </PRE>
 */
public class RemoteFileManager extends FileManagerImpl {
  
	/** The standard RMI binding name. */
	public static final String BINDING_NAME = "jwnl";

	/**
	 * Construct a file manager backed by a set of files contained in the default WN search directory.
	 * See {@link FileManagerImpl} for a description of the default search directory.
	 * @exception RemoteException If remote operation failed.
	 */
	@SuppressWarnings("deprecation")
	public RemoteFileManager(String searchDir, Class dictionaryFileType) throws IOException, RemoteException {
		super(searchDir, dictionaryFileType);
		UnicastRemoteObject.exportObject(this);
	}

	/**
	 * Bind this object to the value of <code>BINDING_NAME</code> in the local RMI
	 * registry.
	 * @exception AlreadyBoundException If <code>BINDING_NAME</code> is already bound.
	 * @exception RemoteException If remote operation failed.
	 */
	public void bind() throws RemoteException, AlreadyBoundException {
		Registry registry = LocateRegistry.getRegistry();
		registry.bind(BINDING_NAME, this);
	}

	/**
	 * Lookup the object bound to the value of <code>BINDING_NAME</code> in the RMI
	 * registry on the host named by <var>hostname</var>
	 * @return An RMI proxy of type <code>FileManager</code>.
	 * @exception AccessException If this operation is not permitted.
	 * @exception NotBoundException If there is no object named <code>BINDING_NAME</code> in the remote registry.
	 * @exception RemoteException If remote operation failed.
	 * @exception UnknownHostException  If the host could not be located.
	 */
	public static FileManager lookup(String hostname) throws AccessException, NotBoundException, RemoteException, UnknownHostException {
		Registry registry = LocateRegistry.getRegistry(hostname);
		return (FileManager) registry.lookup(BINDING_NAME);
	}
}