package build.pluto.buildhttp;

import build.pluto.builder.BuildManagers;
import build.pluto.builder.BuildRequest;
import build.pluto.builder.RequiredBuilderFailed;
import build.pluto.test.build.ScopedPath;
import build.pluto.test.build.ScopedBuildTest;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class HTTPDownloaderTest extends ScopedBuildTest {
    @ScopedPath("")
    private File locationOnLocal;
    private String remoteLocation;
    private String fileName;
    private long interval;

    @Test
    public void testDownload() throws IOException {
        this.remoteLocation =
             "http://www.antlr.org/download/antlr-4.4-complete.jar";
        this.fileName = "antlr-4.4-complete.jar";
        interval = 0L;
        build();
        File downloadedFile = new File(locationOnLocal, fileName);
        assertTrue(downloadedFile.exists());
    }

    @Test
    public void testDownloadTwiceBuilderDoesNotFail() throws IOException {
        this.remoteLocation =
             "http://www.antlr.org/download/antlr-4.4-complete.jar";
        this.fileName = "antlr-4.4-complete.jar";
        interval = 0L;
        build();
        build();
        File downloadedFile = new File(locationOnLocal, fileName);
        assertTrue(downloadedFile.exists());
    }

    @Test(expected = RequiredBuilderFailed.class)
    public void testRemoteLocationCannotBeAccessed() throws IOException {
        this.remoteLocation =
             "http://www.antlr.org/downlioad/antlr-4.4-complete.jar";
        this.fileName = "antlr-4.4-complete.jar";
        interval = 0L;
        build();
        File downloadedFile = new File(locationOnLocal, fileName);
        assertTrue(downloadedFile.exists());
    }

    private void build() throws IOException {
        HTTPInput input =
            new HTTPInput(remoteLocation, locationOnLocal, fileName, interval);
        BuildRequest<?, ?, ?, ?> buildRequest =
            new BuildRequest(HTTPDownloader.factory, input);
        BuildManagers.build(buildRequest);
    }
}
