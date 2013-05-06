package networklib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import networklib.channel.AsyncChannel;
import networklib.channel.Channel;

public class TestUtils {

	public Channel[] setUpLoopbackChannels() throws IOException {
		final Channel[] channels = new Channel[2];

		Socket[] sockets = setUpLoppbackSockets();
		channels[0] = new Channel(sockets[0]);
		channels[1] = new Channel(sockets[1]);

		channels[0].start();
		channels[1].start();
		channels[0].initPinging();

		return channels;
	}

	public AsyncChannel[] setUpAsyncLoopbackChannels() throws IOException {
		final AsyncChannel[] channels = new AsyncChannel[2];

		Socket[] sockets = setUpLoppbackSockets();
		channels[0] = new AsyncChannel(sockets[0]);
		channels[1] = new AsyncChannel(sockets[1]);

		channels[0].start();
		channels[1].start();
		channels[0].initPinging();

		return channels;
	}

	private static Socket[] setUpLoppbackSockets() throws IOException {
		Socket[] sockets = new Socket[2];

		PipedInputStream in1 = new PipedInputStream();
		PipedOutputStream out1 = new PipedOutputStream(in1);
		PipedInputStream in2 = new PipedInputStream();
		PipedOutputStream out2 = new PipedOutputStream(in2);

		sockets[0] = new LoopbackSocket(out1, in2);
		sockets[1] = new LoopbackSocket(out2, in1);

		return sockets;
	}

	private static class LoopbackSocket extends Socket {
		private final OutputStream out;
		private final InputStream in;

		private boolean closed = false;

		LoopbackSocket(OutputStream out, InputStream in) {
			this.out = out;
			this.in = in;
		}

		@Override
		public OutputStream getOutputStream() {
			return out;
		}

		@Override
		public InputStream getInputStream() {
			return in;
		}

		@Override
		public boolean isClosed() {
			return closed;
		}

		@Override
		public synchronized void close() {
			closed = true;
		}
	}
}
