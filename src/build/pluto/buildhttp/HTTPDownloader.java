package build.pluto.buildhttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import build.pluto.builder.Builder;
import build.pluto.builder.BuilderFactory;
import build.pluto.builder.BuilderFactoryFactory;
import build.pluto.dependency.RemoteRequirement;
import build.pluto.output.None;

public class HTTPDownloader extends Builder<HTTPInput, None> {
    public static BuilderFactory<HTTPInput, None, HTTPDownloader> factory
        = BuilderFactoryFactory.of(HTTPDownloader.class, HTTPInput.class);

    public HTTPDownloader(HTTPInput input) {
        super(input);
    }

    @Override
    protected String description(HTTPInput input) {
        return "HTTP download " + input.remoteLocation;
    }

    @Override
    public File persistentPath(HTTPInput input) {
        return new File(input.locationOnLocal.getAbsolutePath() + ".http.dep");
    }

    @Override
    protected None build(HTTPInput input) throws Throwable {
        File localFile = input.locationOnLocal.getAbsoluteFile();
        URL remoteURL = new URL(input.remoteLocation);
        RemoteRequirement httpRequirement = new HTTPRequirement(
                new File(input.locationOnLocal.getAbsolutePath() + "http.dep.time"),
                input.consistencyCheckInterval,
                localFile,
                remoteURL);
        requireOther(httpRequirement);

        //get file
        HttpURLConnection httpConnection = (HttpURLConnection) remoteURL.openConnection();
        if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) 
        	throw new IOException("HTTP request could not be sent: " + httpConnection.getResponseMessage());
    	try (	InputStream inputStream = httpConnection.getInputStream();
				FileOutputStream outputStream = new FileOutputStream(localFile)) {
			int readBytes = -1;
			int BUFFER_SIZE = 4096;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((readBytes = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
		}
        httpConnection.disconnect();
        return None.val;
    }
}
