import java.io.IOException;
import java.io.InputStream;

class A extends InputStream {

  public int pos;
  public String buffer;

  @Override
  public int read() throws IOException {
    if (pos == buffer.length()) {
      return -1; // OK
    }
    return buffer.getBytes()[pos++]; // Noncompliant {{Convert this signed byte into an unsigned byte.}}
  }

  @Override
  public int read(byte[] b) throws IOException {
    return b[pos++]; // Noncompliant
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return b[off + (pos++)]; // Noncompliant
  }
}

class B extends A {
  @Override
  public int read() throws IOException {
    return buffer.getBytes()[pos++] & 0xFF;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return (b[pos++] & 0xFF);
  }
}
