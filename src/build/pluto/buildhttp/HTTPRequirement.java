package build.pluto.buildhttp;

import build.pluto.builder.BuildUnitProvider;
import build.pluto.dependency.RemoteRequirement;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequirement extends RemoteRequirement {
    
    private static final long serialVersionUID = 1L;
    
	private final File localResource;
    private final URL url;

    public HTTPRequirement(File persistentPath, long consistencyCheckInterval, File localResource, URL url) {
        super(persistentPath, consistencyCheckInterval);
        this.localResource = localResource;
        this.url = url;
    }

    @Override
    public boolean isConsistentWithRemote() {
        return false;
    }

    @Override
    protected boolean isRemoteResourceAccessible() {
        try {
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.connect();
            int response = httpCon.getResponseCode();
            return response == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected boolean isLocalResourceAvailable() {
        return localResource.exists();
    }
    
    @Override
    public boolean tryMakeConsistent(
            BuildUnitProvider manager) throws IOException {
        return this.isConsistent();
    }
}
