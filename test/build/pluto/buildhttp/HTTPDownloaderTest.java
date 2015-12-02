package build.pluto.buildhttp;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import build.pluto.builder.BuildManagers;
import build.pluto.builder.BuildRequest;
import build.pluto.test.build.ScopedBuildTest;
import build.pluto.test.build.ScopedPath;

public class HTTPDownloaderTest extends ScopedBuildTest {
	@ScopedPath("")
	private File locationOnLocal;
	private String remoteLocation;
	private String fileName;
	private long interval;

	@Test
	public void testDownload() throws Throwable {
		this.remoteLocation = "http://www.antlr.org/download/antlr-4.4-complete.jar";
		this.fileName = "antlr-4.4-complete.jar";
		interval = 0L;
		build();
		File downloadedFile = new File(locationOnLocal, fileName);
		assertTrue(downloadedFile.exists());
	}

	@Test
	public void testDownloadTwiceBuilderDoesNotFail() throws Throwable {
		this.remoteLocation = "http://www.antlr.org/download/antlr-4.4-complete.jar";
		this.fileName = "antlr-4.4-complete.jar";
		interval = 0L;
		build();
		build();
		File downloadedFile = new File(locationOnLocal, fileName);
		assertTrue(downloadedFile.exists());
	}
	
	@Test
	public void testDownloadTwiceIncremental() throws Throwable {
		this.remoteLocation = "http://www.antlr.org/download/antlr-4.4-complete.jar";
		this.fileName = "antlr-4.4-complete.jar";
		interval = 10000L;
		
		long start1 = System.currentTimeMillis();
		build();
		long end1 = System.currentTimeMillis();
		long duration1 = end1 - start1;
		
		long start2 = System.currentTimeMillis();
		build();
		long end2 = System.currentTimeMillis();
		long duration2 = end2 - start2;

		assertTrue("Interval was chosen to small, test is invalid", end2 - start1 < interval);
		assertTrue("Second build redownloaded the file, which should have been reused", duration2 < 0.1*duration1);
		
		File downloadedFile = new File(locationOnLocal, fileName);
		assertTrue(downloadedFile.exists());
	}

	@Test(expected = IOException.class)
	public void testRemoteLocationCannotBeAccessed() throws Throwable {
		this.remoteLocation = "http://www.antlr.org/NOT-DOWNLOAD/antlr-4.4-complete.jar";
		this.fileName = "antlr-4.4-complete.jar";
		interval = 0L;
		build();
		assertTrue(false);
	}

	private void build() throws Throwable {
		HTTPInput input = new HTTPInput(remoteLocation, new File(locationOnLocal, fileName), interval);
		BuildRequest<?, ?, ?, ?> buildRequest = new BuildRequest<>(HTTPDownloader.factory, input);
		BuildManagers.build(buildRequest);
	}
}
