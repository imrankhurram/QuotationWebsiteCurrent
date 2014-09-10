package com.nextcontrols.utility;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

public class Encoder {
   public static final String USASCII = "US-ASCII";
   public static final String ISO88591 = "ISO-8859-1";
     private String encoding;
     public Encoder(String pEncoding){
       encoding  = pEncoding;
   }
     public String byteBuffer2String(ByteBuffer buffer) {
       StringBuffer decodedBuffer = new StringBuffer();
       Charset charset = Charset.forName(encoding);
       CharsetDecoder decoder = charset.newDecoder().onMalformedInput(
               CodingErrorAction.IGNORE);
       try {
           CharBuffer charbuffer = decoder.decode(buffer);
           String temp = charbuffer.toString();
           decodedBuffer.append(temp);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return decodedBuffer.toString();
   }
     public byte[] stringBuffer2bytes(StringBuffer stringbuffer) {
       byte[] bytes = new byte[stringbuffer.length()];
       Charset charset = Charset.forName(encoding);
       ByteBuffer buffer = charset.encode(stringbuffer.toString());          buffer.get(bytes);
       return bytes;
   }
     public byte[] string2bytes(String string) {
       byte[] bytes = new byte[string.length()];
       Charset charset = Charset.forName(encoding);
       ByteBuffer buffer = charset.encode(string);          buffer.get(bytes);
       return bytes;
   }
  }
