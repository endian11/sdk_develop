/**
 * Java RTP Library (jlibrtp)
 * Copyright (C) 2006 Arne Kepp
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.travelrely.core.nrs.nr.jlibrtp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import com.travelrely.core.nrs.nr.util.RingBuffer4Play;
import com.travelrely.core.nrs.nr.util.memUnit;
import com.travelrely.core.nrs.nr.voice.rtpTerminal;

import android.os.Process;
import android.util.Log;
/**
 * The RTP receiver thread waits on the designated UDP socket for new packets.
 * 
 * Once one arrives, it is parsed and tested. We also check the ip-address of the sender. 
 * If accepted, the packet is added onto the packet buffer of the participant.
 * 
 * A separate thread moves the packet from the packet buffer to the application.
 * 
 * @author Arne Kepp
 */
public class RTPReceiverThread extends Thread {
	/** Parent RTP Session */
	RTPSession rtpSession = null;
	FileOutputStream logFileOutputStream=null;

	RTPReceiverThread(RTPSession session) {
		rtpSession = session;
		if(RTPSession.rtpDebugLevel > 1) {
			System.out.println("<-> RTPReceiverThread created");
		} 
	}

	public void run() {
		try {
			SimpleDateFormat sfDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			logFileOutputStream = new FileOutputStream("/sdcard/log_"+sfDateFormat.format(new Date())+".txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
//		if(RTPSession.rtpDebugLevel > 1) {
//			if(rtpSession.mcSession) {
//				System.out.println("-> RTPReceiverThread.run() starting on MC " + rtpSession.rtpMCSock.getLocalPort() );
//			} else {
//				System.out.println("-> RTPReceiverThread.run() starting on " + rtpSession.rtpSock.getLocalPort() );
//			}
//		}
		int preSeq=0;
		while(!rtpSession.endSession) {
//			if(RTPSession.rtpDebugLevel > 6) {
//				if(rtpSession.mcSession) {
//					System.out.println("-> RTPReceiverThread.run() waiting for MC packet on " + rtpSession.rtpMCSock.getLocalPort() );
//				} else {
//					System.out.println("-> RTPReceiverThread.run() waiting for packet on " + rtpSession.rtpSock.getLocalPort() );
//				}
//			}

			// Prepare a packet
			byte[] rawPkt = new byte[1500];
			DatagramPacket packet = new DatagramPacket(rawPkt, rawPkt.length);
			// Wait for it to arrive
//			if(RTPSession.rtpDebugLevel > 6) {
//				System.out.println("rtpSession.mcSession is "+rtpSession.mcSession);
//			}
			if(! rtpSession.mcSession) {
				//Unicast
				try {
//					if(RTPSession.rtpDebugLevel > 6) {
//						System.out.println("rtp sock is  "+rtpSession.rtpSock+" port is "+rtpSession.rtpSock.getLocalPort());
//					}
					rtpSession.rtpSock.receive(packet);
//					if(RTPSession.rtpDebugLevel > 6) {
//						System.out.println("got rtp packet at "+System.currentTimeMillis());
//					}
				} catch (IOException e) {
					if(!rtpSession.endSession) {
						e.printStackTrace();
					} else {
						continue;
					}
				}
			} else {
				//Multicast 
//				try {
//					rtpSession.rtpMCSock.receive(packet);
//					if(RTPSession.rtpDebugLevel > 6) {
//						System.out.println("got rtp packet at "+System.currentTimeMillis());
//					}
//				} catch (IOException e) {
//					if(!rtpSession.endSession) {
//						e.printStackTrace();
//					} else {
//						continue;
//					}
//				}
			}

			// Parse the received RTP (?) packet
			//Log.e("amrlib", "recv data!");
			RtpPkt pkt = new RtpPkt(rawPkt, packet.getLength());
//			int seqnum =(((int)rawPkt[2] & 0xff) << 8)+ ((int)rawPkt[3]&0xff);
//			Log.e("rtplib", "seq num calc is "+seqnum);
//			try {
//				logFileOutputStream.write(("\nseq num calc is "+seqnum).getBytes());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if (seqnum!=pkt.seqNumber) {
//				Log.e("amrlib","seq num calc is "+seqnum +", pkt seqnumber is "+pkt.seqNumber);
//			}
			//Log.e("rtplib","pkt.seqNumber is "+pkt.seqNumber);
			if (preSeq!=0 && pkt.seqNumber-preSeq!=1) {
				Log.e("rtplib", "seq not continue, pre is "+preSeq+",cur is "+pkt.seqNumber);
			}
			preSeq=pkt.seqNumber;
//			if(true)
//				continue;
			
			if (false==pkt.isValid) {
				Log.e("rtplib","packet is not valid:"+rtpTerminal.printHexString(rawPkt));
				continue;
			}
			// Check whether it was valid.
//			if(pkt == null) {
//				System.out.println("Received invalid RTP packet. Ignoring");
//				continue;
//			}
			
			long pktSsrc = pkt.getSsrc();
			
			// Check for loops and SSRC collisions
//			if( rtpSession.ssrc == pktSsrc )
//				rtpSession.resolveSsrcConflict();
//			
//			long[] csrcArray = pkt.getCsrcArray();
//			if( csrcArray != null) {
//				for(int i=0; i< csrcArray.length; i++) {
//					if(csrcArray[i] == rtpSession.ssrc);
//						rtpSession.resolveSsrcConflict();
//				}
//			}
			
//			if(RTPSession.rtpDebugLevel > 17) {
//				System.out.println("-> RTPReceiverThread.run() rcvd packet, seqNum " + pktSsrc );
//				if(RTPSession.rtpDebugLevel > 10) {
//					String str = new String(pkt.getPayload());
//					System.out.println("-> RTPReceiverThread.run() payload is " + str );
//				}
//			}
			
			//Find the participant in the database based on SSRC
			Participant part = rtpSession.partDb.getParticipant(pktSsrc);

			if(part == null) {
				Log.e("amrlib", "part is null");
				InetSocketAddress nullSocket = null;
				part = new Participant((InetSocketAddress) packet.getSocketAddress(), nullSocket, pkt.getSsrc());
				part.unexpected = true;
				Log.e("rtplib","RTPReceiverThread: Got an unexpected packet");
				rtpSession.partDb.addParticipant(1,part);
			}

			
			byte[] payload=pkt.getPayload();
			if (payload.length==33 || payload.length==7) {
				if (payload.length==33) {
					RingBuffer4Play.getInstance().writeMemunit(new memUnit(payload)); //write ring buffer
				}
			}else {
				Log.e("amrlib", "payload len is"+payload.length);
			}
			if (true) {
				continue;
			}
			Log.e("amrlib", "program runs here!");
			// Do checks on whether the datagram came from the expected source for that SSRC.
			if(part.rtpAddress == null || packet.getAddress().equals(part.rtpAddress.getAddress())) {
				PktBuffer pktBuffer = part.pktBuffer;

				if(pktBuffer != null) {
					//A buffer already exists, append to it
					pktBuffer.addPkt(pkt);
				} else {
					// Create a new packet/frame buffer
					pktBuffer = new PktBuffer(this.rtpSession, part,pkt);
					part.pktBuffer = pktBuffer;
				}
			} else {
				System.out.println("RTPReceiverThread: Got an unexpected packet from " + pkt.getSsrc() 
						+ " the sending ip-address was " + packet.getAddress().toString() 
						+ ", we expected from " + part.rtpAddress.toString());
			}

			// Statistics for receiver report.
			//part.updateRRStats(packet.getLength(), pkt);
			// Upate liveness
			part.lastRtpPkt = System.currentTimeMillis();

			if(RTPSession.rtpDebugLevel > 5) {
				System.out.println("<-> RTPReceiverThread signalling pktBufDataReady");
			}
			
			// Signal the thread that pushes data to application
			rtpSession.pktBufLock.lock();
			try { rtpSession.pktBufDataReady.signalAll(); } finally {
				rtpSession.pktBufLock.unlock();
			}

		}
		try {
			logFileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
