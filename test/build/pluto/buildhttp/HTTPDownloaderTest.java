package build.pluto.buildhttp;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import build.pluto.BuildUnit;
import build.pluto.builder.Builder;
import build.pluto.output.Output;
import build.pluto.util.NoReporting;
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
		
		build();
		buildAndAssertNoRun();

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
		BuildManagers.build(buildRequest, new NoReporting() {
			@Override
			public <O extends Output> void skippedBuilder(BuildRequest<?, O, ?, ?> req, BuildUnit<O> unit) {
				throw new AssertionError("Builder should be executed but was skipped.");
			}
		});
	}

	private void buildAndAssertNoRun() throws Throwable {
		HTTPInput input = new HTTPInput(remoteLocation, new File(locationOnLocal, fileName), interval);
		BuildRequest<?, ?, ?, ?> buildRequest = new BuildRequest<>(HTTPDownloader.factory, input);
		BuildManagers.build(buildRequest, new NoReporting() {
			@Override
			public <O extends Output> void startedBuilder(BuildRequest<?, O, ?, ?> req, Builder<?, ?> b, BuildUnit<O> oldUnit, Set<BuildReason> reasons) {
				throw new AssertionError("Builder should not be executed a second time.");
			}
		});
	}
}
