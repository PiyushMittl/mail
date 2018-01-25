package com.piyush.mail.services;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class Main {

	public static void main(String[] args) {

		Main gmail = new Main();
		gmail.read();

	}

	public void read() {

		Properties props = new Properties();

		try {

			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props, null);

			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", "pm31121988@gmail.com", "Sweetls2607@");

			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();

			System.out.println("Total Messages:- " + messageCount);

			Message[] messages = inbox.getMessages();
			System.out.println("------------------------------");

			for (int i = messageCount-1; i > 0; i--) {
				// System.out.println("Mail Subject:- " + messages[i].getSubject());

				Message m = messages[i];
				writePart(m);
			}

			inbox.close(true);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method checks for content-type based on which, it processes and fetches
	 * the content of the message
	 */
	public static void writePart(Part p) throws Exception {
		if (p instanceof Message)
			// Call methos writeEnvelope
			writeEnvelope((Message) p);

		System.out.println("----------------------------");
		System.out.println("CONTENT-TYPE: " + p.getContentType());

		if (p.isMimeType("APPLICATION/PDF")) {
			System.out.println("This is pdf");
			System.out.println("--------------------------->>>>>>>>>>>>");
			//System.out.println((String) p.getContent());
			Object o = p.getContent();
			InputStream is = (InputStream) o;
			OutputStream os = new FileOutputStream("new_source.pdf");
			byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while((bytesRead = is.read(buffer)) !=-1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            //flush OutputStream to write any buffered data to file
            os.flush();
            os.close();
		}
		
		
		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i));
		}
//		// check if the content is a nested message
//		else if (p.isMimeType("message/rfc822")) {
//			System.out.println("This is a Nested Message");
//			System.out.println("---------------------------");
//			writePart((Part) p.getContent());
//		}
//		// check if the content is an inline image
//		/*
//		 * else if (p.isMimeType("image/jpeg")) {
//		 * System.out.println("--------> image/jpeg"); Object o = p.getContent();
//		 * 
//		 * InputStream x = (InputStream) o; // Construct the required byte array
//		 * System.out.println("x.length = " + x.available()); int i=0; while ((i = (int)
//		 * ((InputStream) x).available()) > 0) { int result = (int) (((InputStream)
//		 * x).read(bArray)); if (result == -1) //int i = 0; i=0; byte[] bArray = new
//		 * byte[x.available()];
//		 * 
//		 * break; } FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
//		 * f2.write(bArray); }
//		 */
//		else if (p.getContentType().contains("image/")) {
//			System.out.println("content type" + p.getContentType());
//			File f = new File("image" + new Date().getTime() + ".jpg");
//			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
//			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
//			byte[] buffer = new byte[1024];
//			int bytesRead;
//			while ((bytesRead = test.read(buffer)) != -1) {
//				output.write(buffer, 0, bytesRead);
//			}
//		} else {
//			Object o = p.getContent();
//			if (o instanceof String) {
//				System.out.println("This is a string");
//				System.out.println("---------------------------");
//				System.out.println((String) o);
//			} else if (o instanceof InputStream) {
//				System.out.println("This is just an input stream");
//				System.out.println("---------------------------");
//				InputStream is = (InputStream) o;
//				is = (InputStream) o;
//				int c;
//				while ((c = is.read()) != -1)
//					System.out.write(c);
//			} else {
//				System.out.println("This is an unknown type");
//				System.out.println("---------------------------");
//				System.out.println(o.toString());
//			}
//		}

	}

	/*
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	public static void writeEnvelope(Message m) throws Exception {
		System.out.println("This is the message envelope");
		System.out.println("---------------------------");
		Address[] a;

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("FROM: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("TO: " + a[j].toString());
		}

		// SUBJECT
		if (m.getSubject() != null)
			System.out.println("SUBJECT: " + m.getSubject());

		// FILE NAME
		if (m.getFileName() != null)
			System.out.println("FILE NAME: " + m.getFileName());

		// FILE NAME
		if (m.getReceivedDate() != null)
			System.out.println("RECEIVED DATE: " + m.getReceivedDate());
		
		System.out.println("MESSAGE NUMBER: " + m.getMessageNumber());

	}

}