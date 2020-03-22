package io.tangledwires.simmer;

import com.google.common.util.concurrent.RateLimiter;
import org.checkerframework.common.value.qual.IntRange;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ThrottledFileInputStream extends InputStream {

    @Nonnull private final FileInputStream innerStream;
    @Nonnull private final RateLimiter limiter;

    public ThrottledFileInputStream(@Nonnull String filePath, @IntRange(from=1) int bytesPerSecond) throws FileNotFoundException {
        innerStream = new FileInputStream(filePath);
        limiter = RateLimiter.create(bytesPerSecond);
    }

    @Override
    public int available() throws IOException {
        return innerStream.available();
    }

    @Override
    public void close() throws IOException {
        innerStream.close();
    }

    @Override
    public int read() throws IOException {
        limiter.acquire(1);
        return innerStream.read();
    }

    @Override
    public int read(@Nonnull byte[] bytes) throws IOException {
        limiter.acquire(bytes.length);
        return innerStream.read(bytes);
    }

    @Override
    public int read(@Nonnull byte[] bytes, int offset, int len) throws IOException {
        limiter.acquire(len);
        return innerStream.read(bytes, offset, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return innerStream.skip(n);
    }
}
