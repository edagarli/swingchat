package server;

import java.net.*;
import java.io.*;

/**
 * �û�����Ľ����
 */
public class Node {
	String username = null;
	Socket socket = null;
	ObjectOutputStream output = null;
	ObjectInputStream input = null;
		
	Node next = null;
}